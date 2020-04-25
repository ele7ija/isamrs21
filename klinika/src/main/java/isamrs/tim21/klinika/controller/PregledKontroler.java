package isamrs.tim21.klinika.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.Pregled;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.PregledRepository;

@RestController
@RequestMapping(path="/pregledi")
public class PregledKontroler {
	@Autowired
	private PregledRepository pregledRepository;
	
	@Autowired
	private KlinikaRepository klinikaRepository;
	
	@GetMapping(value="/klinika/{idKlinike}")
	public ResponseEntity<List<Pregled>> getAllPregledi(@PathVariable("idKlinike") Long idKlinike){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<List<Pregled>>(HttpStatus.NOT_FOUND);
		}else{
			List<Pregled> retval = pregledRepository.findAllByIdKlinike(klinika.getId());
			return new ResponseEntity<List<Pregled>>(retval, HttpStatus.OK);
		}
	}
}
