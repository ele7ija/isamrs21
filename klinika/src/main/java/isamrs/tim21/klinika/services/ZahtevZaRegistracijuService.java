package isamrs.tim21.klinika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.Pacijent;
import isamrs.tim21.klinika.domain.ZahtevZaRegistraciju;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.dto.ZahtevZaRegistracijuDTO;
import isamrs.tim21.klinika.exceptions.BusinessLogicException;
import isamrs.tim21.klinika.exceptions.EntityNotFoundException;
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
    return new CustomResponse<>(z, true, "Zahtev kreiran");
  }

  @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
  public ZahtevZaRegistraciju delete(ZahtevZaRegistracijuDTO zahtevZaRegistracijuDTO) throws EntityNotFoundException, BusinessLogicException{
    ZahtevZaRegistraciju zahtevZaRegistraciju = 
      zahtevRepo.findById(zahtevZaRegistracijuDTO.getId()).orElse(null);
    if(zahtevZaRegistraciju == null)
      throw new EntityNotFoundException("Zahtev za registraciju");
    if(zahtevZaRegistraciju.getOdobren())
      throw new BusinessLogicException("Zahtev je već odobren");
    zahtevRepo.delete(zahtevZaRegistraciju);
    return zahtevZaRegistraciju;
  }

  @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
  public ZahtevZaRegistraciju update(ZahtevZaRegistracijuDTO zahtevZaRegistracijuDTO) throws EntityNotFoundException, BusinessLogicException{
    ZahtevZaRegistraciju zahtevZaRegistraciju = zahtevRepo.findById(zahtevZaRegistracijuDTO.getId()).orElse(null);
    if(zahtevZaRegistraciju == null)
      throw new EntityNotFoundException("Zahtev za registraciju");
    if(zahtevZaRegistraciju.getOdobren())
      throw new BusinessLogicException("Zahtev je već odobren");
    zahtevZaRegistraciju.setDatumOdobrenja(zahtevZaRegistracijuDTO.getDatumOdobrenja());
    zahtevZaRegistraciju.setOdobren(zahtevZaRegistracijuDTO.getOdobren());
    zahtevZaRegistraciju.getPacijent().setEnabled(true); //posto u deployovanoj aplikaciji ne radi mejl, odmah enable-uj pacijenta
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