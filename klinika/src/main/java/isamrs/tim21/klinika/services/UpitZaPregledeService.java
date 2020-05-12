package isamrs.tim21.klinika.services;

import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.Pacijent;
import isamrs.tim21.klinika.domain.Pregled;
import isamrs.tim21.klinika.domain.TipPregleda;
import isamrs.tim21.klinika.domain.UpitZaPregled;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.dto.UpitZaPregledDTO;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.KorisniciRepository;
import isamrs.tim21.klinika.repository.PregledRepository;
import isamrs.tim21.klinika.repository.TipPregledaRepository;
import isamrs.tim21.klinika.repository.UpitZaPregledRepository;

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
	PregledRepository pregledRepository;
	
	@Autowired
	PregledService pregledService;
	
	@Autowired
	PosetaService posetaService;

	@Transactional
	public ResponseEntity<CustomResponse<UpitZaPregled>> obradiAdmin(UpitZaPregled u) throws Exception{
		UpitZaPregled upit = upitZaPregledRepository.findById(u.getId()).get();
		if(upit == null){
			return new ResponseEntity<CustomResponse<UpitZaPregled>>(
					new CustomResponse<UpitZaPregled>(null, false, "Greska: Trazeni upit nije pronadjen."),
					HttpStatus.NOT_FOUND
			);
		}
		if(upit.getVersion() != u.getVersion()){
			throw new Exception();
		}
		upit.setAdminObradio(true);
		upit.setOdobren(u.getOdobren());
		Pregled p = pregledService.get(upit.getKlinika().getId(), u.getUnapredDefinisaniPregled().getId());
		if(p == null){
			//U PITANJU JE BIO CUSTOM PREGLED
			p = pregledService.add(upit.getKlinika(), u.getUnapredDefinisaniPregled()).getResult();
			upit.setUnapredDefinisaniPregled(p);
		}else{
			//validacije radis samo ako je admin odobrio ovaj upit
			if(upit.getOdobren()){
				//ukoliko pregled ima posetu, admin nije smeo da odobri upit u
				if(p.getPoseta() != null){
					upit.setOdobren(false);
					upit = upitZaPregledRepository.save(upit);
					return new ResponseEntity<CustomResponse<UpitZaPregled>>(
							new CustomResponse<UpitZaPregled>(upit, false, "Obavestenje: Ovaj pregled je vec rezervisan, te je iz tog razloga upit ipak odbijen."),
							HttpStatus.OK
					);
				}
				//ukoliko pregled vec ima odobren upit, admin nije smeo da odobri u
				for(UpitZaPregled drugiUpit: p.getUpiti()){
					if(drugiUpit.getId() == upit.getId())
						continue;
					if(drugiUpit.getOdobren() && drugiUpit.getPotvrdjen()){
						upit.setOdobren(false);
						upit = upitZaPregledRepository.save(upit);
						return new ResponseEntity<CustomResponse<UpitZaPregled>>(
								new CustomResponse<UpitZaPregled>(upit, false, "Obavestenje: Ovaj pregled je vec rezervisan, te je iz tog razloga upit ipak odbijen."),
								HttpStatus.OK
						);
					}
					if(drugiUpit.getOdobren() && !drugiUpit.getPacijentObradio()){
						upit.setAdminObradio(false);
						upit.setOdobren(false);
						upit = upitZaPregledRepository.save(upit);
						return new ResponseEntity<CustomResponse<UpitZaPregled>>(
								new CustomResponse<UpitZaPregled>(upit, false, "Obavestenje: Ovaj pregled je vec odobren. Mocicete da odobrite ovaj pregled samo u slucaju da pacijent kojem je ovaj pregled odobren ipak odluci da ne potvrdi rezervaciju."),
								HttpStatus.OK
						);
					}
				}
			}
			
		}
		upit = upitZaPregledRepository.save(upit);
		return new ResponseEntity<CustomResponse<UpitZaPregled>>(
				new CustomResponse<UpitZaPregled>(upit, true, "OK."),
				HttpStatus.OK
		);
	}

	@Transactional(readOnly=false)
	public CustomResponse<Boolean> delete(Long idUpita, Long version) throws Exception{
		UpitZaPregled upit = upitZaPregledRepository.findById(idUpita).get();
		CustomResponse<Boolean> retval;
		if(upit == null){
			retval = new CustomResponse<Boolean>(false, false, "Greska. Trazeni upit ne postoji");
		}
		/*else if(upit.getVersion() != version){
			retval = new CustomResponse<Boolean>(false, false, "Greska. Verzija podatka je zastarela. Osvezite stranicu.");
		}*/
		else if(!upit.getPacijentObradio()){
			retval = new CustomResponse<Boolean>(true, false, "Greska. Pacijent jos uvek nije video odgovor na vas upit.");
		}else{
			UpitZaPregled upToDelete = new UpitZaPregled();
			upToDelete.setId(idUpita);
			upToDelete.setVersion(version);
			upitZaPregledRepository.delete(upToDelete); //ovde moze da dodje do nepoklapanja verzija
			retval = new CustomResponse<Boolean>(true, true, "OK.");
		}
		return retval;
	}

	public ResponseEntity<CustomResponse<Boolean>> deleteMain(Long idKlinike, Long idUpita, Long version) throws Exception{
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return new ResponseEntity<CustomResponse<Boolean>>(new CustomResponse<Boolean>(false, false, "Greska. Klinika ne postoji"), HttpStatus.NOT_FOUND);
		}else{
			CustomResponse<Boolean> customResponse = delete(idUpita, version);
			return new ResponseEntity<CustomResponse<Boolean>>(customResponse, customResponse.getResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
		}
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

	@Transactional(readOnly=false)
	public ResponseEntity<CustomResponse<UpitZaPregled>> obradiAdminMain(Long idKlinike, Long idUpita,
			UpitZaPregled upitZaPregledToChange) throws Exception{
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return new ResponseEntity<CustomResponse<UpitZaPregled>>(
					new CustomResponse<UpitZaPregled>(null, false, "Greska: Klinika nije pronadjena."),
					HttpStatus.NOT_FOUND
			);
		}else{
			upitZaPregledToChange.setId(idUpita);
			upitZaPregledToChange.setKlinika(klinika);
			ResponseEntity<CustomResponse<UpitZaPregled>> retval = obradiAdmin(upitZaPregledToChange);
			return retval;
		}
	}

	@Transactional(readOnly=false)
	public CustomResponse<UpitZaPregled> kreirajUpitZaPregled(UpitZaPregledDTO u)
		throws Exception {
		UpitZaPregled u2 = new UpitZaPregled(u);
		try {
			u2.setKlinika(klinikaRepository.findById(u.getKlinika()).get());
			// TODO provera vezije
		}
		catch(NoSuchElementException e) {
			return new CustomResponse<UpitZaPregled>(null, false, "Klinika ne postoji.");
		}
		try {
			u2.setTipPregleda(tipPregledaRepository.findById(u.getTipPregleda()).get());
			if (u2.getTipPregleda().getVersion() != u.getTipPregledaVerzija()) {
				throw new ObjectOptimisticLockingFailureException(TipPregleda.class, u2.getTipPregleda());
			}
		}
		catch(NoSuchElementException e) {
			return new CustomResponse<UpitZaPregled>(null, false, "Tip pregleda ne postoji.");
		}		
		try {
			u2.setLekar((Lekar) korisniciRepository.findById(u.getLekar()).get());
			if (u2.getLekar().getVersion() != u.getLekarVerzija()) {
				throw new ObjectOptimisticLockingFailureException(Lekar.class, u2.getLekar());
			}		
		}
		catch(NoSuchElementException e) {
			return new CustomResponse<UpitZaPregled>(null, false, "Lekar ne postoji.");
		}
		try {
			u2.setPacijent((Pacijent) korisniciRepository.findByEmail(u.getPacijent()));
		}
		catch(NoSuchElementException e) {
			return new CustomResponse<UpitZaPregled>(null, false, "Pacijent ne postoji.");
		}
		try {
			// Upit je nastao na osnovu unapred def pregleda
			if (u.getPregled() != null) {
				u2.setUnapredDefinisaniPregled(pregledRepository.findById(u.getPregled()).get());
				if (u2.getUnapredDefinisaniPregled().getVersion() != u.getPregledVerzija()) {
					throw new ObjectOptimisticLockingFailureException(Pregled.class, u2.getUnapredDefinisaniPregled());
				}
			}
		}
		catch(NoSuchElementException e) {
			return new CustomResponse<UpitZaPregled>(null, false, "Pregled ne postoji.");
		}		
		u2.setAdminObradio(false);
		u2.setPacijentObradio(false);
		u2.setOdobren(false);
		u2.setPotvrdjen(false);
		upitZaPregledRepository.save(u2);
		return new CustomResponse<UpitZaPregled>(u2, true, "Uspešno kreiran upit");
	}

	@Transactional(readOnly=true)
	public CustomResponse<List<UpitZaPregled>> pronadjiNeodobreneNeodradjene(String email)
		throws Exception {
		List<UpitZaPregled> l = upitZaPregledRepository.findNeodobreniNeodradjeniByEmail(email);
		return new CustomResponse<List<UpitZaPregled>>(l, true, "Uspešno pronađeni upiti.");
	}

	@Transactional(readOnly=true)
	public CustomResponse<List<UpitZaPregled>> pronadjiNeodobreneOdradjene(String email)
		throws Exception {
		List<UpitZaPregled> l = upitZaPregledRepository.findNeodobreniOdradjeniByEmail(email);
		return new CustomResponse<List<UpitZaPregled>>(l, true, "Uspešno pronađeni upiti.");
	}

	@Transactional(readOnly=false)
	public CustomResponse<UpitZaPregled> obradiNeodobren(Long id) 
		throws Exception {
		try {
			UpitZaPregled u = upitZaPregledRepository.findById(id).get();
			u.setPacijentObradio(true);
			upitZaPregledRepository.save(u);
			return new CustomResponse<UpitZaPregled>(u, true, "Upit uspešno obrađen.");
		}
		catch (NoSuchElementException e) {
			return new CustomResponse<UpitZaPregled>(null, false, "Upit ne postoji.");
		}
	}

	@Transactional(readOnly=true)
	public CustomResponse<List<UpitZaPregled>> pronadjiNeodobrene()
		throws Exception {
		List<UpitZaPregled> l = upitZaPregledRepository.findNeodobreni();
		return new CustomResponse<List<UpitZaPregled>>(l, true, "Uspešno pronađeni upiti.");
	}

	@Transactional(readOnly=true)
	public CustomResponse<List<UpitZaPregled>> pronadjiNepotvrdjene(String email)
		throws Exception {
		List<UpitZaPregled> l = upitZaPregledRepository.findNepotvrdjeniByEmail(email);
		return new CustomResponse<List<UpitZaPregled>>(l, true, "Uspešno pronađeni upiti.");
	}

	@Transactional(readOnly=true)
	public CustomResponse<List<UpitZaPregled>> pronadjiNepotvrdjene()
		throws Exception {
		List<UpitZaPregled> l = upitZaPregledRepository.findNepotvrdjeni();
		return new CustomResponse<List<UpitZaPregled>>(l, true, "Uspešno pronađeni upiti.");
	}

	@Transactional(readOnly=false)
	public CustomResponse<UpitZaPregled> izmeniPotvrdi(Long id, Long verzija) throws Exception {
		try {
			UpitZaPregled u = upitZaPregledRepository.findById(id).get();
			if (u.getVersion() != verzija) {
				throw new ObjectOptimisticLockingFailureException(UpitZaPregled.class, u);
			}
			u.setPotvrdjen(true);
			u.setPacijentObradio(true);
			upitZaPregledRepository.save(u);
			posetaService.kreirajNovuPosetu(u);
			return new CustomResponse<UpitZaPregled>(u, true, "Upit je potvrđen.");
		}
		catch (NoSuchElementException e){
			return new CustomResponse<UpitZaPregled>(null, false, "Upit ne postoji.");
		}
	}

	@Transactional(readOnly=false)
	public CustomResponse<UpitZaPregled> izmeniOdustani(Long id, Long verzija) throws Exception {
		try {
			UpitZaPregled u = upitZaPregledRepository.findById(id).get();
			if (u.getVersion() != verzija) {
				throw new ObjectOptimisticLockingFailureException(UpitZaPregled.class, u);
			}
			u.setPotvrdjen(false);
			u.setPacijentObradio(true);
			upitZaPregledRepository.save(u);
			return new CustomResponse<UpitZaPregled>(u, true, "Upit je izbrisan.");
		}
		catch (NoSuchElementException e){
			return new CustomResponse<UpitZaPregled>(null, false, "Upit ne postoji.");
		}
	}
}