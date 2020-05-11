package isamrs.tim21.klinika.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim21.klinika.domain.ZahtevZaRegistraciju;
import isamrs.tim21.klinika.services.ZahtevZaRegistracijuService;

@RestController
@RequestMapping(path="/zahtevi_za_registraciju")
public class ZahtevZaRegistracijuController {
  @Autowired
  ZahtevZaRegistracijuService zahtevService;

  @GetMapping
  public ResponseEntity<List<ZahtevZaRegistraciju>> getAllZahtevi(){
    List<ZahtevZaRegistraciju> retval = zahtevService.findAll();
    return new ResponseEntity<>(retval, HttpStatus.OK);
  }

}