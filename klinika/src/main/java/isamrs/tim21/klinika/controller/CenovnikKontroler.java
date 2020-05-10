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

import isamrs.tim21.klinika.domain.Cenovnik;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.services.CenovnikService;


@RestController
@RequestMapping(path="/cenovnik/{idKlinike}")
public class CenovnikKontroler {
	
	@Autowired
	private CenovnikService cenovnikService;
	
	@GetMapping
	public ResponseEntity<List<Cenovnik>> getAllCenovnici(@PathVariable("idKlinike") Long idKlinike){
		List<Cenovnik> cenovnici = cenovnikService.findAllByIdKlinike(idKlinike);
		if(cenovnici == null){
			return new ResponseEntity<List<Cenovnik>>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<List<Cenovnik>>(cenovnici, HttpStatus.OK);
		}
	}

	@GetMapping(value="/{idCenovnika}")
	public ResponseEntity<Cenovnik> getCenovnik(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idCenovnika") Long idCenovnika){
		Cenovnik cenovnik = cenovnikService.findByIdKlinike(idKlinike, idCenovnika);
		if(cenovnik == null){
			return new ResponseEntity<Cenovnik>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<Cenovnik>(cenovnik, HttpStatus.OK);
		}
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<Cenovnik> addCenovnik(@PathVariable("idKlinike") Long idKlinike, @RequestBody Cenovnik cenovnikToAdd){
		Cenovnik cenovnik = cenovnikService.add(idKlinike, cenovnikToAdd);
		if(cenovnik == null){
			return new ResponseEntity<Cenovnik>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<Cenovnik>(cenovnik, HttpStatus.OK);
		}
	}
	
	@PutMapping(value="/{idCenovnika}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<Cenovnik>> updateCenovnik(@PathVariable("idKlinike") Long idKlinike, 
			@PathVariable("idCenovnika") Long idCenovnika, @RequestBody Cenovnik cenovnikToChange) throws Exception{
		ResponseEntity<CustomResponse<Cenovnik>> retval = null;
		try{
			retval = cenovnikService.update(idKlinike, idCenovnika, cenovnikToChange);
		}catch(Exception e){
			retval = new ResponseEntity<CustomResponse<Cenovnik>>(new CustomResponse<Cenovnik>(
					null, false, "Greska usled optimistickog zakljucavanja. Pokusajte ponovo."), HttpStatus.OK);
		}
		return retval;
	}
	
	@DeleteMapping(value="/{idCenovnika}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<Boolean>> deleteCenovnik(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idCenovnika") Long idCenovnika,
			@RequestParam(name="version") Long version){
		return cenovnikService.delete(idKlinike, idCenovnika, version);
	}
}
