package isamrs.tim21.klinika.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="recept")
public class Recept {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
  private Poseta poseta;

  @OneToOne
  private MedicinskaSestra medicinskaSestra;

  @Column(name = "overen")
  private Boolean overen = false;


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

  public Poseta getPoseta() {
    return this.poseta;
  }

  public void setPoseta(Poseta poseta) {
    this.poseta = poseta;
  }

  public List<Sifarnik> getRecepti(Poseta poseta) {
    return poseta.getLekovi();
  }


  public MedicinskaSestra getMedicinskaSestra() {
    return this.medicinskaSestra;
  }

  public void setMedicinskaSestra(MedicinskaSestra medicinskaSestra) {
    this.medicinskaSestra = medicinskaSestra;
  }



}