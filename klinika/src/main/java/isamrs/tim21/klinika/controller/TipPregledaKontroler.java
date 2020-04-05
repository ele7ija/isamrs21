package isamrs.tim21.klinika.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import isamrs.tim21.klinika.repository.InMemoryKlinikaRepository;
import isamrs.tim21.klinika.repository.InMemoryTipoviPregledaRepository;

@RestController
@RequestMapping(path="/{idKlinike}")
public class TipPregledaKontroler {
	@Autowired
	private InMemoryTipoviPregledaRepository tipoviPregledaRepository;
	
	@Autowired
	private InMemoryKlinikaRepository klinikaRepository;
	
	/*
	 * TODO 1: Napraviti AROUND aspekt koji ce presresti svaku od ovih metoda i proveriti da li data klinika postoji u bazi podataka
	 * U slucaju da postoji, nastaviti sa pozivom REST endpointa
	 * U slucaju da ne postoji, vratiti 404
	 * Bitno -> pitanje je da li je ovo dobro jer cemo onda imati 2 ista upita u bazu za dobavljanje klinike u slucaju da ona postoji
	 * (jednom u aspektu i drugi put u REST endpoint-u)
	 */
	
	/*
	 * TODO 2: 
	 * Razmisliti kako dizajnirati JPA repository-e. Da li imati po jedan repository za klinike i tipove pregleda, 
	 * ili samo jedan repository sa SQL JOIN operacijama koje cemo morati sami da pisemo kao Stringove
	 * (jer JPA nece vrv znati automatski da ih napravi)
	 */
	
	@GetMapping
	public ResponseEntity<List<TipPregleda>> getAllTipoviPregleda(@PathVariable("idKlinike") Long idKlinike){
		Klinika klinika =  klinikaRepository.findById(idKlinike); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<List<TipPregleda>>(HttpStatus.NOT_FOUND);
		}else{
			List<TipPregleda> retval = tipoviPregledaRepository.findByIdKlinike(idKlinike);
			return new ResponseEntity<List<TipPregleda>>(retval, HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/{idTipaPregleda}")
	public ResponseEntity<TipPregleda> getTipPregleda(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idTipaPregleda") Long idTipaPregleda){
		Klinika klinika =  klinikaRepository.findById(idKlinike); //ovo ce verovatno ici u aspekt
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
	public ResponseEntity<TipPregleda> addTipPregleda(@PathVariable("idKlinike") Long idKlinike, @RequestBody TipPregleda tipPregledaToAdd){
		Klinika klinika =  klinikaRepository.findById(idKlinike); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<TipPregleda>(HttpStatus.NOT_FOUND);
		}else{
			tipPregledaToAdd.setIdKlinike(idKlinike);
			tipoviPregledaRepository.addTipPregleda(tipPregledaToAdd);
			return new ResponseEntity<TipPregleda>(tipPregledaToAdd, HttpStatus.OK);
		}
	}
	
	@PutMapping(value="/{idTipaPregleda}")
	public ResponseEntity<TipPregleda> updateTipPregleda(@PathVariable("idKlinike") Long idKlinike, 
			@PathVariable("idTipaPregleda") Long idTipaPregleda, @RequestBody TipPregleda tipPregledaToChange){
		Klinika klinika =  klinikaRepository.findById(idKlinike); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<TipPregleda>(HttpStatus.NOT_FOUND);
		}else{
			TipPregleda retval = tipoviPregledaRepository.updateTipPregleda(idTipaPregleda, tipPregledaToChange);
			if(retval == null){
				return new ResponseEntity<TipPregleda>(retval, HttpStatus.NOT_FOUND);
			}else{
				return new ResponseEntity<TipPregleda>(retval, HttpStatus.OK);	
			}
			
		}
	}
	
	@DeleteMapping(value="/{idTipaPregleda}")
	public ResponseEntity<Boolean> deleteTipPRegleda(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idTipaPregleda") Long idTipaPregleda){
		Klinika klinika =  klinikaRepository.findById(idKlinike); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND);
		}else{
			Boolean success = tipoviPregledaRepository.deleteTipPregleda(idTipaPregleda);
			return new ResponseEntity<Boolean>(success, HttpStatus.OK);
		}
	}
}
