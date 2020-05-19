package isamrs.tim21.klinika.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.AdministratorKlinike;
import isamrs.tim21.klinika.domain.Authority;
import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.dto.AdminKlinikeDTO;
import isamrs.tim21.klinika.dto.AdminKlinikeProfilDTO;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.KorisniciRepository;

@Service
public class AdminKlinikeService {

	@Autowired
	public CustomUserDetailsService userDetailsService;
	
  @Autowired
  KlinikaRepository klinikaRepository;

	@Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AutentifikacijaService autentifikacijaServis;
	
	@Autowired
	public KorisniciRepository korisnikRepository;
	
	public CustomResponse<AdministratorKlinike> updateProfilAdminKlinike(AdminKlinikeProfilDTO admin) {
		AdministratorKlinike retval = (AdministratorKlinike) userDetailsService.findUserAndChangePassword(admin.getAdminKlinike().getId(), admin.getPoslednjaSifra(), admin.getAdminKlinike().getSifra());
		retval.setIme(admin.getAdminKlinike().getIme());
		retval.setPrezime(admin.getAdminKlinike().getPrezime());
		if(!admin.getPoslednjaSifra().equals(retval.getSifra())){
			retval.setPoslednjaPromenaSifre(new Date());
		}
		retval = korisnikRepository.save(retval);
		return new CustomResponse<AdministratorKlinike>(retval, true, "OK.");
	}

	@Transactional(readOnly=false)
  public AdministratorKlinike saveAdminKlinike(AdminKlinikeDTO adminKlinikeDTO){
    //na osnovu id klinike pronaci kliniku iz baze; dodati kliniku administratoru klinike
    Klinika klinika = klinikaRepository.findById(adminKlinikeDTO.klinikaId).orElse(null);
    AdministratorKlinike adminKlinike= new AdministratorKlinike(adminKlinikeDTO, klinika);
    adminKlinike.setSifra(passwordEncoder.encode(adminKlinikeDTO.getSifra()));
    //dodaj authority adminu klinike
    Authority auth = autentifikacijaServis.findByName("admin-klinike");
		if (auth == null) {
			throw new RuntimeException("Admin-klinike authority nije pronadjen");
    }
    adminKlinike.getAuthorities().add(auth);
    //save admina klinike u bazu
    adminKlinike = korisnikRepository.save(adminKlinike);
    return adminKlinike;
	}
	
	public List<AdministratorKlinike> findAllAdminiKlinike(){
		List<AdministratorKlinike> a = korisnikRepository.findAllAdminiKlinike();
		return a;
	}

}
