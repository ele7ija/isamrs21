package isamrs.tim21.klinika.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.AdministratorKlinike;
import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.Korisnik;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.security.TokenUtils;

@Service

public class KlinikaService {
	
	@Autowired
	private TokenUtils tokenUtils;
	
	@Autowired
	private KlinikaRepository klinikaRepository;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Transactional(readOnly=true)
	private List<Klinika> findAll() {
		return klinikaRepository.findAll();
	}

	@Transactional(readOnly=true)
	public Klinika findKlinikaUlogovanog(HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = this.tokenUtils.getUsernameFromToken(token);
		Korisnik user = (Korisnik) this.userDetailsService.loadUserByUsername(email);
		if(user instanceof AdministratorKlinike){
			AdministratorKlinike admin = (AdministratorKlinike) user;
			return klinikaRepository.findById(admin.getKlinikaAdmina().getId()).orElse(null);
		}
		return null;
	}
}
