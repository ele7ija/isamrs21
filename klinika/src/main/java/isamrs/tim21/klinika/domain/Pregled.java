package isamrs.tim21.klinika.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.jsonSerialize.IdentitySerializable;
import isamrs.tim21.klinika.jsonSerialize.IdentitySerializer;

/**
 * Pregled je entitet koji nije vezan za slucaj konkretnog pacijenta.
 * Npr. pregled nema opis sta je u sklopu njega radjeno nad pacijentom,
 * koja je bolest dijagnostifikovana, koja je terapija prepisana.
 * Konkretni detalji o tome sta je radjeno na pregledu i u sklopu cijeg
 * kartona je u entitetu {@link Poseta}.
 *
 */
@Entity
@Table(name="pregled")
public class Pregled implements IdentitySerializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	private Date pocetakPregleda;
	
	@Temporal(TemporalType.DATE)
	private Date krajPregleda;
	
	@ManyToOne(fetch=FetchType.EAGER)
	//@JsonSerialize(using=IdentitySerializer.class)
	private TipPregleda tipPregleda;
	
	@ManyToOne(fetch=FetchType.EAGER)
	//@JsonSerialize(using=IdentitySerializer.class)
	private Sala sala;
	
	@ManyToOne(fetch=FetchType.EAGER)
	//@JsonSerialize(using=IdentitySerializer.class)
	private Lekar lekar;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JsonSerialize(using=IdentitySerializer.class)
	private Klinika klinika;

	@OneToOne(mappedBy="pregled", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JsonSerialize(using=IdentitySerializer.class)
	private Poseta poseta;
	
	public Pregled(){}

	public Klinika getKlinika() {
		return klinika;
	}

	public void setKlinika(Klinika klinika) {
		this.klinika = klinika;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipPregleda getTipPregleda() {
		return tipPregleda;
	}

	public void setTipPregleda(TipPregleda tipPregleda) {
		this.tipPregleda = tipPregleda;
	}

	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	public Lekar getLekar() {
		return lekar;
	}

	public void setLekar(Lekar lekar) {
		this.lekar = lekar;
	}

	public Poseta getPoseta() {
		return poseta;
	}

	public void setPoseta(Poseta poseta) {
		this.poseta = poseta;
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
