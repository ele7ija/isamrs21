package isamrs.tim21.klinika.dto;

import isamrs.tim21.klinika.domain.MedicinskaSestra;

public class ReceptDTO {
  private Long id;
  private Boolean overen;
  private MedicinskaSestra medicinskaSestra;

  public ReceptDTO(){}

  public ReceptDTO(Long id, Boolean overen, MedicinskaSestra medicinskaSestra){
    this.id = id;
    this.overen = overen;
    this.medicinskaSestra = medicinskaSestra;
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

  public MedicinskaSestra getMedicinskaSestra() {
    return this.medicinskaSestra;
  }

  public void setMedicinskaSestra(MedicinskaSestra medicinskaSestra) {
    this.medicinskaSestra = medicinskaSestra;
  }

}