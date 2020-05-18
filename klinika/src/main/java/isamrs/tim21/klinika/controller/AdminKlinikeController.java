package isamrs.tim21.klinika.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim21.klinika.domain.AdministratorKlinike;
import isamrs.tim21.klinika.dto.AdminKlinikeDTO;
import isamrs.tim21.klinika.dto.AdminKlinikeProfilDTO;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.services.AdminKlinikeService;

@RestController
@RequestMapping(path="/admin_klinike")
public class AdminKlinikeController {
  @Autowired
  private AdminKlinikeService adminKlinikeService;
  
  @GetMapping
  @PreAuthorize("hasAuthority('admin-klinickog-centra')")
  public ResponseEntity<List<AdministratorKlinike>> getAllAdminiKlinike(){
    List<AdministratorKlinike> retval = adminKlinikeService.findAllAdminiKlinike();
    return new ResponseEntity<>(retval, HttpStatus.OK);
  }

  
  @PostMapping
  @PreAuthorize("hasAuthority('admin-klinickog-centra')")
  public ResponseEntity<AdministratorKlinike> addAdministratorKlinike(
    @RequestBody AdminKlinikeDTO noviAdminKlinikeDTO){
    AdministratorKlinike retval = adminKlinikeService.saveAdminKlinike(noviAdminKlinikeDTO);
    return new ResponseEntity<>(retval, HttpStatus.OK);
  }
  
  @PutMapping(value="/profil")
  public ResponseEntity<CustomResponse<AdministratorKlinike>> updateProfilAdminKlinike(
    @RequestBody AdminKlinikeProfilDTO sestra){
	CustomResponse<AdministratorKlinike> retval = null;
	try{
		retval = adminKlinikeService.updateProfilAdminKlinike(sestra);
	}catch(Exception e){
		return new ResponseEntity<CustomResponse<AdministratorKlinike>>(
				new CustomResponse<AdministratorKlinike>(null, false, "Greska: Verzija podatka je zastarela. Osvezite stranicu"),
				HttpStatus.OK);
	}
	return new ResponseEntity<CustomResponse<AdministratorKlinike>>(retval, HttpStatus.OK); 
  }

}