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
import isamrs.tim21.klinika.domain.MedicinskaSestra;
import isamrs.tim21.klinika.domain.MedicinskoOsoblje;
import isamrs.tim21.klinika.domain.Pregled;
import isamrs.tim21.klinika.domain.Sala;
import isamrs.tim21.klinika.domain.TipPregleda;
import isamrs.tim21.klinika.domain.UpitZaPregled;
import isamrs.tim21.klinika.domain.VrstaTipaPregleda;
import isamrs.tim21.klinika.domain.ZahtevZaGodisnji;
import isamrs.tim21.klinika.dto.CustomResponse;
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

	@Transactional(readOnly=false, isolation = Isolation.READ_COMMITTED)
	public CustomResponse<Pregled> add(Klinika klinika, Pregled pregled) {
		pregled.setId(null);
		pregled.setKlinika(klinika);
		pregled.setUpiti(new ArrayList<UpitZaPregled>());
		
		Lekar lekar = (Lekar)osobljeRepository.findLekarByIdKlinikeAndByIdPessimisticRead(klinika.getId(), pregled.getLekar().getId());
		if(lekar == null){
			return new CustomResponse<Pregled>(null, false,
					"Greska: Trazeni lekar nije pronadjen u klinici.");
		}
		pregled.setLekar(lekar);
		
		TipPregleda tipPregleda = tipPregledaRepository.findByIdKlinikeAndIdPessimisticRead(klinika.getId(), pregled.getTipPregleda().getId());
		if(tipPregleda == null){
			return new CustomResponse<Pregled>(null, false,
					"Greska: Trazeni tip pregleda nije pronadjen u klinici.");
		}
		pregled.setTipPregleda(tipPregleda);
		
		Sala sala = salaRepository.findByIdKlinikeAndIdSalePessimisticRead(klinika.getId(), pregled.getSala().getId());
		if(sala == null){
			return new CustomResponse<Pregled>(null, false,
					"Greska: Trazena sala nije pronadjen u klinici.");
		}
		pregled.setSala(sala);
		
		for(int i = 0; i < pregled.getDodatniLekari().size(); i++){
			Lekar l = (Lekar)osobljeRepository.findLekarByIdKlinikeAndByIdPessimisticRead(klinika.getId(), pregled.getDodatniLekari().get(i).getId());
			if(l == null){
				return new CustomResponse<Pregled>(null, false,
						"Greska: Jedan od dodatnih lekara nije pronadjen u klinici.");
			}
			pregled.getDodatniLekari().set(i, l);
		}
		return validateAll(klinika, pregled);
	}

	public CustomResponse<Pregled> update(Klinika klinika, Pregled pregled, Long idPregleda) throws Exception{
		pregled.setId(idPregleda);
		
		/* VALIDACIJE: */		
		/* Da li trazeni pregled postoji*/
		if(pregledRepository.findByIdKlinikeAndIdPregleda(klinika.getId(), idPregleda) == null){
			return new CustomResponse<Pregled>(null, false,
					"Greska: Trazeni pregled nije pronadjen u klinici");
		}
		pregled.setKlinika(klinika);
		return validateAll(klinika, pregled);
		
	}
	
	@Transactional(readOnly=false)
	public ResponseEntity<CustomResponse<Boolean>> delete(Long idKlinike, Long idPregleda, Long version) throws Exception{
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return new ResponseEntity<CustomResponse<Boolean>>(
					new CustomResponse<Boolean>(false, false, "Greska: Klinika nije pronadjen."), HttpStatus.NOT_FOUND);
		}
		if(posetaRepository.findByIdPregleda(idPregleda) != null){
			return new ResponseEntity<CustomResponse<Boolean>>(
					new CustomResponse<Boolean>(true, false, "Greska: Pregled je rezervisan. Ne mozete ga obrisati. Osvezite stranicu"), HttpStatus.OK);
		}
		Pregled p = pregledRepository.findById(idPregleda).get();
		if(p == null){
			return new ResponseEntity<CustomResponse<Boolean>>(
					new CustomResponse<Boolean>(false, false, "Greska: Pregled nije pronadjen."), HttpStatus.NOT_FOUND);
		}
		for(UpitZaPregled up : p.getUpiti()){
			if(!up.getAdminObradio()){
				return new ResponseEntity<CustomResponse<Boolean>>(
						new CustomResponse<Boolean>(true, false, "Greska: Postoji upit za ovaj pregled za koji administrator jos uvek nije obradio."), HttpStatus.OK);
			}
			if(!up.getPacijentObradio()){
				return new ResponseEntity<CustomResponse<Boolean>>(
						new CustomResponse<Boolean>(true, false, "Greska: Postoji upit za ovaj pregled za koji pacijent jos uvek nije video odgovor administratora."), HttpStatus.OK);
			}
		}
		upitZaPregledRepository.deleteAll(p.getUpiti());
		p.setUpiti(new ArrayList<>());
		pregledRepository.save(p); //nece inkrementirati verziju?
		
		Pregled pregledToDelete = new Pregled();
		pregledToDelete.setId(idPregleda);
		pregledToDelete.setVersion(version);
		pregledRepository.delete(pregledToDelete);
		
		return new ResponseEntity<CustomResponse<Boolean>>(
				new CustomResponse<Boolean>(true, true, "OK"), HttpStatus.OK);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	private CustomResponse<Pregled> validateAll(Klinika klinika, Pregled pregled) {
		/*
			Serialiazble zato sto je potrebno zalkjucati tabelu pregleda za dodavanje dok se ne izvrse sve provere koje su neophodne
			za dodavanje ovog pregleda.
		*/

		/* VALIDACIJE: */
		/* Da li su svi entiteti unutar iste klinike kao i pregled koji se dodaje? ODRADJENO UNUTAR OSTALIH METODA*/

		/* Da li je trazena sala zauzeta i da li uopste postoji? */
		if(!validateSala(klinika, pregled)){
			return new CustomResponse<Pregled>(null, false,
					"Greska: Trazena sala nije slobodna za uneti vremenski interval trajanja pregleda");
		}
		
		/* Da li je lekar slobodan u to vreme? */
		/* Da li je lekar specijalizovan za dati tip pregleda? */
		/* Da li je lekar na odustvu u datom terminu pregleda? */
		if(!validateOsoblje(klinika, pregled)){
			return new CustomResponse<Pregled>(null, false,
					"Greska: Lekar ne moze da izvrsi ovaj pregled. "
					+ "Proverite da li je lekar zauzet u datom vremenskom intervalu, da li je uopste specijalizovan za dati tip pregleda i da li je na odsustvu.");
		}
		
		/*
		 * Da li su svi dodatni lekari slobodni u datom terminu
		 */
		if(pregled.getTipPregleda().getVrsta() == VrstaTipaPregleda.operacija && !validateDodatnoOsoblje(klinika, pregled)){
			return new CustomResponse<Pregled>(null, false,
					"Greska: Neki od dodatnihlekara nije slobodan u datom terminu pregleda");
		}
		return new CustomResponse<Pregled>(pregledRepository.save(pregled), true, "OK"); 
	}
	
	@Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.SERIALIZABLE)
	private boolean validateSala(Klinika klinika, Pregled pregled) {
		Sala sala = salaRepository.findByIdKlinikeAndIdSale(klinika.getId(), pregled.getSala().getId());
		if(sala == null){
			return false;
		}
		
		//vrati false ukoliko postoji pregled koji se odrzava u ovoj sali
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
		for(Lekar lekar : pregled.getDodatniLekari()){
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
			//vrati false ukoliko je lekar na odsustvu u datom intervalu
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
		MedicinskoOsoblje osoblje = osobljeRepository.findByIdKlinikeAndById(klinika.getId(), pregled.getLekar().getId());
		if(osoblje == null || osoblje instanceof MedicinskaSestra)
			return false;
		Lekar lekar = (Lekar) osoblje;
		
		//vrati false ukoliko postoji pregled koji vodi ovaj lekar
		for(Pregled p : lekar.getPregledi()){
			if(p.getId() == pregled.getId())
				continue;
			if(p.intersects(pregled)){
				return false;
			}
		}

		//vrati false ukoliko postoji dodatna operacija na kojoj je angazovan ovaj lekar
		for(Pregled p : lekar.getDodatneOperacije()){
			if(p.getId() == pregled.getId())
				continue;
			if(p.intersects(pregled)){
				return false;
			}
		}
		
		//vrati false ukoliko lekar nije specijalizovan za ovaj tip pregleda
		if(!lekar.getTipovi_pregleda().contains(pregled.getTipPregleda()))
			return false;

		//vrati false ukoliko je lekar na odsustvu u datom intervalu
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
	public List<Pregled> getAll(Long idKlinike) {
		if(!klinikaRepository.findById(idKlinike).isPresent()){
			return null;
		}else{
			return pregledRepository.findAllByIdKlinike(idKlinike);
		}
	}
	
	@Transactional(readOnly=true)
	public Pregled get(Long idKlinike, Long idPregleda) {
		if(!klinikaRepository.findById(idKlinike).isPresent()){
			return null;
		}else{
			return pregledRepository.findByIdKlinikeAndIdPregleda(idKlinike, idPregleda);
		}
	}

	@Transactional(readOnly=false)
	public ResponseEntity<CustomResponse<Pregled>> add(Long idKlinike, Pregled pregled) {
		Klinika klinika = klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			CustomResponse<Pregled> customResponse = new CustomResponse<Pregled>(null, false,
					"Greska: Trazena klinika ne postoji");
			return new ResponseEntity<CustomResponse<Pregled>>(customResponse, HttpStatus.NOT_FOUND);
		}else{
			CustomResponse<Pregled> customResponse = add(klinika, pregled);
			return new ResponseEntity<CustomResponse<Pregled>>(customResponse, HttpStatus.OK);
		}
	}

	@Transactional(readOnly=false)
	public ResponseEntity<CustomResponse<Pregled>> update(Long idKlinike, Long idPregleda, Pregled pregled) {
		Klinika klinika = klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			CustomResponse<Pregled> customResponse = new CustomResponse<Pregled>(null, false,
					"Greska: Trazena klinika ne postoji");
			return new ResponseEntity<CustomResponse<Pregled>>(customResponse, HttpStatus.NOT_FOUND);
		}else{
			CustomResponse<Pregled> customResponse = null;
			try{
				customResponse = update(klinika, pregled, idPregleda);
			}catch(Exception e){
				return new ResponseEntity<CustomResponse<Pregled>>(
						new CustomResponse<Pregled>(null, false, "Izuzetak pri optimistickom zakljucavanju. Osvezite stranicu i pokusajte ponovo"),
						HttpStatus.OK);
			}
			return new ResponseEntity<CustomResponse<Pregled>>(customResponse, HttpStatus.OK);
		}
	}
}
