package isamrs.tim21.klinika.domain;

import java.sql.Timestamp;
import java.util.Date;

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
	
	/*
	 * Pri definisanju slobodnog pregleda
	 * cena pregleda se preuzima iz odabranog tipa pregleda
	 * adminu klinike se ponudi da definise popust na cenu 
	 */
	@Column(nullable=false)
	private Long cena;

	@Column
	private Double popust;
	
	@Column(nullable=false)
	private Double konacnaCena;
		
	@Column(name="pocetak_pregleda")
    @Temporal(TemporalType.TIMESTAMP)
	private Date pocetakPregleda;
	
	@Column(name="kraj_pregleda")
    @Temporal(TemporalType.TIMESTAMP)
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
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonSerialize(using=IdentitySerializer.class)
	private Klinika klinika;

	@OneToOne(mappedBy="pregled", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
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
	
	public Long getCena() {
		return cena;
	}

	public void setCena(Long cena) {
		this.cena = cena;
	}

	public Double getPopust() {
		return popust;
	}

	public void setPopust(Double popust) {
		this.popust = popust;
	}

	public Double getKonacnaCena() {
		return konacnaCena;
	}

	public void setKonacnaCena(Double konacnaCena) {
		this.konacnaCena = konacnaCena;
	}

	public boolean intersects(Pregled pregled) {
		if(pocetakPregleda.after(pregled.getPocetakPregleda()) && pocetakPregleda.before(pregled.getKrajPregleda()))
			return true;
		if(pregled.getPocetakPregleda().after(pocetakPregleda) && pregled.getPocetakPregleda().before(krajPregleda))
			return true;
		return false;
	}
	
}
