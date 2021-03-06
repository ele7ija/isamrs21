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

import isamrs.tim21.klinika.domain.Korisnik;
import isamrs.tim21.klinika.repository.KorisniciRepository;

@RestController
@RequestMapping(path="/api/korisnici")
public class KorisnikKontroler {
	@Autowired
	KorisniciRepository korisnikRepository;
	
	@GetMapping
	@PreAuthorize("hasAuthority('admin-klinike') or hasAuthority('admin-klinickog-centra')")
	public ResponseEntity<List<Korisnik>> getAllKorisnici(){
		List<Korisnik> osoblje = korisnikRepository.findAll();
		return new ResponseEntity<List<Korisnik>>(osoblje, HttpStatus.OK);
	}

	@GetMapping(path="/profil")
	public ResponseEntity<Korisnik> getKorisnik(@RequestParam("email") String email){
		Korisnik korisnik = korisnikRepository.findByEmail(email);
		return new ResponseEntity<Korisnik>(korisnik, korisnik == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
	}
}
