package isamrs.tim21.klinika.dto;

import java.sql.Timestamp;

public class ZahtevZaRegistracijuDTO {
  public Long id;
  public Timestamp datumOdobrenja;
  public Timestamp datumPodnosenja;

  public Timestamp getDatumPodnosenja() {
    return this.datumPodnosenja;
  }

  public void setDatumPodnosenja(Timestamp datumPodnosenja) {
    this.datumPodnosenja = datumPodnosenja;
  }

  public String tekst;
  public Boolean odobren;

  public ZahtevZaRegistracijuDTO
  (Long id, Timestamp datumOdobrenja, String tekst, Boolean odobren,
  Long pacijentId) {
    this.id = id;
    this.datumOdobrenja = datumOdobrenja;
    this.tekst = tekst;
    this.odobren = odobren;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Timestamp getDatumOdobrenja() {
    return this.datumOdobrenja;
  }

  public void setDatumOdobrenja(Timestamp datumOdobrenja) {
    this.datumOdobrenja = datumOdobrenja;
  }

  public String getTekst() {
    return this.tekst;
  }

  public void setTekst(String tekst) {
    this.tekst = tekst;
  }

  public Boolean isOdobren() {
    return this.odobren;
  }

  public Boolean getOdobren() {
    return this.odobren;
  }

  public void setOdobren(Boolean odobren) {
    this.odobren = odobren;
  }


  

}