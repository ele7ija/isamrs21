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

import isamrs.tim21.klinika.domain.Pregled;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.repository.KlinikaRepository;
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
		List<Pregled> pregledi = pregledService.getAll(idKlinike);
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
		Pregled pregled = pregledService.get(idKlinike, idPregleda);
		return new ResponseEntity<Pregled>(pregled, pregled == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<Pregled>> add(@PathVariable("idKlinike") Long idKlinike, @RequestBody Pregled pregled){
		return pregledService.add(idKlinike, pregled);
	}
	
	@DeleteMapping(value="/{idPregleda}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<Boolean>> delete(@PathVariable("idKlinike") Long idKlinike,
			@PathVariable("idPregleda") Long idPregleda, @RequestParam(name="version") Long version) throws Exception{
		ResponseEntity<CustomResponse<Boolean>> retval = pregledService .delete(idKlinike, idPregleda, version);
		return retval;
	}
}
