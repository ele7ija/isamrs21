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
@RequestMapping(path="/api/zahtevi_za_registraciju")
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
    return new ResponseEntity<>(zahtevService.kreiraj(zahtev), HttpStatus.OK);
  }
  

  @PostMapping("/odbij")
  @PreAuthorize("hasAuthority('admin-klinickog-centra')")
  public ResponseEntity<CustomResponse<ZahtevZaRegistraciju>> odbijZahtev(
    @RequestBody ZahtevZaRegistracijuDTO zahtevZaRegistracijuDTO){
    ZahtevZaRegistraciju retval = zahtevService.delete(zahtevZaRegistracijuDTO);
    mailService.odbijZahtev(zahtevZaRegistracijuDTO);
    return new ResponseEntity<>(
      new CustomResponse<>(retval, true, "OK"),
      HttpStatus.OK);
  }

  @PostMapping("/prihvati")
  @PreAuthorize("hasAuthority('admin-klinickog-centra')")
  public ResponseEntity<CustomResponse<ZahtevZaRegistraciju>> prihvatiZahtev(
  @RequestBody ZahtevZaRegistracijuDTO zahtevZaRegistracijuDTO) throws Exception {
    ZahtevZaRegistraciju retval = zahtevService.update(zahtevZaRegistracijuDTO);
    mailService.prihvatiZahtev(zahtevZaRegistracijuDTO);
    return new ResponseEntity<>(
      new CustomResponse<>(retval, true, "OK"),
      HttpStatus.OK);
  }

  @GetMapping ("/registruj/{id}")
  public String registrujKorisnika(@PathVariable Long id) {
    return zahtevService.registrujKorisnika(id);
  }
}