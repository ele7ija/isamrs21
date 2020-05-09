package isamrs.tim21.klinika.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.Pacijent;
import isamrs.tim21.klinika.domain.Pregled;
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

	@Autowired PosetaService posetaService;

	@Transactional
	public CustomResponse<UpitZaPregled> obradiAdmin(UpitZaPregled u) {
		UpitZaPregled upit = upitZaPregledRepository.findById(u.getId()).get();
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
					return new CustomResponse<UpitZaPregled>(upit, false, "Obavestenje: Ovaj pregled je vec rezervisan, te je iz tog razloga upit ipak odbijen.");
				}
				//ukoliko pregled vec ima odobren upit, admin nije smeo da odobri u
				for(UpitZaPregled drugiUpit: p.getUpiti()){
					if(drugiUpit.getId() == upit.getId())
						continue;
					if(drugiUpit.getOdobren() && drugiUpit.getPotvrdjen()){
						upit.setOdobren(false);
						upit = upitZaPregledRepository.save(upit);
						return new CustomResponse<UpitZaPregled>(upit, false, "Obavestenje: Ovaj pregled je vec rezervisan, te je iz tog razloga upit ipak odbijen.");
					}
					if(drugiUpit.getOdobren() && !drugiUpit.getPacijentObradio()){
						upit.setAdminObradio(false);
						upit.setOdobren(false);
						upit = upitZaPregledRepository.save(upit);
						return new CustomResponse<UpitZaPregled>(null, false, "Obavestenje: Ovaj pregled je vec odobren. Mocicete da odobrite ovaj pregled samo u slucaju da pacijent kojem je ovaj pregled odobren ipak odluci da ne potvrdi rezervaciju.");
					}
				}
			}
			
		}
		upit = upitZaPregledRepository.save(upit);
		return new CustomResponse<UpitZaPregled>(upit, true, "OK.");
	}

	@Transactional
	public CustomResponse<Boolean> delete(Long idUpita) {
		UpitZaPregled upit = upitZaPregledRepository.findById(idUpita).get();
		CustomResponse<Boolean> retval;
		if(upit == null){
			retval = new CustomResponse<Boolean>(false, false, "Greska. Trazeni upit ne postoji");
		}
		else if(!upit.getPacijentObradio()){
			retval = new CustomResponse<Boolean>(false, false, "Greska. Pacijent jos uvek nije video odgovor na vas upit.");
		}else{
			upitZaPregledRepository.deleteById(upit.getId());
			retval = new CustomResponse<Boolean>(true, true, "OK.");
		}
		return retval;
	}

	@Transactional(readOnly=false)
	public CustomResponse<UpitZaPregled> kreirajUpitZaPregled(UpitZaPregledDTO u)
		throws Exception {
		UpitZaPregled u2 = new UpitZaPregled(u);
		try {
			u2.setKlinika(klinikaRepository.findById(u.getKlinika()).get());
		}
		catch(NoSuchElementException e) {
			return new CustomResponse<UpitZaPregled>(null, false, "Klinika ne postoji.");
		}
		try {
			u2.setTipPregleda(tipPregledaRepository.findById(u.getTipPregleda()).get());
		}
		catch(NoSuchElementException e) {
			return new CustomResponse<UpitZaPregled>(null, false, "Tip pregleda ne postoji.");
		}		
		try {
			u2.setLekar((Lekar) korisniciRepository.findById(u.getLekar()).get());		}
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
			u2.setUnapredDefinisaniPregled(pregledRepository.findById(u.getPregled()).get());
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
	public CustomResponse<List<UpitZaPregled>> pronadjiNeodobrene(String email)
		throws Exception {
		List<UpitZaPregled> l = upitZaPregledRepository.findNeodobreniByEmail(email);
		return new CustomResponse<List<UpitZaPregled>>(l, true, "Uspešno pronađeni upiti.");
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
	public CustomResponse<UpitZaPregled> izmeniPotvrdi(Long id) throws Exception {
		try {
			UpitZaPregled u = upitZaPregledRepository.findById(id).get();
			u.setPotvrdjen(true);
			u.setPacijentObradio(true);
			upitZaPregledRepository.save(u);
			return new CustomResponse<UpitZaPregled>(u, true, "Upit je potvrđen.");
		}
		catch (NoSuchElementException e){
			return new CustomResponse<UpitZaPregled>(null, false, "Upit ne postoji.");
		}
	}

	@Transactional(readOnly=false)
	public CustomResponse<UpitZaPregled> izmeniOdustani(Long id) throws Exception {
		try {
			UpitZaPregled u = upitZaPregledRepository.findById(id).get();
			u.setPotvrdjen(false);
			u.setPacijentObradio(true);
			upitZaPregledRepository.save(u);
			return new CustomResponse<UpitZaPregled>(u, true, "Upit je potvrđen.");
		}
		catch (NoSuchElementException e){
			return new CustomResponse<UpitZaPregled>(null, false, "Upit ne postoji.");
		}
	}
}
