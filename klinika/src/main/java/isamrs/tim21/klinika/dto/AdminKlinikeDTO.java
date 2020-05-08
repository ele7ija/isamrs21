package isamrs.tim21.klinika.dto;

import isamrs.tim21.klinika.domain.Klinika;

public class AdminKlinikeDTO {
  public String ime;
  public String prezime;
  public String email;
  public String sifra;
  public Klinika klinika;
  public Long klinikaId;

  public AdminKlinikeDTO(){}

  public AdminKlinikeDTO(String ime, String prezime, String email, String sifra,Long klinikaId) {
    this.ime = ime;
    this.prezime = prezime;
    this.email = email;
    this.sifra = sifra;
    this.klinikaId = klinikaId;
  }
  
  public String getSifra() {
		return sifra;
	}
}
