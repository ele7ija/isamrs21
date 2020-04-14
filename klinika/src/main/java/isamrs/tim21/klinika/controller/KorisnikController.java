package isamrs.tim21.klinika.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim21.klinika.domain.Pacijent;
import isamrs.tim21.klinika.dto.PacijentDTO;
import isamrs.tim21.klinika.repository.PacijentRepository;

@RestController
@RequestMapping(path="/korisnici")
public class KorisnikController {
	@Autowired
	private PacijentRepository pacijentRepository;
	
	@PostMapping(path = "/registracija", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registracija(@RequestBody PacijentDTO noviPacijentDTO) {
		Pacijent pacijent = new Pacijent(noviPacijentDTO);
		pacijentRepository.save(pacijent);
		return new ResponseEntity<Pacijent>(pacijent, HttpStatus.OK);
	}
}
