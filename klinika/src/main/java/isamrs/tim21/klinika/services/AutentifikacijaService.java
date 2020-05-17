package isamrs.tim21.klinika.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.Authority;
import isamrs.tim21.klinika.repository.AuthorityRepository;

@Service
public class AutentifikacijaService {

	@Autowired
	private AuthorityRepository authRepository;

	public Authority findByName(String name) {
		return authRepository.findByName(name);
	}
	
	
}
