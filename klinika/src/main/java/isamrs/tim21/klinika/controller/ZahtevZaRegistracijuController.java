package isamrs.tim21.klinika.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim21.klinika.domain.ZahtevZaRegistraciju;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.dto.ZahtevZaRegistracijuDTO;
import isamrs.tim21.klinika.exceptions.SendMailException;
import isamrs.tim21.klinika.services.MailService;
import isamrs.tim21.klinika.services.ZahtevZaRegistracijuService;

@RestController
@RequestMapping(path="/zahtevi_za_registraciju")
public class ZahtevZaRegistracijuController {
  @Autowired
  private ZahtevZaRegistracijuService zahtevService;

  @Autowired
  private MailService mailService;

  @GetMapping
  public ResponseEntity<List<ZahtevZaRegistraciju>> getAllZahtevi(){
    List<ZahtevZaRegistraciju> retval = zahtevService.findAll();
    return new ResponseEntity<>(retval, HttpStatus.OK);
  }

  @PostMapping("/podnesi")
  public ResponseEntity<CustomResponse<ZahtevZaRegistraciju>> podnesi(
    @RequestBody ZahtevZaRegistraciju zahtev
  ) {
    return new ResponseEntity<CustomResponse<ZahtevZaRegistraciju>>(zahtevService.kreiraj(zahtev), HttpStatus.OK);
  }
  

  @PostMapping("/odbij")
  @PreAuthorize("hasAuthority('admin-klinickog-centra')")
  public ResponseEntity<ZahtevZaRegistraciju>  odbijZahtev(
    @RequestBody ZahtevZaRegistracijuDTO zahtevZaRegistracijuDTO){
    //prvo posalji mejl. nokon slanja mejla obrisi zahtev iz baze
    //slanje mejla
    try{
      mailService.odbijZahtev(zahtevZaRegistracijuDTO);
      //brisanje zahteva jedino ako je mejl uspesno poslat
      ZahtevZaRegistraciju retval = zahtevService.delete(zahtevZaRegistracijuDTO);
      return new ResponseEntity<>(retval, HttpStatus.OK);
    }
    catch(Exception e){
      throw new SendMailException("slanje mejla nije uspelo");
    }
  }

  @PostMapping("/prihvati")
  @PreAuthorize("hasAuthority('admin-klinickog-centra')")
  public ResponseEntity<ZahtevZaRegistraciju> prihvatiZahtev(
  @RequestBody ZahtevZaRegistracijuDTO zahtevZaRegistracijuDTO){
    try{
      mailService.prihvatiZahtev(zahtevZaRegistracijuDTO);
      //update zahteva jedino ako je mejl uspesno poslat
      ZahtevZaRegistraciju retval = zahtevService.update(zahtevZaRegistracijuDTO);
      return new ResponseEntity<>(retval, HttpStatus.OK);
    }
    catch(Exception e){
      throw new SendMailException("slanje mejla nije uspelo");
    }
  }

  // @GetMapping("/registruj/{id}")
  // public RedirectView registrujKorisnika(@PathVariable Long id) {
  //   zahtevService.registrujKorisnika(id); 
  //   RedirectView r = new RedirectView("http://localhost:8081");
  //   return r;
  // }

  @GetMapping ("/registruj/{id}")
  public String registrujKorisnika(@PathVariable Long id) {
    String s = zahtevService.registrujKorisnika(id);
    return s;
  }
}