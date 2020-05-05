package isamrs.tim21.klinika.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.Pacijent;
import isamrs.tim21.klinika.domain.Poseta;
import isamrs.tim21.klinika.domain.UpitZaPregled;
import isamrs.tim21.klinika.dto.PosetaDTO;
import isamrs.tim21.klinika.dto.UpitZaPregledDTO;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.KorisniciRepository;
import isamrs.tim21.klinika.repository.TipPregledaRepository;
import isamrs.tim21.klinika.repository.UpitZaPregledRepository;
import isamrs.tim21.klinika.services.UpitZaPregledeService;

@RestController
@RequestMapping(path="/upit")
public class UpitZaPregledController {
	@Autowired
	UpitZaPregledRepository upitZaPregledRepository;
	
	@Autowired
	UpitZaPregledeService upitZaPregledeService;
	
	@Autowired
	KlinikaRepository klinikaRepository;
	
	@Autowired
	KorisniciRepository korisniciRepository;
	
	@Autowired
	TipPregledaRepository tipPregledaRepository;
	
	
	
	/** Pacijent ili lekar dodaje upit */
	@PostMapping
	@PreAuthorize("hasAuthority('pacijent') or hasAuthority('lekar')")
	public ResponseEntity<UpitZaPregled> kreirajPosetu(@RequestBody UpitZaPregledDTO u) {
		UpitZaPregled u2 = new UpitZaPregled(u);
		u2.setKlinika(klinikaRepository.findById(u.getKlinika()).get());
		u2.setTipPregleda(tipPregledaRepository.findById(u.getTipPregleda()).get());
		u2.setLekar((Lekar) korisniciRepository.findById(u.getLekar()).get());
		u2.setPacijent((Pacijent) korisniciRepository.findByEmail(u.getPacijent()));
		u2.setAdminObradio(false);
		u2.setPacijentObradio(false);
		u2.setOdobren(false);
		u2.setPotvrdjen(false);
		upitZaPregledRepository.save(u2);
		return new ResponseEntity<UpitZaPregled>(u2, HttpStatus.OK);
	}
	
	@GetMapping(value="/neodobreni")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<List<UpitZaPregled>> izlistajNeodobreneUpite() {
		List<UpitZaPregled> l = upitZaPregledRepository.findNeodobreni();
		return new ResponseEntity<List<UpitZaPregled>>(l, HttpStatus.OK);
	}
	
	@GetMapping(value="/nepotvrdjeni")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<List<UpitZaPregled>> izlistajNepotvrdjeneUpite() {
		List<UpitZaPregled> l = upitZaPregledRepository.findNepotvrdjeni();
		return new ResponseEntity<List<UpitZaPregled>>(l, HttpStatus.OK);
	}
	
	@GetMapping(value="/{idKlinike}")
	public ResponseEntity<List<UpitZaPregled>> getAll(@PathVariable("idKlinike") Long idKlinike){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<List<UpitZaPregled>>(HttpStatus.NOT_FOUND);
		}else{
			List<UpitZaPregled> retval = upitZaPregledRepository.findAllByIdKlinike(klinika.getId());
			return new ResponseEntity<List<UpitZaPregled>>(retval, HttpStatus.OK);
		}
	}
	
	// Put mapping za odobravanje/potvrdu upita.
	@PutMapping(value="obradiAdmin/{idKlinike}/{idUpita}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<UpitZaPregled> obradiAdmin(@PathVariable("idKlinike") Long idKlinike, 
			@PathVariable("idUpita") Long idUpita, @RequestBody UpitZaPregled upitZaPregledToChange){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<UpitZaPregled>(HttpStatus.NOT_FOUND);
		}else{
			upitZaPregledToChange.setId(idUpita);
			upitZaPregledToChange.setKlinika(klinika);
			return new ResponseEntity<UpitZaPregled>(upitZaPregledeService.obradiAdmin(upitZaPregledToChange), HttpStatus.OK);
		}
	}
}
