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
import isamrs.tim21.klinika.domain.TipPregleda;
import isamrs.tim21.klinika.repository.CenovnikRepository;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.TipPregledaRepository;

@RestController
@RequestMapping(path="/tipPregleda/{idKlinike}")
public class TipPregledaKontroler {
	@Autowired
	private TipPregledaRepository tipoviPregledaRepository;
	
	@Autowired
	private CenovnikRepository cenovnikRepository;
	
	@Autowired
	private KlinikaRepository klinikaRepository;
	
	@GetMapping
	public ResponseEntity<List<TipPregleda>> getAllTipoviPregleda(@PathVariable("idKlinike") Long idKlinike){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<List<TipPregleda>>(HttpStatus.NOT_FOUND);
		}else{
			List<TipPregleda> retval = tipoviPregledaRepository.findAllByIdKlinike(klinika.getId());
			return new ResponseEntity<List<TipPregleda>>(retval, HttpStatus.OK);
		}
	}

	@GetMapping(value="/{idTipaPregleda}")
	public ResponseEntity<TipPregleda> getTipPregleda(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idTipaPregleda") Long idTipaPregleda){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<TipPregleda>(HttpStatus.NOT_FOUND);
		}else{
			TipPregleda retval = tipoviPregledaRepository.findByIdKlinikeAndIdTipaPregleda(idKlinike, idTipaPregleda);
			if(retval == null){
				return new ResponseEntity<TipPregleda>(HttpStatus.NOT_FOUND);
			}else{
				return new ResponseEntity<TipPregleda>(retval, HttpStatus.OK);
			}
			
		}
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<TipPregleda> addTipPregleda(@PathVariable("idKlinike") Long idKlinike, @RequestBody TipPregleda tipPregledaToAdd){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<TipPregleda>(HttpStatus.NOT_FOUND);
		}else{
			//POSTAVI JOS JEDNOM SVE PARAMETRE NA BEKU
			tipPregledaToAdd.setKlinika(klinika);
			tipPregledaToAdd.setId(null);
			TipPregleda retval = tipoviPregledaRepository.save(tipPregledaToAdd);
			return new ResponseEntity<TipPregleda>(retval, HttpStatus.OK);
		}
	}
	
	@PutMapping(value="/{idTipaPregleda}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<TipPregleda> updateTipPregleda(@PathVariable("idKlinike") Long idKlinike, 
			@PathVariable("idTipaPregleda") Long idTipaPregleda, @RequestBody TipPregleda tipPregledaToChange){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<TipPregleda>(HttpStatus.NOT_FOUND);
		}else{
			//POSTAVI JOS JEDNOM SVE PARAMETRE NA BEKU
			tipPregledaToChange.setId(idTipaPregleda);
			tipPregledaToChange.setKlinika(klinika);
			
			if(! tipoviPregledaRepository.findById(idTipaPregleda).isPresent()){
				return new ResponseEntity<TipPregleda>(HttpStatus.NOT_FOUND);
			}

			TipPregleda retval = tipoviPregledaRepository.save(tipPregledaToChange);
			return new ResponseEntity<TipPregleda>(retval, HttpStatus.OK);	
			
		}
	}
	
	@DeleteMapping(value="/{idTipaPregleda}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<Boolean> deleteTipPregleda(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idTipaPregleda") Long idTipaPregleda){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND);
		}else{
			tipoviPregledaRepository.deleteById(idTipaPregleda);
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
	}
}
