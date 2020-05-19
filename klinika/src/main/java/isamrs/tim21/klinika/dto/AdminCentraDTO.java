package isamrs.tim21.klinika.dto;

public class AdminCentraDTO {
  public String ime;
  public String prezime;
  public String email;
  public String sifra;

  public AdminCentraDTO () {}

  public AdminCentraDTO(String ime, String prezime, String email, String sifra) {
    this.ime = ime;
    this.prezime = prezime;
    this.email = email;
    this.sifra = sifra;
  }

  public String getIme() {
    return this.ime;
  }

  public String getPrezime() {
    return this.prezime;
  }

  public String getEmail() {
    return this.email;
  }

  public String getSifra() {
    return this.sifra;
  }
}