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

import isamrs.tim21.klinika.domain.Cenovnik;
import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.TipPregleda;
import isamrs.tim21.klinika.repository.CenovnikRepository;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.TipPregledaRepository;


@RestController
@RequestMapping(path="/cenovnik/{idKlinike}")
public class CenovnikKontroler {
	@Autowired
	private CenovnikRepository cenovnikRepository;
	
	@Autowired
	private TipPregledaRepository tipPregledaRepository;
	
	@Autowired
	private KlinikaRepository klinikaRepository;
	
	@GetMapping
	public ResponseEntity<List<Cenovnik>> getAllCenovnici(@PathVariable("idKlinike") Long idKlinike){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<List<Cenovnik>>(HttpStatus.NOT_FOUND);
		}else{
			List<Cenovnik> retval = cenovnikRepository.findAllByIdKlinike(klinika.getId());
			return new ResponseEntity<List<Cenovnik>>(retval, HttpStatus.OK);
		}
	}

	@GetMapping(value="/{idCenovnika}")
	public ResponseEntity<Cenovnik> getCenovnik(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idCenovnika") Long idCenovnika){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<Cenovnik>(HttpStatus.NOT_FOUND);
		}else{
			Cenovnik retval = cenovnikRepository.findByIdKlinikeAndIdCenovnika(idKlinike, idCenovnika);
			if(retval == null){
				return new ResponseEntity<Cenovnik>(HttpStatus.NOT_FOUND);
			}else{
				return new ResponseEntity<Cenovnik>(retval, HttpStatus.OK);
			}
			
		}
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<Cenovnik> addCenovnik(@PathVariable("idKlinike") Long idKlinike, @RequestBody Cenovnik cenovnikToAdd){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<Cenovnik>(HttpStatus.NOT_FOUND);
		}else{
			//POSTAVI JOS JEDNOM SVE PARAMETRE NA BEKU
			cenovnikToAdd.setKlinika(klinika);
			cenovnikToAdd.setId(null);
			for(int i = 0; i < cenovnikToAdd.getTipoviPregleda().size(); i++){
				TipPregleda tp = tipPregledaRepository.findById(cenovnikToAdd.getTipoviPregleda().get(i).getId()).get();
				tp.setCenovnik(cenovnikToAdd);
				cenovnikToAdd.getTipoviPregleda().set(i, tp);
			}
			Cenovnik retval = cenovnikRepository.save(cenovnikToAdd);
			return new ResponseEntity<Cenovnik>(retval, HttpStatus.OK);
		}
	}
	
	@PutMapping(value="/{idCenovnika}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<Cenovnik> updateCenovnik(@PathVariable("idKlinike") Long idKlinike, 
			@PathVariable("idCenovnika") Long idCenovnika, @RequestBody Cenovnik cenovnikToChange){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<Cenovnik>(HttpStatus.NOT_FOUND);
		}else{
			//POSTAVI JOS JEDNOM SVE PARAMETRE NA BEKU
			cenovnikToChange.setId(idCenovnika);
			cenovnikToChange.setKlinika(klinika);
			for(int i = 0; i < cenovnikToChange.getTipoviPregleda().size(); i++){
				TipPregleda tp = tipPregledaRepository.findById(cenovnikToChange.getTipoviPregleda().get(i).getId()).get();
				tp.setCenovnik(cenovnikToChange);
				cenovnikToChange.getTipoviPregleda().set(i, tp);
			}
			if(! cenovnikRepository.findById(idCenovnika).isPresent()){
				return new ResponseEntity<Cenovnik>(HttpStatus.NOT_FOUND);
			}
			Cenovnik retval = cenovnikRepository.save(cenovnikToChange);
			return new ResponseEntity<Cenovnik>(retval, HttpStatus.OK);	
			
		}
	}
	
	@DeleteMapping(value="/{idCenovnika}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<Boolean> deleteCenovnik(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idCenovnika") Long idCenovnika){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND);
		}else{
			if(tipPregledaRepository.findByIdCenovnika(idCenovnika).size() != 0){
				return new ResponseEntity<Boolean>(HttpStatus.FORBIDDEN);
			}
			cenovnikRepository.deleteById(idCenovnika);
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
	}
}
