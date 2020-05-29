package isamrs.tim21.klinika.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim21.klinika.domain.Poseta;
import isamrs.tim21.klinika.dto.PosetaDTO;
import isamrs.tim21.klinika.dto.PosetaDTO2;
import isamrs.tim21.klinika.dto.PosetaDTO3;
import isamrs.tim21.klinika.services.PosetaService;

@RestController
@RequestMapping(path="/posete")
public class PosetaKontroler {
	@Autowired
	PosetaService service;
	
	@Autowired
	HttpServletRequest request;
	
	/** Pacijent dodaje posetu */
	@PostMapping(path="/pacijent")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<Poseta> kreirajPosetu(@RequestBody PosetaDTO p) {
		return new ResponseEntity<Poseta>(service.kreirajNovuPosetu(p, request), HttpStatus.OK);
	}
	
	/** Neko ko nije pacijent dodaje posetu pri cemu mora da dostavi i email */
	@PostMapping
	@PreAuthorize("hasAuthority('admin-klinike') or hasAuthority('admin-klinickog-centra') or "
			+ "hasAuthority('lekar') or hasAuthority('medicinska-sestra')")
	public ResponseEntity<Poseta> kreirajPosetu(@RequestBody PosetaDTO2 p) {
		return new ResponseEntity<Poseta>(service.kreirajNovuPosetu(p), HttpStatus.OK);
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<List<Poseta>> izlistajSvePosete() {
		return new ResponseEntity<List<Poseta>>(service.nadjiSvePosete(request), HttpStatus.OK);
	}
	
	@GetMapping(value="/nerealizovane")
	@PreAuthorize("hasAuthority('pacijent')")
	public ResponseEntity<List<Poseta>> izlistajNerealizovanePosete() {
		return new ResponseEntity<List<Poseta>>(service.nadjiNerealizovanePosete(request), HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<?> updatePoseta(@RequestBody PosetaDTO3 posetaDTO) {
		
		List retval = service.updatePoseta(posetaDTO);

		return new ResponseEntity<>(retval, HttpStatus.OK);
	}
}
