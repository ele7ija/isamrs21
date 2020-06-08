package isamrs.tim21.klinika.dto;

public class ReceptDTO {
  private Long posetaId;
  private Boolean overen;

  public ReceptDTO(Long posetaId, Boolean overen){
    this.posetaId = posetaId;
    this.overen = overen;
  }

  public Long getPosetaId() {
    return this.posetaId;
  }

  public void setPosetaId(Long posetaId) {
    this.posetaId = posetaId;
  }

  public Boolean isOveren() {
    return this.overen;
  }

  public Boolean getOveren() {
    return this.overen;
  }

  public void setOveren(Boolean overen) {
    this.overen = overen;
  }

}