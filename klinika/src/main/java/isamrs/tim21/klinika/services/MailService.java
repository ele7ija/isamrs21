package isamrs.tim21.klinika.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.ZahtevZaRegistraciju;
import isamrs.tim21.klinika.dto.ZahtevZaRegistracijuDTO;
import isamrs.tim21.klinika.repository.ZahtevZaRegistracijuRepository;

@Service
public class MailService {
  @Autowired
  ZahtevZaRegistracijuRepository zahtevRepo;
  
  @Autowired
  private JavaMailSender javaMailSender;

  private String text;

	public void odbijZahtev(ZahtevZaRegistracijuDTO zahtevZaRegistracijuDTO) {
      ZahtevZaRegistraciju zahtev = 
      zahtevRepo.findById(zahtevZaRegistracijuDTO.getId()).orElse(null);
      String text = generateText(zahtevZaRegistracijuDTO, zahtev);
      String subject = "Odbijanje zahteva registracije";
      
      SimpleMailMessage mailToSend = new SimpleMailMessage();
      mailToSend.setTo(zahtev.getPacijent().getEmail());
      mailToSend.setText(text);
      mailToSend.setSubject(subject);
      javaMailSender.send(mailToSend);
  }
  
  public String generateText(ZahtevZaRegistracijuDTO zahtevDTO, ZahtevZaRegistraciju zahtev){
    text = "Poštovani korisniče ";
    text += zahtev.getPacijent().getIme();
    text += " ";
    text += zahtev.getPacijent().getPrezime();
    text += ", \n\n";
    text += zahtevDTO.getTekst();
    text += "\n\n";
    text += "Vaš ISA tim21.";
    return this.text;
  }
  
}