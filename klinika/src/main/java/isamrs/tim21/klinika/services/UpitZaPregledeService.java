package isamrs.tim21.klinika.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public UpitZaPregled obradiAdmin(UpitZaPregled u) {
		UpitZaPregled upit = upitZaPregledRepository.findById(u.getId()).get();
		upit.setAdminObradio(true);
		upit.setOdobren(u.getOdobren());
		Pregled p = pregledService.find(upit.getKlinika().getId(), u.getUnapredDefinisaniPregled().getId());
		if(p == null){
			//U PITANJU JE BIO CUSTOM PREGLED
			p = pregledService.add(upit.getKlinika(), u.getUnapredDefinisaniPregled()).getResult();
			upit.setUnapredDefinisaniPregled(p);
		}
		return upitZaPregledRepository.save(upit);
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
}
