package isamrs.tim21.klinika.dto;

import java.util.List;

import isamrs.tim21.klinika.domain.Sifarnik;

public class PosetaDTO3 {
  private Long posetaId;
  private Sifarnik bolest;
  private List<Sifarnik> lekovi;
  private String opis;
  private Long zdravstveniKartonId;

  public PosetaDTO3(){}

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

  public Long getZdravstveniKartonId() {
    return this.zdravstveniKartonId;
  }

  public void setZdravstveniKartonId(Long zdravstveniKartonId) {
    this.zdravstveniKartonId = zdravstveniKartonId;
  }


}