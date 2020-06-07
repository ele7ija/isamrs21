package isamrs.tim21.klinika.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.Poseta;
import isamrs.tim21.klinika.domain.Recept;
import isamrs.tim21.klinika.repository.ReceptRepository;

@Service
public class ReceptService {

  @Autowired
  ReceptRepository receptRepository;

  public Recept saveRecept(Poseta poseta){
    Recept recept = new Recept();
    recept.setPoseta(poseta);
    return receptRepository.save(recept);
  }
  
}