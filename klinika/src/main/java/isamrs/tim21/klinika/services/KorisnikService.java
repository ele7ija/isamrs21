package isamrs.tim21.klinika.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.AdministratorKlinike;
import isamrs.tim21.klinika.domain.Authority;
import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.dto.AdminKlinikeDTO;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.KorisniciRepository;

@Service
public class KorisnikService {
  @Autowired
  KorisniciRepository korisnikRepository;

  @Autowired
  KlinikaRepository klinikaRepository;

	@Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AutentifikacijaService autentifikacijaServis;
  
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
}
