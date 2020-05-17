package isamrs.tim21.klinika.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Sifarnik {
  /**
   * ova klasa modeluje dijagnoze i lekove
   * type moze biti dijagnoza ili lek
   */
  enum Tip{ DIJAGNOZA, LEK }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="tip")
  @Enumerated(EnumType.STRING)
  private Tip tip;

  @Column(name="sifra", nullable=false, unique=true)
  private String sifra;

  @Column(name="naziv", nullable=false, unique=true)
  private String naziv;

  Sifarnik () {}


  public Sifarnik(Long id, Tip tip, String sifra, String naziv) {
    this.id = id;
    this.tip = tip;
    this.sifra = sifra;
    this.naziv = naziv;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Tip getTip() {
    return this.tip;
  }

  public void setTip(Tip tip) {
    this.tip = tip;
  }

  public String getSifra() {
    return this.sifra;
  }

  public void setSifra(String sifra) {
    this.sifra = sifra;
  }

  public String getNaziv() {
    return this.naziv;
  }

  public void setNaziv(String naziv) {
    this.naziv = naziv;
  }

}