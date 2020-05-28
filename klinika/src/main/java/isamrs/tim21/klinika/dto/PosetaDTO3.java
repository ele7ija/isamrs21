package isamrs.tim21.klinika.dto;

import java.util.List;

import isamrs.tim21.klinika.domain.Sifarnik;

public class PosetaDTO3 {
  private String opis;
  private Long posetaId;
  private Sifarnik bolest;
  private List<Sifarnik> lekovi;
  private String dioptrija;
  private String krvnaGrupa;
  private Integer visina;
  private Integer tezina;

  public PosetaDTO3(
    String opis, Long posetaId, Sifarnik selectedDijagnoza, List<Sifarnik> selectedLekovi,
     String dioptrija, String krvnaGrupa, Integer visina, Integer tezina ){
    this.opis = opis;
    this.posetaId = posetaId;
    this.bolest = selectedDijagnoza;
    this.lekovi = selectedLekovi;
    this.dioptrija = dioptrija;
    this.krvnaGrupa = krvnaGrupa;
    this.visina = visina;
    this.tezina = tezina;
  }

  public Long getPosetaId() {
    return this.posetaId;
  }

  public void setPosetaId(Long posetaId) {
    this.posetaId = posetaId;
  }

  public Sifarnik getBolest() {
    return this.bolest;
  }

  public void setBolest(Sifarnik bolest) {
    this.bolest = bolest;
  }

  public List<Sifarnik> getLekovi() {
    return this.lekovi;
  }

  public void setLekovi(List<Sifarnik> lekovi) {
    this.lekovi = lekovi;
  }

  public String getOpis() {
    return this.opis;
  }

  public void setOpis(String opis) {
    this.opis = opis;
  }

  public String getDioptrija() {
    return this.dioptrija;
  }

  public void setDioptrija(String dioptrija) {
    this.dioptrija = dioptrija;
  }

  public String getKrvnaGrupa() {
    return this.krvnaGrupa;
  }

  public void setKrvnaGrupa(String krvnaGrupa) {
    this.krvnaGrupa = krvnaGrupa;
  }

  public Integer getVisina() {
    return this.visina;
  }

  public void setVisina(Integer visina) {
    this.visina = visina;
  }

  public Integer getTezina() {
    return this.tezina;
  }

  public void setTezina(Integer tezina) {
    this.tezina = tezina;
  }


}