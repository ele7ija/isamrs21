package isamrs.tim21.klinika.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.AdministratorKlinike;
import isamrs.tim21.klinika.dto.AdminKlinikeProfilDTO;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.repository.KorisniciRepository;

@Service
public class AdminKlinikeService {

	@Autowired
	public CustomUserDetailsService userDetailsService;
	
	@Autowired
	public KorisniciRepository korisnikRepository;
	
	public CustomResponse<AdministratorKlinike> updateProfilAdminKlinike(AdminKlinikeProfilDTO admin) {
		AdministratorKlinike retval = (AdministratorKlinike) userDetailsService.findUserAndChangePassword(admin.getAdminKlinike().getId(), admin.getPoslednjaSifra(), admin.getAdminKlinike().getSifra());
		retval.setIme(admin.getAdminKlinike().getIme());
		retval.setPrezime(admin.getAdminKlinike().getPrezime());
		retval = korisnikRepository.save(admin.getAdminKlinike());
		return new CustomResponse<AdministratorKlinike>(retval, true, "OK.");
	}

}
