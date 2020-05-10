package isamrs.tim21.klinika.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.Pregled;
import isamrs.tim21.klinika.domain.UpitZaPregled;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.KorisniciRepository;
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
	PregledService pregledService;
	
	@Autowired
	PosetaService posetaService;

	@Transactional(readOnly=false)
	public CustomResponse<UpitZaPregled> obradiAdmin(UpitZaPregled u) throws ObjectOptimisticLockingFailureException{
		UpitZaPregled upit = upitZaPregledRepository.findById(u.getId()).get();
		if(upit == null){
			return new CustomResponse<UpitZaPregled>(null, false, "Greska: Trazeni upit nije pronadjen.");
		}
		if(upit.getVersion() != u.getVersion()){
			throw new ObjectOptimisticLockingFailureException(UpitZaPregled.class, u);
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

	@Transactional(readOnly=false)
	public CustomResponse<Boolean> delete(Long idUpita, Long version) {
		UpitZaPregled upit = upitZaPregledRepository.findById(idUpita).get();
		CustomResponse<Boolean> retval;
		if(upit == null){
			retval = new CustomResponse<Boolean>(false, false, "Greska. Trazeni upit ne postoji");
		}
		else if(upit.getVersion() != version){
			retval = new CustomResponse<Boolean>(false, false, "Greska. Verzija podatka je zastarela. Osvezite stranicu.");
		}
		else if(!upit.getPacijentObradio()){
			retval = new CustomResponse<Boolean>(false, false, "Greska. Pacijent jos uvek nije video odgovor na vas upit.");
		}else{
			upitZaPregledRepository.deleteById(upit.getId());
			retval = new CustomResponse<Boolean>(true, true, "OK.");
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

	@Transactional(readOnly=false)
	public CustomResponse<UpitZaPregled> obradiAdminMain(Long idKlinike, Long idUpita,
			UpitZaPregled upitZaPregledToChange) {
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return new CustomResponse<UpitZaPregled>(null, false, "Greska: Klinika nije pronadjena.");
		}else{
			upitZaPregledToChange.setId(idUpita);
			upitZaPregledToChange.setKlinika(klinika);
			CustomResponse<UpitZaPregled> customResponse = null;
			try{
				customResponse = obradiAdmin(upitZaPregledToChange);
			}catch(ObjectOptimisticLockingFailureException e){
				return new CustomResponse<UpitZaPregled>(null, false, "Greska. Verzija upita je zastarela. Osvezite stranicu");
			}
			return customResponse;
		}
	}

	@Transactional(readOnly=false)
	public ResponseEntity<CustomResponse<Boolean>> deleteMain(Long idKlinike, Long idUpita, Long version) {
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<CustomResponse<Boolean>>(new CustomResponse<Boolean>(false, false, "Greska. Klinika ne postoji"), HttpStatus.NOT_FOUND);
		}else{
			CustomResponse<Boolean> customResponse = delete(idUpita, version);
			return new ResponseEntity<CustomResponse<Boolean>>(customResponse, HttpStatus.OK);
		}
	}

	
}
