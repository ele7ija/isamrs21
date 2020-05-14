package isamrs.tim21.klinika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.ZahtevZaRegistraciju;
import isamrs.tim21.klinika.dto.ZahtevZaRegistracijuDTO;
import isamrs.tim21.klinika.repository.ZahtevZaRegistracijuRepository;

@Service
public class ZahtevZaRegistracijuService {
  @Autowired
  ZahtevZaRegistracijuRepository zahtevRepo;
  
  public List<ZahtevZaRegistraciju> findAll() {
    return zahtevRepo.findAll();
  }

  public ZahtevZaRegistraciju delete(ZahtevZaRegistracijuDTO zahtevZaRegistracijuDTO) {
    ZahtevZaRegistraciju zahtevZaRegistraciju = 
      zahtevRepo.findById(zahtevZaRegistracijuDTO.getId()).orElse(null);
    zahtevRepo.delete(zahtevZaRegistraciju);
    return zahtevZaRegistraciju;
  }

  public ZahtevZaRegistraciju update(ZahtevZaRegistracijuDTO zahtevZaRegistracijuDTO) {
    ZahtevZaRegistraciju zahtevZaRegistraciju = 
    zahtevRepo.findById(zahtevZaRegistracijuDTO.getId()).orElse(null);    
    zahtevZaRegistraciju.setDatumOdobrenja(zahtevZaRegistracijuDTO.getDatumOdobrenja());
    zahtevZaRegistraciju.setOdobren(zahtevZaRegistracijuDTO.getOdobren());
    zahtevRepo.save(zahtevZaRegistraciju);
    return zahtevZaRegistraciju;
  }
  public String registrujKorisnika(Long id){
    ZahtevZaRegistraciju zahtevZaRegistraciju = zahtevRepo.findById(id).orElse(null);
    //if null bice greska
    if (null == zahtevZaRegistraciju){
      return "problem ";
    }
    //else enabluj korisnika i obrisi zahtev
    zahtevZaRegistraciju.getPacijent().setEnabled(true);
    zahtevRepo.deleteById(id);
    return "uspesno ste registrovani";
  }
}