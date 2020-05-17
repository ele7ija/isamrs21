package isamrs.tim21.klinika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.Sifarnik;
import isamrs.tim21.klinika.repository.SifarnikRepository;

@Service
public class SifarnikService {
  
  @Autowired
  private SifarnikRepository sifarnikRepository;

  public List<Sifarnik> findAll(){
    return sifarnikRepository.findAll();
  }

  public Sifarnik save(Sifarnik dijagnozaIliLek){
    dijagnozaIliLek.setId(null);
    sifarnikRepository.save(dijagnozaIliLek);
    return dijagnozaIliLek;

  }

}