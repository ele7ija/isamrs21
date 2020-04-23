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
import isamrs.tim21.klinika.domain.Sala;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.SalaRepository;

@RestController
@RequestMapping(path="/sala/{idKlinike}")
public class SalaKontroler {
	@Autowired
	private SalaRepository salaRepository;
	
	@Autowired
	private KlinikaRepository klinikaRepository;
	
	@GetMapping
	public ResponseEntity<List<Sala>> getAllSale(@PathVariable("idKlinike") Long idKlinike){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<List<Sala>>(HttpStatus.NOT_FOUND);
		}else{
			List<Sala> retval = salaRepository.findAllByIdKlinike(klinika.getId());
			return new ResponseEntity<List<Sala>>(retval, HttpStatus.OK);
		}
	}

	@GetMapping(value="/{idSale}")
	public ResponseEntity<Sala> getSala(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idSale") Long idSale){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<Sala>(HttpStatus.NOT_FOUND);
		}else{
			Sala retval = salaRepository.findByIdKlinikeAndIdSale(idKlinike, idSale);
			if(retval == null){
				return new ResponseEntity<Sala>(HttpStatus.NOT_FOUND);
			}else{
				return new ResponseEntity<Sala>(retval, HttpStatus.OK);
			}
			
		}
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<Sala> addSala(@PathVariable("idKlinike") Long idKlinike, @RequestBody Sala salaToAdd){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<Sala>(HttpStatus.NOT_FOUND);
		}else{
			//POSTAVI JOS JEDNOM SVE PARAMETRE NA BEKU
			salaToAdd.setKlinika(klinika);
			salaToAdd.setId(null);
			
			Sala retval = salaRepository.save(salaToAdd);
			return new ResponseEntity<Sala>(retval, HttpStatus.OK);
		}
	}
	
	@PutMapping(value="/{idSale}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<Sala> updateSala(@PathVariable("idKlinike") Long idKlinike, 
			@PathVariable("idSale") Long idSale, @RequestBody Sala salaToChange){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<Sala>(HttpStatus.NOT_FOUND);
		}else{
			//POSTAVI JOS JEDNOM SVE PARAMETRE NA BEKU
			salaToChange.setId(idSale);
			salaToChange.setKlinika(klinika);
			
			if(! salaRepository.findById(idSale).isPresent()){
				return new ResponseEntity<Sala>(HttpStatus.NOT_FOUND);
			}
			Sala retval = salaRepository.save(salaToChange);
			return new ResponseEntity<Sala>(retval, HttpStatus.OK);	
			
		}
	}
	
	@DeleteMapping(value="/{idSale}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<Boolean> deleteSala(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idSale") Long idSale){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND);
		}else{
			salaRepository.deleteById(idSale);
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
	}
}
