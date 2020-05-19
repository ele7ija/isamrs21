package isamrs.tim21.klinika.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.jsonSerialize.IdentitySerializable;
import isamrs.tim21.klinika.jsonSerialize.ZdravstveniKartonSerializer;

@Entity
@Table(name="poseta")
public class Poseta implements IdentitySerializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="bolest")
	private String bolest;
	
	@Column(name="opis")
	private String opis;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JsonSerialize(using=ZdravstveniKartonSerializer.class)
	private ZdravstveniKarton zdravstveniKarton;
	
	@OneToOne(fetch=FetchType.EAGER)
	private Pregled pregled;
	
	public Poseta(){}

	public ZdravstveniKarton getZdravstveniKarton() {
		return zdravstveniKarton;
	}

	public void setZdravstveniKarton(ZdravstveniKarton zdravstveniKarton) {
		this.zdravstveniKarton = zdravstveniKarton;
	}

	public Pregled getPregled() {
		return pregled;
	}

	public void setPregled(Pregled pregled) {
		this.pregled = pregled;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBolest() {
		return bolest;
	}

	public void setBolest(String bolest) {
		this.bolest = bolest;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	
	
}
