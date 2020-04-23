package isamrs.tim21.klinika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.Authority;
import isamrs.tim21.klinika.domain.Pacijent;
import isamrs.tim21.klinika.dto.PacijentDTO;
import isamrs.tim21.klinika.repository.PacijentRepository;

@Service
public class PacijentService {
	@Autowired
	private PacijentRepository pacijentRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AutentifikacijaService autentifikacijaServis;
	
	public Pacijent findByEmail(String email) throws UsernameNotFoundException {
		Pacijent p = pacijentRepository.findByEmail(email);
		return p;
	}

	public Pacijent save(PacijentDTO pacijentDTO) {
		Pacijent p = new Pacijent(pacijentDTO);
		// pre nego sto postavimo lozinku u atribut hesiramo je
		p.setSifra(passwordEncoder.encode(pacijentDTO.getSifra()));
		p.setEnabled(true);
		
		Authority auth = autentifikacijaServis.findByName("pacijent");
		// u primeru se registruju samo obicni korisnici i u skladu sa tim im se i dodeljuje samo rola USER
		p.getAuthorities().add(auth);
		
		p = pacijentRepository.save(p);
		return p;
	}
	
}
