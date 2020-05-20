package isamrs.tim21.klinika.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.AdministratorCentra;
import isamrs.tim21.klinika.domain.Authority;
import isamrs.tim21.klinika.dto.AdminCentraDTO;
import isamrs.tim21.klinika.dto.AdminCentraProfilDTO;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.repository.KorisniciRepository;

@Service
public class AdminCentraService {

  @Autowired
	private KorisniciRepository korisnikRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
  @Autowired
	private AutentifikacijaService autentifikacijaServis;
	
	@Autowired
	public CustomUserDetailsService userDetailsService;
	
	public List<AdministratorCentra> findAllAdminiCentra() {
		return korisnikRepository.findAllAdminiCentra();
	}

	public AdministratorCentra saveAdminCentra(AdminCentraDTO noviAdminCentraDTO) {
		AdministratorCentra admin = new AdministratorCentra(noviAdminCentraDTO);
		admin.setSifra(passwordEncoder.encode(noviAdminCentraDTO.getSifra()));
		Authority auth = autentifikacijaServis.findByName("admin-klinickog-centra");
		if (auth == null) {
			throw new RuntimeException("Admin-klinickog-centra authority nije pronadjen");
		}
		admin.getAuthorities().add(auth);
		admin = korisnikRepository.save(admin);
		return admin;
	}

	public CustomResponse<AdministratorCentra> updateProfileAdminCentra(AdminCentraProfilDTO admin) {
		AdministratorCentra retval = (AdministratorCentra) userDetailsService.findUserAndChangePassword(
			admin.getAdmin().getId(), admin.getPoslednjaSifra(), admin.getAdmin().getSifra());
		
		retval.setIme(admin.getAdmin().getIme());
		retval.setPrezime(admin.getAdmin().getPrezime());
		if(!admin.getPoslednjaSifra().equals(retval.getSifra())) {
			retval.setPoslednjaPromenaSifre(new Date());
		}
		retval = korisnikRepository.save(retval);
		return new CustomResponse<AdministratorCentra>(retval, true, "OK");
	}
  
}