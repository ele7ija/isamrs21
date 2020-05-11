package isamrs.tim21.klinika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.ZahtevZaRegistraciju;
import isamrs.tim21.klinika.repository.ZahtevZaRegistracijuRepository;

@Service
public class ZahtevZaRegistracijuService {
  @Autowired
  ZahtevZaRegistracijuRepository zahtevRepo;
  
  public List<ZahtevZaRegistraciju> findAll() {
    return zahtevRepo.findAll();
  }
}