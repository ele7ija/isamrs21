package isamrs.tim21.klinika.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.Pregled;
import isamrs.tim21.klinika.domain.UpitZaPregled;
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
		if(pregledService.find(upit.getKlinika().getId(), u.getUnapredDefinisaniPregled().getId()) == null){
			//U PITANJU JE BIO CUSTOM PREGLED
			Pregled pregled = pregledService.add(upit.getKlinika(), u.getUnapredDefinisaniPregled()).getResult();
			upit.setUnapredDefinisaniPregled(pregled);
			pregled.getUpiti().add(upit);
		}
		return upitZaPregledRepository.save(upit);
	}
}
