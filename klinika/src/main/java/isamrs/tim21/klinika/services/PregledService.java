package isamrs.tim21.klinika.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.Pregled;
import isamrs.tim21.klinika.domain.Sala;
import isamrs.tim21.klinika.domain.TipPregleda;
import isamrs.tim21.klinika.domain.UpitZaPregled;
import isamrs.tim21.klinika.domain.VrstaTipaPregleda;
import isamrs.tim21.klinika.domain.ZahtevZaGodisnji;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.exceptions.BusinessLogicException;
import isamrs.tim21.klinika.exceptions.EntityNotFoundException;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.OsobljeRepository;
import isamrs.tim21.klinika.repository.PosetaRepository;
import isamrs.tim21.klinika.repository.PregledRepository;
import isamrs.tim21.klinika.repository.SalaRepository;
import isamrs.tim21.klinika.repository.TipPregledaRepository;
import isamrs.tim21.klinika.repository.UpitZaPregledRepository;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

@Service
public class PregledService {

	@Autowired
	PregledRepository pregledRepository;
	
	@Autowired
	SalaRepository salaRepository;
	
	@Autowired
	TipPregledaRepository tipPregledaRepository;
	
	@Autowired
	OsobljeRepository osobljeRepository;
	
	@Autowired
	PosetaRepository posetaRepository;
	
	@Autowired
	KlinikaRepository klinikaRepository;
	
	@Autowired
	UpitZaPregledRepository upitZaPregledRepository;

	//anotacija ispod se gleda samo kaad se ova metoda pozove van instance trenutnog bean-a, u nasem slucaju iz upitZaPregled servisa
	@Transactional(readOnly=false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY)
	public CustomResponse<Pregled> add(Klinika klinika, Pregled pregled) throws EntityNotFoundException{
		pregled.setId(null);
		pregled.setKlinika(klinika);
		pregled.setUpiti(new ArrayList<UpitZaPregled>());
		
		/*
		U cilju sprecavanja metode za uklanjanje specijalnosti lekara da se izvrsava konkurentno sa ovom metodom nad 
		istom instancom lekara, instanca lekara bi ovde mogla da se dobavlja samo sa shared lock-om (jer se pri uklanjanju
		specijalizacije lekara, lekar dobavlja sa exclusive lock-om, tako da ove dve transakcije ne bi mogle da rade u paraleli nad
		istim lekarom). Medjutim, kako treba i spreciti da se konkurentno dodaju dva pregleda kod istog lekara, lekar se ipak 
		dobavlja sa exclusive lock-om. To ce spreciti vise transakcija za dodavanje pregleda kod istog lekara da rade u paraleli.
		*/
		Lekar lekar = (Lekar)osobljeRepository.findLekarByIdKlinikeAndByIdPessimisticWrite(klinika.getId(), pregled.getLekar().getId());
		if(lekar == null)
			throw new EntityNotFoundException("Lekar");
		pregled.setLekar(lekar);
		
		/*
		Kao i u gornjem slucaju, tip pregleda bi mogao da se dobavlja sa shared lock-om, kako bi sprecili transakciju za brisanje
		tipa pregleda(koja dobavlja tip pregleda sa exclusive lock-om) da se izvrsava u paraleli sa ovom transakcijom nad istom 
		instancom tipa pregleda. Kako nema razloga da se spreci simultano dodavanje dva pregleda za isti tip pregleda, tip pregleda
		se dobavlja sa shared lock-om.
		*/
		TipPregleda tipPregleda = tipPregledaRepository.findByIdKlinikeAndIdPessimisticRead(klinika.getId(), pregled.getTipPregleda().getId());
		if(tipPregleda == null)
			throw new EntityNotFoundException("Tip pregleda");
		pregled.setTipPregleda(tipPregleda);
		
		/*
			Za salu vazi isti slican kao i za lekara, jer je moguce da se dva pregleda simultano dodaju za istu salu,
			a da im se vreme odrzavanja preklapa
		*/
		Sala sala = salaRepository.findByIdKlinikeAndIdSalePessimisticWrite(klinika.getId(), pregled.getSala().getId());
		if(sala == null)
			throw new EntityNotFoundException("Sala");
		pregled.setSala(sala);
		
		/*
			Za dodatne lekare vazi isti rezon kao i za lekara
		*/
		for(int i = 0; i < pregled.getDodatniLekari().size(); i++){
			Lekar l = (Lekar)osobljeRepository.findLekarByIdKlinikeAndByIdPessimisticWrite(klinika.getId(), pregled.getDodatniLekari().get(i).getId());
			if(l == null)
				throw new EntityNotFoundException("Lekar");
			pregled.getDodatniLekari().set(i, l);
		}
		return validateAll(klinika, pregled);
	}
	
	@Transactional(readOnly=false, isolation = Isolation.READ_COMMITTED)
	public ResponseEntity<CustomResponse<Boolean>> delete(Long idKlinike, Long idPregleda, Long version)
		throws EntityNotFoundException, BusinessLogicException{
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null)
			throw new EntityNotFoundException("Klinika");
		
		/*
		Cilj je spreciti metodu za odobravanje upita za unapred definisani pregled da se izvrsava simultano sa ovom metodom
		Naravno samo u slucaju da se izvrsavaju nad istom instancom pregleda.
		Zato ovde pregled dobavimo sa exclusive lock-om prvo, a u metodi za odobravanje upita sa shared lock-om
		*/
		Pregled p = pregledRepository.findByIdKlinikeAndIdPregledaPessimisticWrite(idKlinike, idPregleda);
		if(p == null)
			throw new EntityNotFoundException("Pregled");

		if(version != p.getVersion()) //kako je pregled dobavljen exclusive lock-om, smemo rucnoporediti verzije
			throw new BusinessLogicException("Greška: Verzija pregleda je zastarela.");
		
		if(posetaRepository.findByIdPregleda(idPregleda) != null)
			throw new BusinessLogicException("Greška: Pregled je već rezervisan. Ne možete ga obrisati.");
		
		/*
		Takodje je cilj spreciti brisanje pregleda ako postoje neobradjeni upiti za njega
		Kako u odgovarajucim metodama dobavljamo shared lock nad ovim pregledom, konkurentni pristup nece biti moguc
		*/
		for(UpitZaPregled up : p.getUpiti()){
			if(!up.getAdminObradio())
				throw new BusinessLogicException("Greška: Postoji upit za ovaj pregled za koji administrator još uvek nije obradio.");
			if(!up.getPacijentObradio())
				throw new BusinessLogicException("Greška: Postoji upit za ovaj pregled za koji pacijent još uvek nije video odgovor administratora.");
		}
		upitZaPregledRepository.deleteAll(p.getUpiti());
		p.setUpiti(new ArrayList<>());
		pregledRepository.save(p); //nece inkrementirati verziju?
		
		pregledRepository.delete(p);
		return new ResponseEntity<CustomResponse<Boolean>>(
				new CustomResponse<Boolean>(true, true, "OK"), HttpStatus.OK);
	}
	
	private CustomResponse<Pregled> validateAll(Klinika klinika, Pregled pregled) throws BusinessLogicException{
		/*
			Ova metoda se poziva iz add(klinika, Pregled) metode koja je vec zakljucala odgovarajucim lock-om sve entitete
			koje pregled referencira.
		*/

		/* VALIDACIJE: */
		/* Da li su svi entiteti unutar iste klinike kao i pregled koji se dodaje? ODRADJENO UNUTAR OSTALIH METODA*/

		/* Da li je trazena sala zauzeta i da li uopste postoji? */
		if(!validateSala(klinika, pregled))
			throw new BusinessLogicException("Greška: Tražena sala nije slobodna za uneti vremenski interval trajanja pregleda");
		
		/* Da li je lekar slobodan u to vreme? */
		/* Da li je lekar specijalizovan za dati tip pregleda? */
		/* Da li je lekar na odustvu u datom terminu pregleda? */
		if(!validateOsoblje(klinika, pregled))
			throw new BusinessLogicException("Greska: Lekar ne moze da izvrsi ovaj pregled. "
			+ "Proverite da li je lekar zauzet u datom vremenskom intervalu, da li je uopste specijalizovan za dati tip pregleda i da li je na odsustvu.");
		
		/*
		 * Da li su svi dodatni lekari slobodni u datom terminu
		 */
		if(pregled.getTipPregleda().getVrsta() == VrstaTipaPregleda.operacija && !validateDodatnoOsoblje(klinika, pregled))
			throw new BusinessLogicException("Greska: Neki od dodatnihlekara nije slobodan u datom terminu pregleda");
		
		return new CustomResponse<Pregled>(pregledRepository.save(pregled), true, "OK"); 
	}
	
	private boolean validateSala(Klinika klinika, Pregled pregled) {
		/*
			Sala je vec zakljucana exclusive lock-om i dobavljena u radnu memoriju
		*/
		Sala sala = pregled.getSala(); //ovo nece izazvati upit u bazu podataka
		
		/*
		vrati false ukoliko postoji pregled koji se odrzava u ovoj sali za vreme koje se preklapa sa ovim pregledom
		Kako je sala zakljucana sa exclusive lock-om, nemoguce je da se u toku ove provere doda neki novi pregled za ovu salu
		Moguce je da se neki pregled izbrise, al to nece narusiti konzistentnost podataka
		*/
		for(Pregled p : sala.getPregledi()){
			if(p.getId() == pregled.getId())
				continue;
			if(p.intersects(pregled)){
				return false;
			}
		}
		return true;
	}

	@Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.SERIALIZABLE)
	private boolean validateDodatnoOsoblje(Klinika klinika, Pregled pregled){
		/*
			Svaki lekar je vec zakljucan exclusive lock-om i dobavljen u radnu memoriju
		*/
		for(Lekar lekar : pregled.getDodatniLekari()){
			//ista logika kao u validateOsoblje
			for(Pregled p : lekar.getPregledi()){
				if(p.getId() == pregled.getId())
					continue;
				if(p.intersects(pregled)){
					return false;
				}
			}
			for(Pregled p : lekar.getDodatneOperacije()){
				if(p.getId() == pregled.getId())
					continue;
				if(p.intersects(pregled)){
					return false;
				}
			}
			if(!lekar.getTipovi_pregleda().contains(pregled.getTipPregleda()))
				return false;
			for(ZahtevZaGodisnji zahtev : lekar.getRadniKalendar().getZahteviZaGodisnjiOdmor()){
				if(!zahtev.isOdobreno())
					continue;
				if(pregled.intersects(zahtev))
					return false;
			}
		}
		return true;
	}
	
	@Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.SERIALIZABLE)
	private boolean validateOsoblje(Klinika klinika, Pregled pregled) {
		/*
			Lekar je vec zakljucan exclusive lock-om i dobavljen u radnu memoriju
		*/
		Lekar lekar = pregled.getLekar();
		
		/*
		vrati false ukoliko postoji pregled koji se odrzava kod ovog lekara za vreme koje se preklapa sa ovim pregledom
		Kako je lekar zakljucan sa exclusive lock-om, nemoguce je da se u toku ove provere doda neki novi pregled za ovog lekara
		Moguce je da se neki pregled izbrise, al to nece narusiti konzistentnost podataka
		*/
		for(Pregled p : lekar.getPregledi()){
			if(p.getId() == pregled.getId())
				continue;
			if(p.intersects(pregled)){
				return false;
			}
		}

		//isto sto vazi za preglede ovog lekara, vazi i za dodatne operacije
		for(Pregled p : lekar.getDodatneOperacije()){
			if(p.getId() == pregled.getId())
				continue;
			if(p.intersects(pregled)){
				return false;
			}
		}
		
		/*
		vrati false ukoliko lekar nije specijalizovan za ovaj tip pregleda
		Nemoguce je da se u toku ove provere ukloni specijalnost za ovog lekara, jer je ovaj lekar zakljucan exclusive lock-om
		*/
		if(!lekar.getTipovi_pregleda().contains(pregled.getTipPregleda()))
			return false;

		//vrati false ukoliko je lekar na odsustvu u datom intervalu
		//metoda za odobravanje zahteva ima isolation type SERIALIZABLE, tako da ovde nemamo problem
		for(ZahtevZaGodisnji zahtev : lekar.getRadniKalendar().getZahteviZaGodisnjiOdmor()){
			if(!zahtev.isOdobreno())
				continue;
			if(pregled.intersects(zahtev))
				return false;
		}
		return true;
	}

	public List<Pregled> findSlobodni(Long idKlinike) {
		Klinika k = klinikaRepository.getOne(idKlinike);
		List<Pregled> retval = pregledRepository.findByKlinikaAndPosetaIsNull(k);
		List<Pregled> r = new ArrayList<Pregled>();
		for (Pregled p : retval) {
			boolean flag = false;
			for (UpitZaPregled u : p.getUpiti()) {
				if (u.getPotvrdjen()) {
					flag = true;
					break;
				}
				else if (u.getOdobren() && !u.getPacijentObradio()) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				r.add(p);
			}
		}
		return r;
	}

	@Transactional(readOnly=true)
	public List<Pregled> getAll(Long idKlinike) throws EntityNotFoundException{
		if(!klinikaRepository.findById(idKlinike).isPresent())
			throw new EntityNotFoundException("Klinika");
		else
			return pregledRepository.findAllByIdKlinike(idKlinike);
	}
	
	@Transactional(readOnly=true)
	public Pregled get(Long idKlinike, Long idPregleda) {
		if(!klinikaRepository.findById(idKlinike).isPresent()){
			return null;
		}else{
			return pregledRepository.findByIdKlinikeAndIdPregleda(idKlinike, idPregleda);
		}
	}

	@Transactional(readOnly=false, isolation = Isolation.READ_COMMITTED)
	public ResponseEntity<CustomResponse<Pregled>> add(Long idKlinike, Pregled pregled) throws EntityNotFoundException{
		Klinika klinika = klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null)
			throw new EntityNotFoundException("Klinika");
		else{
			CustomResponse<Pregled> customResponse = add(klinika, pregled);
			return new ResponseEntity<CustomResponse<Pregled>>(customResponse, HttpStatus.OK);
		}
	}
}
