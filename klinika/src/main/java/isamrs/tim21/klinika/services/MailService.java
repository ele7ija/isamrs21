package isamrs.tim21.klinika.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.Pacijent;
import isamrs.tim21.klinika.domain.ZahtevZaGodisnji;
import isamrs.tim21.klinika.domain.ZahtevZaRegistraciju;
import isamrs.tim21.klinika.dto.ZahtevZaRegistracijuDTO;
import isamrs.tim21.klinika.repository.ZahtevZaRegistracijuRepository;
import org.springframework.scheduling.annotation.Async;

@Service
public class MailService {
  @Autowired
  ZahtevZaRegistracijuRepository zahtevRepo;
  
  @Autowired
  private JavaMailSender javaMailSender;

  @Autowired
  Environment environment;

  private String subject;
  private String text;
  private String poruka;
  private String link= "http://localhost:8080/api/zahtevi_za_registraciju/registruj/";

  public void prihvatiZahtev(ZahtevZaRegistracijuDTO zahtevDTO){
    ZahtevZaRegistraciju zahtev = zahtevRepo.findById(zahtevDTO.getId()).orElse(null);
    subject = "Prihvatanje zahteva registracije";
    poruka = "Vas zahtev za registraciju je odobren. "
           + "Kliniknite na link ispod kako biste se uspesno registrovali."
           + link + zahtev.getId();   
    generateText(zahtev.getPacijent());

    SimpleMailMessage mailToSend = new SimpleMailMessage();
    mailToSend.setTo(zahtev.getPacijent().getEmail());
    mailToSend.setText(this.text);
    mailToSend.setSubject(subject);
    javaMailSender.send(mailToSend);
  }

	public void odbijZahtev(ZahtevZaRegistracijuDTO zahtevDTO) {
    ZahtevZaRegistraciju zahtev = zahtevRepo.findById(zahtevDTO.getId()).orElse(null);
    subject = "Odbijanje zahteva registracije";
    poruka = zahtevDTO.getTekst();
    generateText(zahtev.getPacijent());

    SimpleMailMessage mailToSend = new SimpleMailMessage();
    mailToSend.setTo(zahtev.getPacijent().getEmail());
    mailToSend.setText(this.text);
    mailToSend.setSubject(subject);
    javaMailSender.send(mailToSend);
  }
  
  public void generateText(Pacijent pacijent){
    text = "Poštovani korisniče ";
    text += pacijent.getIme() + " " + pacijent.getPrezime();
    text += ", \n\n";
    text += poruka;
    text += "\n\n";
    text += "Vaš ISA tim21.";
  }

  @Async
  public void prihvatiZahtevZaGodisnji(ZahtevZaGodisnji zahtev) {
    subject = "Prihvatanje zahteva za odsustvo";
    poruka = "Vaš zahtev za odsustvo je odobren.";
    generateText(zahtev);

    SimpleMailMessage mailToSend = new SimpleMailMessage();
    mailToSend.setTo(zahtev.getRadniKalendar().getMedicinskoOsoblje().getEmail());
    mailToSend.setText(this.text);
    mailToSend.setSubject(subject);
    javaMailSender.send(mailToSend);
  }

  private void generateText(ZahtevZaGodisnji zahtev) {
    text = "Poštovani korisniče ";
    text += zahtev.getRadniKalendar().getMedicinskoOsoblje().getIme() + " " + zahtev.getRadniKalendar().getMedicinskoOsoblje().getPrezime();
    text += ", \n\n";
    text += poruka;
    text += "\n\n";
    text += "Vaš ISA tim21.";
  }

  @Async
  public void odbiZahtevZaGodisnji(ZahtevZaGodisnji zahtev) {
    subject = "Odbijanje zahteva za odsustvo";
    poruka = "Vaš zahtev za odsustvo je odbijen. Razlog: '" + zahtev.getRazlogOdbijanja() + "'.";
    generateText(zahtev);

    SimpleMailMessage mailToSend = new SimpleMailMessage();
    mailToSend.setTo(zahtev.getRadniKalendar().getMedicinskoOsoblje().getEmail());
    mailToSend.setText(this.text);
    mailToSend.setSubject(subject);
    javaMailSender.send(mailToSend);
  }
  
}