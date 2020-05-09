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
	public ResponseEntity<CustomResponse<UpitZaPregled>> kreirajUpitZaPregled(@RequestBody UpitZaPregledDTO udto) {
		try {
			CustomResponse<UpitZaPregled> u = upitZaPregledeService.kreirajUpitZaPregled(udto);
			return new ResponseEntity<CustomResponse<UpitZaPregled>>(u, HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<CustomResponse<UpitZaPregled>>(
				new CustomResponse<UpitZaPregled>(null, false, "Greška: Vaša verzija je zastarela. Osvežite stranicu."),
				HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/neodobreni/{email}")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<CustomResponse<List<UpitZaPregled>>> izlistajNeodobreneUpite(@PathVariable("email") String email) {
		try {
			CustomResponse<List<UpitZaPregled>> l = upitZaPregledeService.pronadjiNeodobrene(email);
			return new ResponseEntity<CustomResponse<List<UpitZaPregled>>>(l, HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<CustomResponse<List<UpitZaPregled>>>(
				new CustomResponse<List<UpitZaPregled>>(null, false, "Greška: Vaša verzija je zastarela. Osvežite stranicu."),
				HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/neodobreni")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<CustomResponse<List<UpitZaPregled>>> izlistajNeodobreneUpite() {
		try {
			CustomResponse<List<UpitZaPregled>> l = upitZaPregledeService.pronadjiNeodobrene();
			return new ResponseEntity<CustomResponse<List<UpitZaPregled>>>(l, HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<CustomResponse<List<UpitZaPregled>>>(
				new CustomResponse<List<UpitZaPregled>>(null, false, "Greška: Vaša verzija je zastarela. Osvežite stranicu."),
				HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/nepotvrdjeni/{email}")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<CustomResponse<List<UpitZaPregled>>> izlistajNepotvrdjeneUpite(@PathVariable("email") String email) {
		try {
			CustomResponse<List<UpitZaPregled>> l = upitZaPregledeService.pronadjiNepotvrdjene(email);
			return new ResponseEntity<CustomResponse<List<UpitZaPregled>>>(l, HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<CustomResponse<List<UpitZaPregled>>>(
				new CustomResponse<List<UpitZaPregled>>(null, false, "Greška: Vaša verzija je zastarela. Osvežite stranicu."),
				HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/nepotvrdjeni")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<CustomResponse<List<UpitZaPregled>>> izlistajNepotvrdjeneUpite() {
		try {
			CustomResponse<List<UpitZaPregled>> l = upitZaPregledeService.pronadjiNepotvrdjene();
			return new ResponseEntity<CustomResponse<List<UpitZaPregled>>>(l, HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<CustomResponse<List<UpitZaPregled>>>(
				new CustomResponse<List<UpitZaPregled>>(null, false, "Greška: Vaša verzija je zastarela. Osvežite stranicu."),
				HttpStatus.OK);
		}
	}
	
	@PutMapping(value="/potvrdi/{id}")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<CustomResponse<UpitZaPregled>> potvrdi(@PathVariable("id") Long id) {
		try {
			CustomResponse<UpitZaPregled> u = upitZaPregledeService.izmeniPotvrdi(id);
			return new ResponseEntity<CustomResponse<UpitZaPregled>>(u, HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<CustomResponse<UpitZaPregled>>(
				new CustomResponse<UpitZaPregled>(null, false, "Greška: Vaša verzija je zastarela. Osvežite stranicu."),
				HttpStatus.OK);
		}
	}
	
	@PutMapping(value="/odustani/{id}")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<CustomResponse<UpitZaPregled>> odustani(@PathVariable("id") Long id) {
		try {
			CustomResponse<UpitZaPregled> u = upitZaPregledeService.izmeniOdustani(id);
			return new ResponseEntity<CustomResponse<UpitZaPregled>>(u, HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<CustomResponse<UpitZaPregled>>(
				new CustomResponse<UpitZaPregled>(null, false, "Greška: Vaša verzija je zastarela. Osvežite stranicu."),
				HttpStatus.OK);
		}
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
