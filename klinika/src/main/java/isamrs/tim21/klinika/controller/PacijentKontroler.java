package isamrs.tim21.klinika.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim21.klinika.domain.Pacijent;
import isamrs.tim21.klinika.services.PacijentService;

@RestController
@RequestMapping(path="/pacijenti")
public class PacijentKontroler {
	
	@Autowired
	private PacijentService pacijentService;
	
	@GetMapping(path = "/all")
	public ResponseEntity<List<Pacijent>> getAllPacijenti(){
		List<Pacijent> retval = pacijentService.findAll();
		return new ResponseEntity<List<Pacijent>>(retval, HttpStatus.OK);
	}
	
	@GetMapping(path = "/alle")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<List<Pacijent>> getAllPacijentiE(){
		List<Pacijent> retval = pacijentService.findAll();
		return new ResponseEntity<List<Pacijent>>(retval, HttpStatus.OK);
	}
	
	@GetMapping(path="/pravoPristupa")
	public ResponseEntity<Boolean> pravoPristupa(@RequestParam("idPacijenta") Long idPacijenta, @RequestParam("idLekara") Long idLekara){
		Boolean retval = pacijentService.pravoPristupa(idPacijenta, idLekara);
		return new ResponseEntity<Boolean>(retval, HttpStatus.OK);
	}
	
}

