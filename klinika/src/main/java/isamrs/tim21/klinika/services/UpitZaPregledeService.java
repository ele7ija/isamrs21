package isamrs.tim21.klinika.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.Pacijent;
import isamrs.tim21.klinika.domain.Poseta;
import isamrs.tim21.klinika.domain.Pregled;
import isamrs.tim21.klinika.domain.TipPregleda;
import isamrs.tim21.klinika.domain.UpitZaPregled;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.dto.UpitZaPregledDTO;
import isamrs.tim21.klinika.exceptions.BusinessLogicException;
import isamrs.tim21.klinika.exceptions.EntityNotFoundException;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.KorisniciRepository;
import isamrs.tim21.klinika.repository.OsobljeRepository;
import isamrs.tim21.klinika.repository.PacijentRepository;
import isamrs.tim21.klinika.repository.PregledRepository;
import isamrs.tim21.klinika.repository.SalaRepository;
import isamrs.tim21.klinika.repository.TipPregledaRepository;
import isamrs.tim21.klinika.repository.UpitZaPregledRepository;
import isamrs.tim21.klinika.repository.ZahtevZaGodisnjiRepository;

@Service
public class UpitZaPregledeService {
	@Autowired
	UpitZaPregledRepository upitZaPregledRepository;
	
	@Autowired
	KlinikaRepository klinikaRepository;
	
	@Autowired
	KorisniciRepository korisniciRepository;
	
	@Autowired
	TipPregledaRepository tipPregledaRepository;
	
	@Autowired
	OsobljeRepository osobljeRepository;

	@Autowired
	PregledRepository pregledRepository;
	
	@Autowired
	PregledService pregledService;
	
	@Autowired
	PosetaService posetaService;
	
	@Autowired
	SalaRepository salaRepository;
	
	@Autowired
	PacijentRepository pacijentRepository;

	@Autowired
	MailService mailService;

	@Autowired
	ZahtevZaGodisnjiRepository zahtevZaGodisnjiRepository;

	@Transactional(readOnly=true)
	public List<UpitZaPregled> sviUpiti(){
		return upitZaPregledRepository.sviUpiti();
	}

	@Transactional(readOnly=false)
	public UpitZaPregled save(UpitZaPregled upit){
		return upitZaPregledRepository.save(upit);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public CustomResponse<UpitZaPregled> odobriUnapredDefinisani(Long idKlinike, Long idUpitaZaPregled, UpitZaPregled u)
		throws EntityNotFoundException, BusinessLogicException{	
		/*
			Pregled se dobavlja u pessimistic write rezimu 
			kako bi sprecili paralelnu obradu(odobravanje ili odbijanje) dva ISTA ili RAZLICITA upita za ISTI pregled
		*/
		Pregled p = pregledRepository.findByIdKlinikeAndIdPregledaPessimisticWrite(idKlinike, u.getUnapredDefinisaniPregled().getId());
		
		//kako je pregled vec zakljucan sa exclusive lock-om, zakljucavanje i upita bi bio nepotreban overhead
		UpitZaPregled upit = upitZaPregledRepository.findById(u.getId()).get();
		if(upit == null)
			throw new EntityNotFoundException("Upit za pregled");
		
		//smemo ovo da radimo posto je PREGLED pod exclusive lock-om, prakticno optimisticko zakljucavanje se ovde ni ne koristi
		//jer nikad 2 transakcije koje konkurentno obradjuju isti upit nece proci liniju 91
		if(upit.getAdminObradio())
			throw new BusinessLogicException("Greska. Upit je vec obradjen. Osvezite stranicu");
		
		//proveri da li je pregled vec rezervisan i potvrdjen od strane pacijenta
		if(p.getPoseta() != null){
			upit.setAdminObradio(true);
			upit.setOdobren(false);
			upit = upitZaPregledRepository.save(upit);
			//ne bacamo exception kako se transakcija ne bi rollback-ovala
			return new CustomResponse<>(upit, false, "Obavestenje: Ovaj pregled je vec rezervisan, te je iz tog razloga upit ipak odbijen.");
		}

		//ukoliko pregled vec ima odobren upit, admin nije smeo da odobri ovaj upit
		for(UpitZaPregled drugiUpit: p.getUpiti()){
			if(drugiUpit.getId().equals(upit.getId()))
				continue;

			//ovaj if je redundantan jer bi za ovaj pregled onda postojala poseta, al aj nek ostane
			if(drugiUpit.getOdobren() && drugiUpit.getPotvrdjen()){
				upit.setAdminObradio(true);
				upit.setOdobren(false);
				upit = upitZaPregledRepository.save(upit);
				//ne bacamo exception kako se transakcija ne bi rollback-ovala
				return new CustomResponse<>(upit, false, "Obavestenje: Ovaj pregled je vec rezervisan, te je iz tog razloga upit ipak odbijen.");
			}
			if(drugiUpit.getOdobren() && !drugiUpit.getPacijentObradio()){
				upit.setAdminObradio(false);
				upit.setOdobren(false);
				upitZaPregledRepository.save(upit);
				//e sad ipak treba otkazati transakciju, baci exception
				throw new BusinessLogicException("Obavestenje: Ovaj pregled je vec odobren. Mocicete da odobrite ovaj pregled samo u slucaju da pacijent kojem je ovaj pregled odobren ipak odluci da ne potvrdi rezervaciju.");
			}
		}
		
		//sve je ok, sacuvaj pregled
		upit.setAdminObradio(true);
		upit.setOdobren(true);
		upit = upitZaPregledRepository.save(upit);
		
		//slanje mejlova pacijentu i lekaru
		mailService.upitOdobren(upit, false);
		mailService.obavestiLekara(upit, upit.getLekar(), true, false);
		
		return new CustomResponse<>(upit, true, "OK.");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public CustomResponse<UpitZaPregled> odbiUnapredDefinisani(Long idKlinike, Long idUpitaZaPregled, UpitZaPregled u)
		throws EntityNotFoundException, BusinessLogicException{	
		//pregledRepository.findByIdKlinikeAndIdPregledaPessimisticRead(idKlinike, u.getUnapredDefinisaniPregled().getId());
		
		UpitZaPregled upit = upitZaPregledRepository.findById(u.getId()).get();
		if(upit == null)
			throw new EntityNotFoundException("Upit za pregled");
		
		//provera ispod sluzi samo ako na primer admin nije osvezio stranicu jako dugo pa je pokusao da odbije vec obradjen upit
		//ako se zaista konkurentno obradjuju 2 upita(odbija-dobija ili prihvata-odbija), to detektuje hibernate u liniji 162
		if(upit.getAdminObradio())
			throw new BusinessLogicException("Greska. Ovaj upit za pregled je vec obradjen. Osvezite stranicu.");

		//sve je ok, sacuvaj pregled
		upit.setAdminObradio(true);
		upit.setOdobren(false);
		upit = upitZaPregledRepository.save(upit);
		
		//slanje mejla pacijentu
		mailService.upitOdbijen(upit);

		return new CustomResponse<>(upit, true, "OK.");
	}


	@Transactional(readOnly=false, isolation = Isolation.READ_COMMITTED)
	public CustomResponse<Boolean> delete(Long idUpita, Long version) throws EntityNotFoundException, BusinessLogicException{
		UpitZaPregled upit = upitZaPregledRepository.findById(idUpita).orElse(null);
		CustomResponse<Boolean> retval;
		if(upit == null)
			throw new EntityNotFoundException("Upit za pregled.");
		else if(!upit.getPacijentObradio())
			throw new BusinessLogicException("Greska. Pacijent jos uvek nije video odgovor na vas upit.");
		else{
			if(upit.getOriginalniPregled() != null){
				UpitZaPregled originalniUpit = upit.getOriginalniPregled();
				originalniUpit.setIzmenjeniPregled(null);
				upit.setOriginalniPregled(null);
				upitZaPregledRepository.delete(originalniUpit);
			}
			UpitZaPregled upToDelete = new UpitZaPregled();
			upToDelete.setId(idUpita);
			upToDelete.setVersion(version);
			upitZaPregledRepository.delete(upToDelete);
			retval = new CustomResponse<>(true, true, "OK.");
		}
		return retval;
	}
	
	@Transactional(readOnly=true)
	public List<UpitZaPregled> getAll(Long idKlinike) {
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return null;
		}else{
			return upitZaPregledRepository.findAllByIdKlinike(klinika.getId());
		}
	}

	@Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED)
	public CustomResponse<UpitZaPregled> kreirajUpitZaPregled(UpitZaPregledDTO u)
		throws Exception {
		UpitZaPregled u2 = new UpitZaPregled(u);
		try {
			u2.setKlinika(klinikaRepository.findById(u.getKlinika()).get());
		}
		catch(NoSuchElementException e) {
			return new CustomResponse<>(null, false, "Klinika ne postoji.");
		}
		try {
			/* SPRECAVA KONKURENTNU IZMENU TIPA PREGLEDA */
			u2.setTipPregleda(tipPregledaRepository.findByIdPessimisticRead(u.getTipPregleda()));
			// if (u2.getTipPregleda().getVersion() != u.getTipPregledaVerzija()) {
			// 	throw new ObjectOptimisticLockingFailureException(TipPregleda.class, u2.getTipPregleda());
			// }
		}
		catch(NoSuchElementException e) {
			return new CustomResponse<>(null, false, "Tip pregleda ne postoji.");
		}		
		try {
			u2.setLekar((Lekar) korisniciRepository.findByIdPessimisticRead(u.getLekar()));
			// if (u2.getLekar().getVersion() != u.getLekarVerzija()) {
			// 	throw new ObjectOptimisticLockingFailureException(Lekar.class, u2.getLekar());
			// }		
		}
		catch(NoSuchElementException e) {
			return new CustomResponse<>(null, false, "Lekar ne postoji.");
		}
		try {
			u2.setPacijent((Pacijent) korisniciRepository.findByEmail(u.getPacijent()));
		}
		catch(NoSuchElementException e) {
			return new CustomResponse<>(null, false, "Pacijent ne postoji.");
		}
		try {
			// Upit je nastao na osnovu unapred def pregleda
			if (u.getPregled() != null) {
				Pregled pregled = pregledRepository.findByIdPessimisticWrite(u.getPregled());
				u2.setUnapredDefinisaniPregled(pregled);
				u2.setSala(pregled.getSala());
				// if (u2.getUnapredDefinisaniPregled().getVersion() != u.getPregledVerzija()) {
				// 	throw new ObjectOptimisticLockingFailureException(Pregled.class, u2.getUnapredDefinisaniPregled());
				// }
			}
		}
		catch(NoSuchElementException e) {
			return new CustomResponse<>(null, false, "Pregled ne postoji.");
		}		
		u2.setAdminObradio(false);
		u2.setPacijentObradio(false);
		u2.setOdobren(false);
		u2.setPotvrdjen(false);
		upitZaPregledRepository.save(u2);
		return new CustomResponse<>(u2, true, "Uspešno kreiran upit");
	}

	@Transactional(readOnly=true)
	public CustomResponse<List<UpitZaPregled>> pronadjiNeodobreneNeodradjene(String email)
		throws Exception {
		List<UpitZaPregled> l = upitZaPregledRepository.findNeodobreniNeodradjeniByEmail(email);
		return new CustomResponse<>(l, true, "Uspešno pronađeni upiti.");
	}

	@Transactional(readOnly=true)
	public CustomResponse<List<UpitZaPregled>> pronadjiNeodobreneOdradjene(String email)
		throws Exception {
		List<UpitZaPregled> l = upitZaPregledRepository.findNeodobreniOdradjeniByEmail(email);
		return new CustomResponse<>(l, true, "Uspešno pronađeni upiti.");
	}

	@Transactional(readOnly=false)
	public CustomResponse<UpitZaPregled> obradiNeodobren(Long id) 
		throws Exception {
		try {
			UpitZaPregled u = upitZaPregledRepository.findById(id).get();
			u.setPacijentObradio(true);
			if (u.getOriginalniPregled() != null) {
				u.getOriginalniPregled().setPacijentObradio(true);
			}
			if (u.getIzmenjeniPregled() != null) {
				u.getIzmenjeniPregled().setPacijentObradio(true);
			}
			upitZaPregledRepository.save(u);
			return new CustomResponse<>(u, true, "Upit uspešno obrađen.");
		}
		catch (NoSuchElementException e) {
			return new CustomResponse<>(null, false, "Upit ne postoji.");
		}
	}

	@Transactional(readOnly=true)
	public CustomResponse<List<UpitZaPregled>> pronadjiNeodobrene()
		throws Exception {
		List<UpitZaPregled> l = upitZaPregledRepository.findNeodobreni();
		return new CustomResponse<>(l, true, "Uspešno pronađeni upiti.");
	}

	@Transactional(readOnly=true)
	public CustomResponse<List<UpitZaPregled>> pronadjiNepotvrdjene(String email)
		throws Exception {
		List<UpitZaPregled> l = upitZaPregledRepository.findNepotvrdjeniByEmail(email);
		return new CustomResponse<>(l, true, "Uspešno pronađeni upiti.");
	}

	@Transactional(readOnly=true)
	public CustomResponse<List<UpitZaPregled>> pronadjiNepotvrdjene()
		throws Exception {
		List<UpitZaPregled> l = upitZaPregledRepository.findNepotvrdjeni();
		return new CustomResponse<>(l, true, "Uspešno pronađeni upiti.");
	}

	@Transactional(readOnly=false)
	public CustomResponse<UpitZaPregled> izmeniPotvrdi(Long id, Long verzija) throws Exception {
		try {
			UpitZaPregled u = upitZaPregledRepository.findById(id).get();
			if (!verzija.equals(u.getVersion())) {
				throw new ObjectOptimisticLockingFailureException(UpitZaPregled.class, u);
			}
			u.setPotvrdjen(true);
			u.setPacijentObradio(true);
			upitZaPregledRepository.save(u);
			Poseta p = posetaService.kreirajNovuPosetu(u);
			mailService.potvrdaRezervacije(p);
			return new CustomResponse<>(u, true, "Upit je potvrđen.");
		}
		catch (NoSuchElementException e){
			return new CustomResponse<>(null, false, "Upit ne postoji.");
		}
	}

	@Transactional(readOnly=false)
	public CustomResponse<UpitZaPregled> izmeniOdustani(Long id, Long verzija) throws Exception {
		try {
			UpitZaPregled u = upitZaPregledRepository.findById(id).get();
			if (!verzija.equals(u.getVersion())) {
				throw new ObjectOptimisticLockingFailureException(UpitZaPregled.class, u);
			}
			u.setPotvrdjen(false);
			u.setPacijentObradio(true);
			upitZaPregledRepository.save(u);
			mailService.odustajanjeOdRezervacije(u, u.getUnapredDefinisaniPregled().getLekar());
			for(Lekar lekar : u.getUnapredDefinisaniPregled().getDodatniLekari()){
				mailService.odustajanjeOdRezervacije(u, lekar);
			}
			return new CustomResponse<>(u, true, "Upit je izbrisan.");
		}
		catch (NoSuchElementException e){
			return new CustomResponse<>(null, false, "Upit ne postoji.");
		}
	}

	@Transactional(readOnly=false, isolation = Isolation.READ_COMMITTED)
	public CustomResponse<UpitZaPregled> odobriCustom(Long idKlinike, Long idUpita,
			UpitZaPregled upitZaPregledToChange) throws EntityNotFoundException, BusinessLogicException{
		//postavi kliniku upitu
		Klinika klinika = klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null)
			throw new EntityNotFoundException("Klinika");
		upitZaPregledToChange.setKlinika(klinika);

		//upit za pregled ne moramo da zakljucavamo pesimisticki
		//prepusticemo hibernate-u da pri save-zu proveri verzije
		UpitZaPregled upit = upitZaPregledRepository.findByIdKlinikeAndById(idKlinike, idUpita);
		if(upit == null)
			throw new EntityNotFoundException("Upit za pregled");
		//kao i kod upita za unapred definisani pregled, ovo samo sluzi ukoliko admin npr dugo nije osvezavao stranicu
		//prava provera verzija izvrsava se u linijama 473 tj. 479
		if(upit.getAdminObradio())
			throw new BusinessLogicException("Ovaj upit za pregled je već obrađen od strane administratora klinike");

		//kreiraj pregled
		Pregled pregled = new Pregled(upitZaPregledToChange);
		pregled = pregledService.add(klinika, pregled).getResult(); //kreira pregled, baca exception ako ne uspe

		//lekar je vec dobavljen u pessimistic write rezimu
		upitZaPregledToChange.setLekar(pregled.getLekar());
		
		//dodatni lekari su vec dobavljeni u pessimistic write rezimu
		int i = 0;
		for(Lekar l: pregled.getDodatniLekari()){
			upitZaPregledToChange.getUnapredDefinisaniPregled().getDodatniLekari().set(i, l);
			i += 1;
		}
		
		//tip pregleda je vec dobavljen u pessimistic read rezimu
		upitZaPregledToChange.setTipPregleda(pregled.getTipPregleda());
		
		//sala je vec dobavljena u pessimistic write rezimu
		upitZaPregledToChange.setSala(pregled.getSala());
		upit.setSala(pregled.getSala()); //upit za custom pregled nije imao postavljenu salu, postavi je sad

		//dobavi pacijenta u PESSIMISTIC_READ rezimu
		Pacijent pacijent = pacijentRepository.findByIdPacijentaPessimisticRead(upitZaPregledToChange.getPacijent().getId());
		upitZaPregledToChange.setPacijent(pacijent);

		upitZaPregledToChange.setUnapredDefinisaniPregled(pregled);
		upit.setUnapredDefinisaniPregled(pregled);
		
		upit.setOdobren(true);
		upit.setAdminObradio(true);
		
		if(upitZaPregledToChange.differsFrom(upit)){
			//UPIT ZA PREGLED JE IZMENJEN, PRAVI NOVI UPIT ZA PREGLED
			upitZaPregledToChange.setId(null);
			upitZaPregledToChange.setVersion(null);
			upitZaPregledToChange.setKlinika(klinika);
			upitZaPregledToChange.setOriginalniPregled(upit);
			upitZaPregledToChange = upitZaPregledRepository.save(upitZaPregledToChange);
			upit.setIzmenjeniPregled(upitZaPregledToChange);
			upit = upitZaPregledRepository.save(upit);
			pregled.getUpiti().add(upitZaPregledToChange);
			pregled.getUpiti().add(upit);
			pregledRepository.save(pregled);
		}else{
			upit = upitZaPregledRepository.save(upit);
			pregled.getUpiti().add(upit);
			pregledRepository.save(pregled);
		}

		UpitZaPregled toSend = upit;
		boolean izmenjen = false;
		if(upit.getIzmenjeniPregled() != null){
			toSend = upit.getIzmenjeniPregled();
			izmenjen = true;
		}
		mailService.upitOdobren(toSend, izmenjen); //obavesti pacijenta da je upit odobren
		mailService.obavestiLekara(toSend, pregled.getLekar(), true, izmenjen); //obavesti lekara da je upit odobren
		for(Lekar l : pregled.getDodatniLekari()){ //obavesti sve dodatne lekare da je upit odobren
			mailService.obavestiLekara(toSend, l, false, izmenjen);
		}
		
		return new CustomResponse<>(upit, true, "OK");
	}

	@Transactional(readOnly=false, isolation = Isolation.READ_COMMITTED)
	public CustomResponse<UpitZaPregled> odbiCustom(Long idKlinike, Long idUpita) throws EntityNotFoundException, BusinessLogicException{
		UpitZaPregled upit = upitZaPregledRepository.findByIdKlinikeAndById(idKlinike, idUpita);
		if(upit == null)
			throw new EntityNotFoundException("Upit za pregled");
		//prava odbrana od stetnog preplitanja transakcija je u liniji 509, ovo samo ako admin nije refreshovao dugo svoju stranicu
		if(upit.getAdminObradio())
			throw new BusinessLogicException("Ovaj upit za pregled je već obrađen od strane administratora klinike");
		upit.setAdminObradio(true);
		upit.setOdobren(false);
		upit = upitZaPregledRepository.save(upit);
		mailService.upitOdbijen(upit); //obavesti pacijenta na mail da je upit odbijen
		return new CustomResponse<>(upit, true, "OK");
	}

	public void izbrisiPosetu(Poseta poseta) {
		Pregled p = pregledRepository.findById(poseta.getPregled().getId()).get();
		for (UpitZaPregled u : p.getUpiti()) {
			UpitZaPregled pravi = upitZaPregledRepository.findById(u.getId()).get();
			if (pravi.getPotvrdjen()) {
				p.getUpiti().remove(u);
				pravi.setOriginalniPregled(null);
				pravi.setIzmenjeniPregled(null);
				pregledRepository.save(p);
				upitZaPregledRepository.delete(pravi);
				break;
			}
		}
	}
}