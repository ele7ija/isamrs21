package isamrs.tim21.klinika.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.Pregled;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.PregledRepository;
import isamrs.tim21.klinika.services.PregledService;

@RestController
@RequestMapping(path="/pregled/{idKlinike}")
public class PregledKontroler {
	
	@Autowired
	PregledService pregledService;
	
	@Autowired
	KlinikaRepository klinikaRepository;
	
	@GetMapping
	@PreAuthorize("hasAnyAuthority('pacijent', 'lekar', 'admin-klinike')")
	public ResponseEntity<List<Pregled>> getAll(@PathVariable("idKlinike") Long idKlinike){
		if(!klinikaRepository.findById(idKlinike).isPresent()){
			return new ResponseEntity<List<Pregled>>(HttpStatus.NOT_FOUND);
		}
		List<Pregled> pregledi = pregledService.findAll(idKlinike);
		return new ResponseEntity<List<Pregled>>(pregledi, HttpStatus.OK);
	}
	
	@GetMapping(value="/slobodni")
	@PreAuthorize("hasAnyAuthority('pacijent', 'lekar', 'admin-klinike')")
	public ResponseEntity<List<Pregled>> getSlobodni(@PathVariable("idKlinike") Long idKlinike){
		if(!klinikaRepository.findById(idKlinike).isPresent()){
			return new ResponseEntity<List<Pregled>>(HttpStatus.NOT_FOUND);
		}
		List<Pregled> pregledi = pregledService.findSlobodni(idKlinike);
		return new ResponseEntity<List<Pregled>>(pregledi, HttpStatus.OK);
	}
	
	@GetMapping(value="/{idPregleda}")
	@PreAuthorize("hasAnyAuthority('pacijent', 'lekar', 'admin-klinike')")
	public ResponseEntity<Pregled> get(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idPregleda") Long idPregleda){
		Pregled pregled = pregledService.find(idKlinike, idPregleda);
		return new ResponseEntity<Pregled>(pregled, pregled == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<Pregled>> add(@PathVariable("idKlinike") Long idKlinike, @RequestBody Pregled pregled){
		Klinika klinika = klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			CustomResponse<Pregled> customResponse = new CustomResponse<Pregled>(null, false,
					"Greska: Trazena klinika ne postoji");
			return new ResponseEntity<CustomResponse<Pregled>>(customResponse, HttpStatus.NOT_FOUND);
		}else{
			CustomResponse<Pregled> customResponse = pregledService.add(klinika, pregled);
			HttpStatus statusCode = customResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
			return new ResponseEntity<CustomResponse<Pregled>>(customResponse, statusCode);
		}
	}
	
	@PutMapping(value="/{idPregleda}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<Pregled>> update(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idPregleda") Long idPregleda,
			@RequestBody Pregled pregled){
		Klinika klinika = klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			CustomResponse<Pregled> customResponse = new CustomResponse<Pregled>(null, false,
					"Greska: Trazena klinika ne postoji");
			return new ResponseEntity<CustomResponse<Pregled>>(customResponse, HttpStatus.NOT_FOUND);
		}else{
			CustomResponse<Pregled> customResponse = pregledService.update(klinika, pregled, idPregleda);
			HttpStatus statusCode = customResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
			return new ResponseEntity<CustomResponse<Pregled>>(customResponse, statusCode);
		}
	}
	
	@DeleteMapping(value="/{idPregleda}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<Boolean>> delete(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idPregleda") Long idPregleda){
		CustomResponse<Boolean> customResponse = pregledService.delete(idKlinike, idPregleda);
		return new ResponseEntity<CustomResponse<Boolean>>(customResponse, HttpStatus.OK);
	}
}
