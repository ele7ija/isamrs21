package isamrs.tim21.klinika.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import isamrs.tim21.klinika.domain.Recept;
import isamrs.tim21.klinika.dto.ReceptDTO;
import isamrs.tim21.klinika.services.ReceptService;

@Controller
@RequestMapping("/api/recept")
public class ReceptController {
  
  @Autowired
  private ReceptService receptService;

  @GetMapping
  public ResponseEntity<List<Recept>> getAllRecepti(){
    List<Recept> recepti = receptService.findAllRecepti();
    return new ResponseEntity<>(recepti, HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<?> overiRecept(@RequestBody ReceptDTO recept){
    Recept r = receptService.overiRecept(recept);
    return new ResponseEntity<>(r, HttpStatus.OK);
  }

}