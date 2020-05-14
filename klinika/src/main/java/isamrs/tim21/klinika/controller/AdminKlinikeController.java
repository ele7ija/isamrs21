package isamrs.tim21.klinika.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim21.klinika.domain.AdministratorKlinike;
import isamrs.tim21.klinika.dto.AdminKlinikeDTO;
import isamrs.tim21.klinika.services.KorisnikService;

@RestController
@RequestMapping(path="/admin_klinike")
public class AdminKlinikeController {
  @Autowired
  private KorisnikService korisnikService;

  @PostMapping
  @PreAuthorize("hasAuthority('admin-klinickog-centra')")
  public ResponseEntity<AdministratorKlinike> addAdministratorKlinike(
    @RequestBody AdminKlinikeDTO noviAdminKlinikeDTO){
    AdministratorKlinike retval = korisnikService.saveAdminKlinike(noviAdminKlinikeDTO);
    return new ResponseEntity<>(retval, HttpStatus.OK);
  }

}