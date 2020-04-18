package isamrs.tim21.klinika.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.jsonSerialize.IdentitySerializable;
import isamrs.tim21.klinika.jsonSerialize.IdentitySerializer;

@Entity
@Table(name="poseta")
public class Poseta implements IdentitySerializable{
	
	@Id
	private Long id;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JsonSerialize(using=IdentitySerializer.class)
	private ZdravstveniKarton zdravstveniKarton;
	
	@Temporal(TemporalType.DATE)
	private Date pocetakPregleda;
	
	@Temporal(TemporalType.DATE)
	private Date krajPregleda;
	
	@OneToOne(fetch=FetchType.LAZY)
	@MapsId //znaci da je id pregleda u tabeli poseta i primarni i strani kljuc
	@JsonSerialize(using=IdentitySerializer.class)
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

	public Date getPocetakPregleda() {
		return pocetakPregleda;
	}

	public void setPocetakPregleda(Date pocetakPregleda) {
		this.pocetakPregleda = pocetakPregleda;
	}

	public Date getKrajPregleda() {
		return krajPregleda;
	}

	public void setKrajPregleda(Date krajPregleda) {
		this.krajPregleda = krajPregleda;
	}


	
	
}
