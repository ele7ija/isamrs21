package isamrs.tim21.klinika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.AdministratorCentra;
import isamrs.tim21.klinika.domain.Authority;
import isamrs.tim21.klinika.dto.AdminCentraDTO;
import isamrs.tim21.klinika.repository.KorisniciRepository;

@Service
public class AdminCentraService {

  @Autowired
	private KorisniciRepository korisnikRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
  @Autowired
  private AutentifikacijaService autentifikacijaServis;
	
	public List<AdministratorCentra> findAllAdminiCentra() {
		return korisnikRepository.findAllAdminiCentra();
	}

	public AdministratorCentra saveAdminCentra(AdminCentraDTO noviAdminCentraDTO) {
		AdministratorCentra admin = new AdministratorCentra(noviAdminCentraDTO);
		admin.setSifra(passwordEncoder.encode(noviAdminCentraDTO.getSifra()));
		Authority auth = autentifikacijaServis.findByName("admin-klinike");
		if (auth == null) {
			throw new RuntimeException("Admin-klinike authority nije pronadjen");
		}
		admin.getAuthorities().add(auth);
		admin = korisnikRepository.save(admin);
		return admin;
	}
  
}