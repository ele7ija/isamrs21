package isamrs.tim21.klinika.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim21.klinika.domain.Ocena;
import isamrs.tim21.klinika.services.OcenaService;

@RestController
@RequestMapping(path="/ocena")
public class OcenaKontroler {
    @Autowired
    OcenaService ocenaService;

    @GetMapping(value="/klinika/{idKlinike}")
    public ResponseEntity<List<Ocena>> pronadjiOceneKlinike(@PathVariable("idKlinike") Long idKlinike) {
        List<Ocena> ocene = ocenaService.pronadjiOceneKlinike(idKlinike);
        return new ResponseEntity<List<Ocena>>(ocene, HttpStatus.OK);
    }

    @GetMapping(value="/lekar/{idLekara}")
    public ResponseEntity<List<Ocena>> pronadjiOceneLekara(@PathVariable("idLekara") Long idLekara) {
        List<Ocena> ocene = ocenaService.pronadjiOceneLekara(idLekara);
        return new ResponseEntity<List<Ocena>>(ocene, HttpStatus.OK);
    }

    @GetMapping(value="/pacijent/{emailPacijenta}")
    public ResponseEntity<List<Ocena>> pronadjiOcenePacijenta(@PathVariable("emailPacijenta") String emailPacijenta) {
        List<Ocena> ocene = ocenaService.pronadjiOcenePacijenta(emailPacijenta);
        return new ResponseEntity<List<Ocena>>(ocene, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Ocena> kreirajOcenu(@RequestBody Ocena ocena) {
        Ocena o = ocenaService.kreirajOcenu(ocena);
        return new ResponseEntity<Ocena>(o, HttpStatus.OK);
    }
}