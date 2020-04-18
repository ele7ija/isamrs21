package isamrs.tim21.klinika.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim21.klinika.domain.Korisnik;
import isamrs.tim21.klinika.domain.Pacijent;
import isamrs.tim21.klinika.dto.KorisnikTokenState;
import isamrs.tim21.klinika.dto.PacijentDTO;
import isamrs.tim21.klinika.repository.PacijentRepository;
import isamrs.tim21.klinika.security.TokenUtils;
import isamrs.tim21.klinika.security.auth.JwtAuthenticationRequest;
import isamrs.tim21.klinika.services.CustomUserDetailsService;
import isamrs.tim21.klinika.services.PacijentService;

@RestController
@RequestMapping(path="/auth")
public class AutentifikacijaController {
	@Autowired
	private PacijentService pacijentService;
	
	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	// Prvi endpoint koji pogadja korisnik kada se loguje.
	// Tada zna samo svoje korisnicko ime i lozinku i to prosledjuje na backend.
	@PostMapping("/login")
	public ResponseEntity<KorisnikTokenState> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest,
			HttpServletResponse response) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
						authenticationRequest.getPassword()));

		// Ubaci korisnika u trenutni security kontekst
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Kreiraj token za tog korisnika
		Korisnik user = (Korisnik) authentication.getPrincipal();
		String jwt = tokenUtils.generateToken(user.getUsername());
		int expiresIn = tokenUtils.getExpiredIn();

		// Vrati token kao odgovor na uspesnu autentifikaciju
		return ResponseEntity.ok(new KorisnikTokenState(jwt, expiresIn));
	}
		
	@PostMapping(path = "/registracija", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registracija(@RequestBody PacijentDTO noviPacijentDTO) {
		Pacijent p = pacijentService.save(noviPacijentDTO);
		return new ResponseEntity<Pacijent>(p, HttpStatus.OK);
	}
	
	// U slucaju isteka vazenja JWT tokena, endpoint koji se poziva da se token osvezi
		@PostMapping(value = "/refresh")
		public ResponseEntity<KorisnikTokenState> refreshAuthenticationToken(HttpServletRequest request) {

			String token = tokenUtils.getToken(request);
			String username = this.tokenUtils.getUsernameFromToken(token);
			Korisnik user = (Korisnik) this.userDetailsService.loadUserByUsername(username);

			if (this.tokenUtils.canTokenBeRefreshed(token, user.getPoslednjaPromenaSifre())) {
				String refreshedToken = tokenUtils.refreshToken(token);
				int expiresIn = tokenUtils.getExpiredIn();

				return ResponseEntity.ok(new KorisnikTokenState(refreshedToken, expiresIn));
			} else {
				KorisnikTokenState userTokenState = new KorisnikTokenState();
				return ResponseEntity.badRequest().body(userTokenState);
			}
		}
}
