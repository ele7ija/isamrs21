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
import isamrs.tim21.klinika.domain.TipPregleda;
import isamrs.tim21.klinika.dto.CustomResponse;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import isamrs.tim21.klinika.services.TipPregledaService;

@RestController
@RequestMapping(path="/tipPregleda/{idKlinike}")
public class TipPregledaKontroler {
	
	@Autowired
	private TipPregledaService tipPregledaService;
	
	@GetMapping
	public ResponseEntity<List<TipPregleda>> getAllTipoviPregleda(@PathVariable("idKlinike") Long idKlinike){
		List<TipPregleda> tipoviPregleda = tipPregledaService.getAllTipoviPregleda(idKlinike);
		return new ResponseEntity<List<TipPregleda>>(tipoviPregleda, HttpStatus.OK);
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<TipPregleda> addTipPregleda(@PathVariable("idKlinike") Long idKlinike, @RequestBody TipPregleda tipPregledaToAdd){
		return tipPregledaService.add(idKlinike, tipPregledaToAdd);
	}
	
	@PutMapping(value="/{idTipaPregleda}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<TipPregleda>> updateTipPregleda(@PathVariable("idKlinike") Long idKlinike, 
			@PathVariable("idTipaPregleda") Long idTipaPregleda, @RequestBody TipPregleda tipPregledaToChange){
		ResponseEntity<CustomResponse<TipPregleda>> retval = tipPregledaService.update(idKlinike, idTipaPregleda, tipPregledaToChange);
		return retval;
	}
	
	@DeleteMapping(value="/{idTipaPregleda}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<Boolean>> deleteTipPregleda(@PathVariable("idKlinike") Long idKlinike,
			@PathVariable("idTipaPregleda") Long idTipaPregleda, @RequestParam(name="version") Long version){
		ResponseEntity<CustomResponse<Boolean>> retval = tipPregledaService.delete(idKlinike, idTipaPregleda, version);
		return retval;
	}
}
