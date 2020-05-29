package isamrs.tim21.klinika.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.Pacijent;
import isamrs.tim21.klinika.domain.Poseta;
import isamrs.tim21.klinika.domain.UpitZaPregled;
import isamrs.tim21.klinika.domain.VrstaTipaPregleda;
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
    String subject = "Prihvatanje zahteva za odsustvo";
    String poruka = "Vaš zahtev za odsustvo je odobren.";
    String text = generateText(zahtev, poruka);

    SimpleMailMessage mailToSend = new SimpleMailMessage();
    mailToSend.setTo(zahtev.getRadniKalendar().getMedicinskoOsoblje().getEmail());
    mailToSend.setText(text);
    mailToSend.setSubject(subject);
    javaMailSender.send(mailToSend);
  }

  private String generateText(ZahtevZaGodisnji zahtev, String poruka) {
    String text = "Poštovani korisniče ";
    text += zahtev.getRadniKalendar().getMedicinskoOsoblje().getIme() + " " + zahtev.getRadniKalendar().getMedicinskoOsoblje().getPrezime();
    text += ", \n\n";
    text += poruka;
    text += "\n\n";
    text += "Vaš ISA tim21.";
    return text;
  }

  @Async
  public void odbiZahtevZaGodisnji(ZahtevZaGodisnji zahtev) {
    String subject = "Odbijanje zahteva za odsustvo";
    String poruka = "Vaš zahtev za odsustvo je odbijen. Razlog: '" + zahtev.getRazlogOdbijanja() + "'.";
    String text = generateText(zahtev, poruka);

    SimpleMailMessage mailToSend = new SimpleMailMessage();
    mailToSend.setTo(zahtev.getRadniKalendar().getMedicinskoOsoblje().getEmail());
    mailToSend.setText(text);
    mailToSend.setSubject(subject);
    javaMailSender.send(mailToSend);
  }
  
  @Async
  public void potvrdaRezervacije(Poseta p) {
    String subject = "Potvrda rezervacije pregleda";
    String poruka = "Rezervisali ste pregled:\n" +
      "\tTip pregleda: " + p.getPregled().getTipPregleda().getNaziv() + "\n" +
      "\tKlinika: \"" + p.getPregled().getKlinika().getNaziv() + "\", " +
      p.getPregled().getKlinika().getAdresa() + "\n" + 
      "\tVreme: " + p.getPregled().getPocetakPregleda().toString() + " - " + 
      p.getPregled().getKrajPregleda().toString() + "\n" +
      "\tLekar: " + p.getPregled().getLekar().getIme() + " " + p.getPregled().getLekar().getPrezime() + "\n" +
      "\tSala: " + p.getPregled().getSala().getOznaka();
    poruka += "\n\n\nSve najbolje Vam želi klinika XYZ.";   
    SimpleMailMessage mailToSend = new SimpleMailMessage();
    mailToSend.setTo(p.getZdravstveniKarton().getPacijent().getEmail());
    mailToSend.setText(poruka);
    mailToSend.setSubject(subject);
    javaMailSender.send(mailToSend);
  }

  @Async
  public void odustajanjeOdRezervacije(UpitZaPregled upit, Lekar lekar){
    String subject = "";
    String poruka = "";
    if(upit.getTipPregleda().getVrsta() == VrstaTipaPregleda.operacija){
      subject = "Otkazivanje rezervacije operacije";
      poruka = "Operacija na kojoj ste bili angažovani je otkazana.\n\n";
      poruka += "Operacija:\n" + "\tTip operacije: " + upit.getTipPregleda().getNaziv() + "\n";
    }
    else{
      subject = "Otkazivanje rezervacije pregleda";
      poruka = "Pregled na kojem ste bili angažovani je otkazan.\n\n";
      poruka += "Pregled:\n" + "\tTip pregleda: " + upit.getTipPregleda().getNaziv() + "\n";
    }
    poruka += "\tKlinika: \"" + upit.getKlinika().getNaziv() + "\", " +
      upit.getKlinika().getAdresa() + "\n" + 
      "\tVreme: " + upit.getPocetakPregleda().toString() + " - " + 
      upit.getKrajPregleda().toString() + "\n" +
      "\tPacijent: " + upit.getPacijent().getIme() + " " + upit.getPacijent().getPrezime();
    poruka += ".\n\nSve najbolje Vam želi klinika XYZ."; 
    SimpleMailMessage mailToSend = new SimpleMailMessage();
    mailToSend.setTo(lekar.getEmail());
    mailToSend.setText(poruka);
    mailToSend.setSubject(subject);
    javaMailSender.send(mailToSend);
  }

  @Async
  public void upitOdobren(UpitZaPregled upit, boolean izmenjenUpit) {
    String subject = "";
    String poruka = "";
    if(!izmenjenUpit){
      subject = "Vaš upit je odobren.";
      poruka = "Vaš upit za pregled je odobren od strane administratora.\n\n";
      poruka += "Upit:\n" +
        "\tTip pregleda: " + upit.getTipPregleda().getNaziv() + "\n" +
        "\tKlinika: \"" + upit.getKlinika().getNaziv() + "\", " +
        upit.getKlinika().getAdresa() + "\n" + 
        "\tVreme: " + upit.getPocetakPregleda().toString() + " - " + 
        upit.getKrajPregleda().toString() + "\n" +
        "\tLekar: " + upit.getLekar().getIme() + " " + upit.getLekar().getPrezime() + "\n" +
        "\tSala: " + upit.getSala().getOznaka();
      poruka += "\nMolimo vas da upit potvrdite ili odbijete odlaskom na http://localhost:8081/#/pacijent/istorija" + 
      "\n\nSve najbolje Vam želi klinika XYZ.";   
      SimpleMailMessage mailToSend = new SimpleMailMessage();
      mailToSend.setTo(upit.getPacijent().getEmail());
      mailToSend.setText(poruka);
      mailToSend.setSubject(subject);
      javaMailSender.send(mailToSend);
    }else{
      subject = "Vaš upit je izmenjen i odobren.";
      poruka = "Vaš upit za pregled je izmenjen i odobren od strane administratora.\n\n";
      poruka += "Novi podaci upita:\n" +
        "\tTip pregleda: " + upit.getTipPregleda().getNaziv() + "\n" +
        "\tKlinika: \"" + upit.getKlinika().getNaziv() + "\", " +
        upit.getKlinika().getAdresa() + "\n" + 
        "\tVreme: " + upit.getPocetakPregleda().toString() + " - " + 
        upit.getKrajPregleda().toString() + "\n" +
        "\tLekar: " + upit.getLekar().getIme() + " " + upit.getLekar().getPrezime();
      poruka += "\n\nStari podaci upita:\n" +
        "\tTip pregleda: " + upit.getOriginalniPregled().getTipPregleda().getNaziv() + "\n" +
        "\tKlinika: \"" + upit.getOriginalniPregled().getKlinika().getNaziv() + "\", " +
      upit.getOriginalniPregled().getKlinika().getAdresa() + "\n" + 
        "\tVreme: " + upit.getOriginalniPregled().getPocetakPregleda().toString() + " - " + 
      upit.getOriginalniPregled().getKrajPregleda().toString() + "\n" +
        "\tLekar: " + upit.getOriginalniPregled().getLekar().getIme() + " " + upit.getOriginalniPregled().getLekar().getPrezime() + "\n" +
        "\tSala: " + upit.getSala().getOznaka();
      poruka += "\nMolimo vas da upit potvrdite ili odbijete odlaskom na http://localhost:8081/#/pacijent/istorija" + 
      "\n\nSve najbolje Vam želi klinika XYZ.";   
      SimpleMailMessage mailToSend = new SimpleMailMessage();
      mailToSend.setTo(upit.getPacijent().getEmail());
      mailToSend.setText(poruka);
      mailToSend.setSubject(subject);
      javaMailSender.send(mailToSend);
    }
    
  }

  @Async
  public void upitOdbijen(UpitZaPregled upit) {
    String subject = "";
    String poruka = "";
    subject = "Vaš upit je ODBIJEN.";
    poruka = "Vaš upit za pregled je ODBIJEN od strane administratora.\n\n";
    poruka += "Upit:\n" +
      "\tTip pregleda: " + upit.getTipPregleda().getNaziv() + "\n" +
      "\tKlinika: \"" + upit.getKlinika().getNaziv() + "\", " +
      upit.getKlinika().getAdresa() + "\n" + 
      "\tVreme: " + upit.getPocetakPregleda().toString() + " - " + 
      upit.getKrajPregleda().toString() + "\n" +
      "\tLekar: " + upit.getLekar().getIme() + " " + upit.getLekar().getPrezime();
    poruka += "\nMolimo vas da potvrdite da ste obavešteni o ovome na adresi: http://localhost:8081/#/pacijent/istorija" + 
    ".\n\nSve najbolje Vam želi klinika XYZ."; 
    SimpleMailMessage mailToSend = new SimpleMailMessage();
    mailToSend.setTo(upit.getPacijent().getEmail());
    mailToSend.setText(poruka);
    mailToSend.setSubject(subject);
    javaMailSender.send(mailToSend);
  }

  @Async
  public void obavestiLekara(UpitZaPregled upit, Lekar lekar, boolean glavni, boolean izmenjenUpit) {
    String subject = "";
    String poruka = "";
    if(upit.getTipPregleda().getVrsta() == VrstaTipaPregleda.operacija){
      subject = "Rezervacija operacije";
      if(glavni)
        poruka = "Angažovani ste kao vodeći lekar na operaciji.\n\n";
      else
        poruka = "Angažovani ste kao dodatni lekar na operaciji.\n\n";
      poruka += "Operacija:\n" + "\tTip operacije: " + upit.getTipPregleda().getNaziv() + "\n";
    }
    else{
      subject = "Rezervacija pregleda";
      poruka = "Angažovani ste kao lekar na pregledu.\n\n";
      poruka += "Pregled:\n" + "\tTip pregleda: " + upit.getTipPregleda().getNaziv() + "\n";
    }
    poruka += "\tKlinika: \"" + upit.getKlinika().getNaziv() + "\", " +
      upit.getKlinika().getAdresa() + "\n" + 
      "\tVreme: " + upit.getPocetakPregleda().toString() + " - " + 
      upit.getKrajPregleda().toString() + "\n" +
      "\tPacijent: " + upit.getPacijent().getIme() + " " + upit.getPacijent().getPrezime() + "\n" + 
      "\tSala: " + upit.getSala().getOznaka();;
    poruka += ".\n\nSve najbolje Vam želi klinika XYZ."; 
    SimpleMailMessage mailToSend = new SimpleMailMessage();
    mailToSend.setTo(lekar.getEmail());
    mailToSend.setText(poruka);
    mailToSend.setSubject(subject);
    javaMailSender.send(mailToSend);
  }
}