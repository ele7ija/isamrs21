package isamrs.tim21.klinika.dto;

public class ReceptDTO {
  private Long id;
  private Boolean overen;

  public ReceptDTO(Long id, Boolean overen){
    this.id = id;
    this.overen = overen;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
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