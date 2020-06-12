package isamrs.tim21.klinika.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim21.klinika.domain.Sifarnik;
import isamrs.tim21.klinika.services.SifarnikService;

@RestController
@RequestMapping("/api/sifarnik")
public class SifarnikController {
  
  @Autowired
  private SifarnikService sifarnikService;

  @GetMapping
  public ResponseEntity<List<Sifarnik>> getAllDijagnozeLekovi(){
    List<Sifarnik> retval= sifarnikService.findAll();
    return new ResponseEntity<>(retval, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Sifarnik> addDijagnozaIliLek(@RequestBody Sifarnik dijagnozaIliLek) {
    Sifarnik retval = sifarnikService.save(dijagnozaIliLek);
    return new ResponseEntity<>(retval, HttpStatus.OK);
  }
}