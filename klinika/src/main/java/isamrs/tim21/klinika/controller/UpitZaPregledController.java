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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import isamrs.tim21.klinika.domain.UpitZaPregled;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.dto.UpitZaPregledDTO;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.KorisniciRepository;
import isamrs.tim21.klinika.repository.PregledRepository;
import isamrs.tim21.klinika.repository.TipPregledaRepository;
import isamrs.tim21.klinika.repository.UpitZaPregledRepository;
import isamrs.tim21.klinika.services.PosetaService;
import isamrs.tim21.klinika.services.UpitZaPregledeService;

@RestController
@RequestMapping(path="/api/upit")
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
			System.out.println(e.getMessage());
			return new ResponseEntity<CustomResponse<UpitZaPregled>>(
				new CustomResponse<UpitZaPregled>(null, false, "Greška: Vaša verzija je zastarela. Osvežite stranicu."),
				HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/neodobreni/neodradjeni/{email}")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<CustomResponse<List<UpitZaPregled>>> izlistajNeodobreneNeodradjeneUpite(@PathVariable("email") String email) {
		try {
			CustomResponse<List<UpitZaPregled>> l = upitZaPregledeService.pronadjiNeodobreneNeodradjene(email);
			return new ResponseEntity<CustomResponse<List<UpitZaPregled>>>(l, HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<CustomResponse<List<UpitZaPregled>>>(
				new CustomResponse<List<UpitZaPregled>>(null, false, "Greška: Vaša verzija je zastarela. Osvežite stranicu."),
				HttpStatus.OK);
		}
	}

	@PutMapping(value="/obradiNeodobren/{id}")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<CustomResponse<UpitZaPregled>> obradiNeodobren(@PathVariable("id") Long id) {
		try {
			CustomResponse<UpitZaPregled> u = upitZaPregledeService.obradiNeodobren(id);
			return new ResponseEntity<CustomResponse<UpitZaPregled>>(u, HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<CustomResponse<UpitZaPregled>>(
				new CustomResponse<UpitZaPregled>(null, false, "Greška: Vaša verzija je zastarela. Osvežite stranicu."),
				HttpStatus.OK);
		}
	}

	@GetMapping(value="/neodobreni/odradjeni/{email}")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<CustomResponse<List<UpitZaPregled>>> izlistajNeodobreneOdradjeneUpite(@PathVariable("email") String email) {
		try {
			CustomResponse<List<UpitZaPregled>> l = upitZaPregledeService.pronadjiNeodobreneOdradjene(email);
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
	
	@PutMapping(value="/potvrdi/{id}/{verzija}")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<CustomResponse<UpitZaPregled>> potvrdi(@PathVariable("id") Long id, @PathVariable("verzija") Long verzija) {
		try {
			CustomResponse<UpitZaPregled> u = upitZaPregledeService.izmeniPotvrdi(id, verzija);
			return new ResponseEntity<CustomResponse<UpitZaPregled>>(u, HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<CustomResponse<UpitZaPregled>>(
				new CustomResponse<UpitZaPregled>(null, false, "Greška: Vaša verzija je zastarela. Osvežite stranicu."),
				HttpStatus.OK);
		}
	}
	
	@PutMapping(value="/odustani/{id}/{verzija}")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<CustomResponse<UpitZaPregled>> odustani(@PathVariable("id") Long id, @PathVariable("verzija") Long verzija) {
		try {
			CustomResponse<UpitZaPregled> u = upitZaPregledeService.izmeniOdustani(id, verzija);
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
		List<UpitZaPregled> upiti = upitZaPregledeService.getAll(idKlinike);
		if(upiti == null){
			return new ResponseEntity<List<UpitZaPregled>>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<List<UpitZaPregled>>(upiti, HttpStatus.OK);
		}
	}
	
	// Put mapping za odobravanje upita za unapred definisani pregled.
	@PutMapping(value="obradiAdmin/{idKlinike}/{idUpita}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<UpitZaPregled>> obradiAdmin(@PathVariable("idKlinike") Long idKlinike, 
			@PathVariable("idUpita") Long idUpita, @RequestBody UpitZaPregled upitZaPregledToChange) throws Exception{
		CustomResponse<UpitZaPregled> retval = null;
		if(upitZaPregledToChange.getOdobren())
			retval = upitZaPregledeService.odobriUnapredDefinisani(idKlinike, idUpita, upitZaPregledToChange);
		else
			retval = upitZaPregledeService.odbiUnapredDefinisani(idKlinike, idUpita, upitZaPregledToChange);
		return new ResponseEntity<CustomResponse<UpitZaPregled>>(retval, HttpStatus.OK);
		
	}
	
	// Put mapping za odobravanje upita za custom pregled.
	@PutMapping(value="obradiAdminCustom/{idKlinike}/{idUpita}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<UpitZaPregled>> obradiAdminCustom(@PathVariable("idKlinike") Long idKlinike, 
			@PathVariable("idUpita") Long idUpita, @RequestBody UpitZaPregled upitZaPregledToChange){
		CustomResponse<UpitZaPregled> retval = null;
		if(upitZaPregledToChange.getOdobren())
			retval = upitZaPregledeService.odobriCustom(idKlinike, idUpita, upitZaPregledToChange);
		else
			retval = upitZaPregledeService.odbiCustom(idKlinike, idUpita);
		return new ResponseEntity<CustomResponse<UpitZaPregled>>(retval, HttpStatus.OK);
		
	}
	
	@DeleteMapping(value="/{idKlinike}/{idUpita}")
	public ResponseEntity<CustomResponse<Boolean>> delete(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idUpita") Long idUpita,
			@RequestParam(name="version") Long version){
		CustomResponse<Boolean> retval = upitZaPregledeService.delete(idUpita, version);
		return new ResponseEntity<CustomResponse<Boolean>>(retval, HttpStatus.OK);
	}
}
