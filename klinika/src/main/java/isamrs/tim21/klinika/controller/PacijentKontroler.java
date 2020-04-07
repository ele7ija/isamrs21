package isamrs.tim21.klinika.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim21.klinika.domain.Pacijent;
import isamrs.tim21.klinika.repository.InMemoryPacijentRepository;

@RestController
@RequestMapping(path="/pacijenti")
public class PacijentKontroler {
	@Autowired
	InMemoryPacijentRepository pacijentRepository;
	
	@GetMapping
	public ResponseEntity<List<Pacijent>> getAllPacijenti(){
		List<Pacijent> retval = new ArrayList<Pacijent>(pacijentRepository.findAll());
		return new ResponseEntity<List<Pacijent>>(retval, HttpStatus.OK);
	}
}

