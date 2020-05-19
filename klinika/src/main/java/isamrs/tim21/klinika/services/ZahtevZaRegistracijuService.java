package isamrs.tim21.klinika.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.Pacijent;
import isamrs.tim21.klinika.domain.ZahtevZaRegistraciju;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.dto.ZahtevZaRegistracijuDTO;
import isamrs.tim21.klinika.repository.KorisniciRepository;
import isamrs.tim21.klinika.repository.ZahtevZaRegistracijuRepository;

@Service
public class ZahtevZaRegistracijuService {
  @Autowired
  ZahtevZaRegistracijuRepository zahtevRepo;
  @Autowired
  KorisniciRepository korisniciRepository;

  public List<ZahtevZaRegistraciju> findAll() {
    return zahtevRepo.findAll();
  }

  public CustomResponse<ZahtevZaRegistraciju> kreiraj(ZahtevZaRegistraciju z) {
    z.setPacijent((Pacijent) korisniciRepository.findById(z.getPacijent().getId()).get());
    zahtevRepo.save(z);
    return new CustomResponse<ZahtevZaRegistraciju>(z, true, "Zahtev kreiran");
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
    String s ;
    //ako zahtev sa tim id-jem nije pronadjen, znaci da zahtev nije podnesen 
    //ili se korisnik vec registrovao
    if (null == zahtevZaRegistraciju){
      s = "<h1> Registracija nije uspela. Pokušajte ponovo. <h/1> ";
      return s;
    }
    //else enabluj korisnika i obrisi zahtev
    else{
      zahtevZaRegistraciju.getPacijent().setEnabled(true);
      zahtevRepo.deleteById(id);
      s = "<h1> Uspešno ste registrovani"
      +  "<br> <a href='http://localhost:8081'> Poseti kliniku </a> </h1> ";
      return s;
    }
  }
}