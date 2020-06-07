package isamrs.tim21.klinika.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import isamrs.tim21.klinika.domain.Recept;
import isamrs.tim21.klinika.services.ReceptService;

@Controller
@RequestMapping("recept")
public class ReceptController {
  
  @Autowired
  private ReceptService receptService;

  @GetMapping
  public ResponseEntity<?> getAllRecepti(){
    List<Recept> recepti = receptService.findAllRecepti();
    return new ResponseEntity<>(recepti, HttpStatus.OK);
  }

}