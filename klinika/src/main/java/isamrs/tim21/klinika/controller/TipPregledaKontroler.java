package isamrs.tim21.klinika.controller;

import java.util.ArrayList;
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
import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.TipPregleda;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.repository.CenovnikRepository;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.TipPregledaRepository;
import isamrs.tim21.klinika.services.TipPregledaService;

@RestController
@RequestMapping(path="/tipPregleda/{idKlinike}")
public class TipPregledaKontroler {
	@Autowired
	private TipPregledaRepository tipoviPregledaRepository;
	
	@Autowired
	private CenovnikRepository cenovnikRepository;
	
	@Autowired
	private KlinikaRepository klinikaRepository;
	
	@Autowired
	private TipPregledaService tipPregledaService;
	
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
			ArrayList<Lekar> lekari = new ArrayList<Lekar>();
			tipPregledaToAdd.setLekari(lekari);
			Cenovnik cenovnik = cenovnikRepository.findById(tipPregledaToAdd.getCenovnik().getId()).orElse(null);
			if(cenovnik == null){
				return new ResponseEntity<TipPregleda>(HttpStatus.NOT_FOUND);
			}
			cenovnik.getTipoviPregleda().add(tipPregledaToAdd);
			tipPregledaToAdd.setCenovnik(cenovnik);
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
			
			TipPregleda tipPregleda = tipoviPregledaRepository.findById(idTipaPregleda).orElse(null);
			if(tipPregleda == null){
				return new ResponseEntity<TipPregleda>(HttpStatus.NOT_FOUND);
			}
			tipPregledaToChange.setLekari(tipPregleda.getLekari()); //ovo se ne menja kroz api call
			TipPregleda retval = tipoviPregledaRepository.save(tipPregledaToChange);
			return new ResponseEntity<TipPregleda>(retval, HttpStatus.OK);	
			
		}
	}
	
	@DeleteMapping(value="/{idTipaPregleda}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<Boolean>> deleteTipPregleda(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idTipaPregleda") Long idTipaPregleda){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<CustomResponse<Boolean>>(new CustomResponse<Boolean>(false, false, "Greska. Klinika ne postoji"), HttpStatus.NOT_FOUND);
		}else{
			CustomResponse<Boolean> customResponse = tipPregledaService.delete(idKlinike, idTipaPregleda);
			//HttpStatus status = customResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
			return new ResponseEntity<CustomResponse<Boolean>>(customResponse, HttpStatus.OK);
		}
	}
}
