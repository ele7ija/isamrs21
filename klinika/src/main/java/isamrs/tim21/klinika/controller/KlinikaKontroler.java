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
import isamrs.tim21.klinika.repository.KlinikaRepository;

@RestController
@RequestMapping(path="/klinika")
public class KlinikaKontroler {
	
	@Autowired
	private KlinikaRepository klinikaRepository;
	
	@GetMapping
	public ResponseEntity<List<Klinika>> getAllKlinike(){	
		List<Klinika> retval = klinikaRepository.findAll();
		return new ResponseEntity<List<Klinika>>(retval, HttpStatus.OK);
	}
	
	@GetMapping(value="/{idKlinike}")
	public ResponseEntity<Klinika> getklinika(@PathVariable("idKlinike") Long idKlinike){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<Klinika>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<Klinika>(klinika, HttpStatus.OK);
		}
	}
	
	@PostMapping
	@PreAuthorize("hasRole('admin_klinickog_centra')")
	public ResponseEntity<Klinika> addKlinika(@RequestBody Klinika klinikaToadd){
		klinikaToadd.setId(null);
		Klinika retval = klinikaRepository.save(klinikaToadd);
		return new ResponseEntity<Klinika>(retval, HttpStatus.OK);
	}
	
	@PutMapping(value="/{idKlinike}")
	@PreAuthorize("hasRole('admin_klinike')")
	public ResponseEntity<Klinika> updateTipPregleda(@PathVariable("idKlinike") Long idKlinike, @RequestBody Klinika klinikaToChange){
		klinikaToChange.setId(idKlinike);
		if(! klinikaRepository.findById(idKlinike).isPresent()){
			return new ResponseEntity<Klinika>(HttpStatus.NOT_FOUND);
		}
		Klinika retval = klinikaRepository.save(klinikaToChange);
		return new ResponseEntity<Klinika>(retval, HttpStatus.OK);
	}
	
	@DeleteMapping(value="/{idKlinike}")
	@PreAuthorize("hasRole('admin_klinike')")
	public ResponseEntity<Boolean> deleteKlinika(@PathVariable("idKlinike") Long idKlinike){
		klinikaRepository.deleteById(idKlinike);
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
}
