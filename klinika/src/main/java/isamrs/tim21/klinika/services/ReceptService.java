package isamrs.tim21.klinika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.Poseta;
import isamrs.tim21.klinika.domain.Recept;
import isamrs.tim21.klinika.dto.ReceptDTO;
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

public List<Recept> findAllRecepti() {
  List<Recept> recepti =  receptRepository.findAll();
	return recepti;
}

public Recept overiRecept(ReceptDTO receptDTO) {
  Recept recept = receptRepository.findById(receptDTO.getId()).orElse(null);
  if(null == recept) 
    return recept;
  recept.setOveren(receptDTO.getOveren());
  recept.setMedicinskaSestra(receptDTO.getMedicinskaSestra());
  return receptRepository.save(recept);
}  
}