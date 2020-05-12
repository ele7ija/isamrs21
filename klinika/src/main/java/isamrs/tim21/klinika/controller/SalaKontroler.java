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

import isamrs.tim21.klinika.domain.Sala;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.services.SalaService;

@RestController
@RequestMapping(path="/sala/{idKlinike}")
public class SalaKontroler {
	
	@Autowired
	private SalaService salaService;
	
	@GetMapping
	public ResponseEntity<List<Sala>> getAllSale(@PathVariable("idKlinike") Long idKlinike){
		List<Sala> sale = salaService.getAllSale(idKlinike);
		if(sale == null){
			return new ResponseEntity<List<Sala>>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<List<Sala>>(sale, HttpStatus.OK);
		}
	}

	@GetMapping(value="/{idSale}")
	public ResponseEntity<Sala> getSala(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idSale") Long idSale){
		Sala sala = salaService.getSala(idKlinike, idSale);
		if(sala == null){
			return new ResponseEntity<Sala>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<Sala>(sala, HttpStatus.OK);
		}
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<Sala> addSala(@PathVariable("idKlinike") Long idKlinike, @RequestBody Sala salaToAdd){
		return salaService.addSala(idKlinike, salaToAdd);
	}
	
	@PutMapping(value="/{idSale}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<Sala>> updateSala(@PathVariable("idKlinike") Long idKlinike, 
			@PathVariable("idSale") Long idSale, @RequestBody Sala salaToChange){
		ResponseEntity<CustomResponse<Sala>> retval = null;
		try{
			retval = salaService.update(idKlinike, idSale, salaToChange);
		}catch(Exception e){
			return new ResponseEntity<CustomResponse<Sala>>(
					new CustomResponse<Sala>(null, false, "Greska: Vasa verzija je zastarela. Osvezite stranicu"),
					HttpStatus.OK);
		}
		return retval;
	}
	
	@DeleteMapping(value="/{idSale}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<Boolean>> deleteSala(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idSale") Long idSale,
			@RequestParam(name="version") Long version){
		ResponseEntity<CustomResponse<Boolean>> retval = null;
		try{
			retval = salaService.deleteMain(idKlinike, idSale, version);
		}catch(Exception e){
			return new ResponseEntity<CustomResponse<Boolean>>(
					new CustomResponse<Boolean>(true, false, "Greska: Vasa verzija je zastarela. Osvezite stranicu"),
					HttpStatus.OK);
		}
		return retval;
	}
}
