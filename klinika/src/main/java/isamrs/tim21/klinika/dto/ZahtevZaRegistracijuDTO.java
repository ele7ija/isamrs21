package isamrs.tim21.klinika.dto;

import java.sql.Timestamp;

public class ZahtevZaRegistracijuDTO {
  public Long id;
  public Timestamp datumOdobrenja;
  public String tekst;
  public Boolean prihvacen;

  public ZahtevZaRegistracijuDTO
  (Long id, Timestamp datumOdobrenja, String tekst, Boolean prihvacen) {
    this.id = id;
    this.datumOdobrenja = datumOdobrenja;
    this.tekst = tekst;
    this.prihvacen = prihvacen;
  }

  public Long getId() {
    return this.id;
  }

  public Timestamp getDatumOdobrenja() {
    return this.datumOdobrenja;
  }

  public String getTekst() {
    return this.tekst;
  }

  public Boolean getPrihvacen() {
    return this.prihvacen;
  }

  public Boolean isPrihvacen() {
    return this.prihvacen;
  }
  

}