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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim21.klinika.domain.AdministratorCentra;
import isamrs.tim21.klinika.dto.AdminCentraDTO;
import isamrs.tim21.klinika.dto.AdminCentraProfilDTO;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.services.AdminCentraService;

@RestController
@RequestMapping("/admin_centra")
public class AdminCentraController {
  
  @Autowired
  private AdminCentraService adminCentraService;

  @GetMapping
  @PreAuthorize("hasAuthority('admin-klinickog-centra')")
  public ResponseEntity<List<AdministratorCentra>> getAllAdminiCentra(){
    List<AdministratorCentra> retval = adminCentraService.findAllAdminiCentra();
    return new ResponseEntity<>(retval, HttpStatus.OK);
  }

  @PostMapping
  @PreAuthorize("hasAuthority('admin-klinickog-centra')")
  public ResponseEntity<AdministratorCentra> addAdminCentra(
  @RequestBody AdminCentraDTO noviAdminCentraDTO){
    AdministratorCentra retval = adminCentraService.saveAdminCentra(noviAdminCentraDTO);
    return new ResponseEntity<>(retval, HttpStatus.OK);
  }

  @PutMapping("/profil")
  public ResponseEntity<?> updateProfileAdminCentra(
    @RequestBody AdminCentraProfilDTO adminCentra){
      CustomResponse<AdministratorCentra> retval = null;
      try{
        retval = adminCentraService.updateProfileAdminCentra(adminCentra);
      }
      catch(Exception e){
        return new ResponseEntity<CustomResponse<AdministratorCentra>>(
          new CustomResponse<AdministratorCentra>(null, false, "Greska: Verzija podatka je zastarela. Osvezite stranicu"),
          HttpStatus.OK);
    }
    return new ResponseEntity<CustomResponse<AdministratorCentra>>(retval, HttpStatus.OK); 
  }

}