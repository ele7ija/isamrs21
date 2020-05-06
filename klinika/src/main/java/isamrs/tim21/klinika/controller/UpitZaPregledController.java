package isamrs.tim21.klinika.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.dto.PosetaDTO;
import isamrs.tim21.klinika.dto.UpitZaPregledDTO;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.KorisniciRepository;
import isamrs.tim21.klinika.repository.PregledRepository;
import isamrs.tim21.klinika.repository.TipPregledaRepository;
import isamrs.tim21.klinika.repository.UpitZaPregledRepository;
import isamrs.tim21.klinika.services.PosetaService;
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
	
	@Autowired
	PregledRepository pregledRepository;
	
	@Autowired
	PosetaService posetaService;
		
	/** Pacijent ili lekar dodaje upit */
	@PostMapping
	@PreAuthorize("hasAuthority('pacijent') or hasAuthority('lekar')")
	public ResponseEntity<UpitZaPregled> kreirajPosetu(@RequestBody UpitZaPregledDTO u) {
		UpitZaPregled u2 = new UpitZaPregled(u);
		u2.setKlinika(klinikaRepository.findById(u.getKlinika()).get());
		u2.setTipPregleda(tipPregledaRepository.findById(u.getTipPregleda()).get());
		u2.setLekar((Lekar) korisniciRepository.findById(u.getLekar()).get());
		u2.setPacijent((Pacijent) korisniciRepository.findByEmail(u.getPacijent()));
		u2.setUnapredDefinisaniPregled(pregledRepository.findById(u.getPregled()).get());
		
		u2.setAdminObradio(false);
		u2.setPacijentObradio(false);
		u2.setOdobren(false);
		u2.setPotvrdjen(false);
		
		upitZaPregledRepository.save(u2);
		return new ResponseEntity<UpitZaPregled>(u2, HttpStatus.OK);
	}
	
	@GetMapping(value="/neodobreni/{email}")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<List<UpitZaPregled>> izlistajNeodobreneUpite(@PathVariable("email") String email) {
		List<UpitZaPregled> l = upitZaPregledRepository.findNeodobreniByEmail(email);
		return new ResponseEntity<List<UpitZaPregled>>(l, HttpStatus.OK);
	}
	
	@GetMapping(value="/neodobreni")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<List<UpitZaPregled>> izlistajNeodobreneUpite() {
		List<UpitZaPregled> l = upitZaPregledRepository.findNeodobreni();
		return new ResponseEntity<List<UpitZaPregled>>(l, HttpStatus.OK);
	}
	
	@GetMapping(value="/nepotvrdjeni/{email}")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<List<UpitZaPregled>> izlistajNepotvrdjeneUpite(@PathVariable("email") String email) {
		List<UpitZaPregled> l = upitZaPregledRepository.findNepotvrdjeniByEmail(email);
		return new ResponseEntity<List<UpitZaPregled>>(l, HttpStatus.OK);
	}
	
	@GetMapping(value="/nepotvrdjeni")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<List<UpitZaPregled>> izlistajNepotvrdjeneUpite() {
		List<UpitZaPregled> l = upitZaPregledRepository.findNepotvrdjeni();
		return new ResponseEntity<List<UpitZaPregled>>(l, HttpStatus.OK);
	}
	
	@PutMapping(value="/potvrdi/{id}")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<UpitZaPregled> potvrdi(@PathVariable("id") Long id) {
		UpitZaPregled u = upitZaPregledRepository.findById(id).get();
		u.setPotvrdjen(true);
		u.setPacijentObradio(true);
		upitZaPregledRepository.save(u);
		posetaService.kreirajNovuPosetu(u);
		return new ResponseEntity<UpitZaPregled>(u, HttpStatus.OK);
	}
	
	@PutMapping(value="/odustani/{id}")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<UpitZaPregled> odustani(@PathVariable("id") Long id) {
		UpitZaPregled u = upitZaPregledRepository.findById(id).get();
		u.setPotvrdjen(false);
		u.setPacijentObradio(true);
		upitZaPregledRepository.save(u);
		return new ResponseEntity<UpitZaPregled>(u, HttpStatus.OK);
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
	
	// Put mapping za odobravanje upita.
	@PutMapping(value="obradiAdmin/{idKlinike}/{idUpita}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<UpitZaPregled>> obradiAdmin(@PathVariable("idKlinike") Long idKlinike, 
			@PathVariable("idUpita") Long idUpita, @RequestBody UpitZaPregled upitZaPregledToChange){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<CustomResponse<UpitZaPregled>>(new CustomResponse<UpitZaPregled>(null, false, "Greska: Klinika nije pronadjena."), HttpStatus.NOT_FOUND);
		}else{
			upitZaPregledToChange.setId(idUpita);
			upitZaPregledToChange.setKlinika(klinika);
			return new ResponseEntity<CustomResponse<UpitZaPregled>>(upitZaPregledeService.obradiAdmin(upitZaPregledToChange), HttpStatus.OK);
		}
	}
	
	@DeleteMapping(value="/{idKlinike}/{idUpita}")
	public ResponseEntity<CustomResponse<Boolean>> delete(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idUpita") Long idUpita){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<CustomResponse<Boolean>>(new CustomResponse<Boolean>(false, false, "Greska. Klinika ne postoji"), HttpStatus.NOT_FOUND);
		}else{
			CustomResponse<Boolean> customResponse = upitZaPregledeService.delete(idUpita);
			return new ResponseEntity<CustomResponse<Boolean>>(customResponse, HttpStatus.OK);
		}
	}
}
