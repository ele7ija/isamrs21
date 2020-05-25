package isamrs.tim21.klinika.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.jsonSerialize.IdentityListSerializer;
import isamrs.tim21.klinika.jsonSerialize.IdentitySerializable;
import isamrs.tim21.klinika.jsonSerialize.PosetaSerializer;
import isamrs.tim21.klinika.jsonSerialize.UpitZaPregledListSerializer;

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
	private TipPregleda tipPregleda;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Sala sala;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Lekar lekar;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Klinika klinika;

	@OneToOne(mappedBy="pregled", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JsonSerialize(using=PosetaSerializer.class)
	private Poseta poseta;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="unapredDefinisaniPregled")
	@JsonSerialize(using=UpitZaPregledListSerializer.class)
	private List<UpitZaPregled> upiti;
	
	@ManyToMany(cascade=CascadeType.MERGE)
	@JoinTable(name="dodatne_operacije_lekara", joinColumns=@JoinColumn(name="id_pregleda", referencedColumnName="id"),
	inverseJoinColumns=@JoinColumn(name="id_lekara", referencedColumnName="id"))
	@JsonSerialize(using=IdentityListSerializer.class)
	private List<Lekar> dodatniLekari;
	
	@Version
	@Column(name="version", columnDefinition="integer DEFAULT 0", nullable=false)
	private Long version;
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Pregled(){}

	public Pregled(UpitZaPregled upitZaPregledToChange) {
		this.lekar = upitZaPregledToChange.getLekar();
		this.tipPregleda = upitZaPregledToChange.getTipPregleda();
		this.sala = upitZaPregledToChange.getSala();
		this.klinika = upitZaPregledToChange.getKlinika();
		this.upiti = new ArrayList<UpitZaPregled>(); //kasnije dodaj upit za pregled
		this.pocetakPregleda = upitZaPregledToChange.getPocetakPregleda();
		this.krajPregleda = upitZaPregledToChange.getKrajPregleda();
		
		//ostalo preuzmi od dummy objekta za pregled sa frontenda
		this.popust = upitZaPregledToChange.getUnapredDefinisaniPregled().getPopust();
		this.cena = upitZaPregledToChange.getUnapredDefinisaniPregled().getCena();
		this.konacnaCena = upitZaPregledToChange.getUnapredDefinisaniPregled().getKonacnaCena();
		this.dodatniLekari = upitZaPregledToChange.getUnapredDefinisaniPregled().getDodatniLekari();
	}

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
		if((pocetakPregleda.after(pregled.getPocetakPregleda()) || pocetakPregleda.equals(pregled.getPocetakPregleda())) && 
			pocetakPregleda.before(pregled.getKrajPregleda()) || pocetakPregleda.equals(pregled.getKrajPregleda()))
			return true;
		if((pregled.getPocetakPregleda().after(pocetakPregleda) || pregled.getPocetakPregleda().equals(pocetakPregleda)) && 
			pregled.getPocetakPregleda().before(krajPregleda) || pregled.getPocetakPregleda().equals(krajPregleda))
			return true;
		return false;
	}

	public List<UpitZaPregled> getUpiti() {
		return upiti;
	}

	public void setUpiti(List<UpitZaPregled> upiti) {
		this.upiti = upiti;
	}

	public List<Lekar> getDodatniLekari() {
		return dodatniLekari;
	}

	public void setDodatniLekari(List<Lekar> dodatniLekari) {
		this.dodatniLekari = dodatniLekari;
	}

	public boolean intersects(ZahtevZaGodisnji zahtevzaGodisnji) {
		Date pocetak = new Date(this.pocetakPregleda.getTime());
		Calendar cal = Calendar.getInstance();
		cal.setTime(pocetak);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		pocetak = cal.getTime();

		Date kraj = new Date(this.krajPregleda.getTime());
		cal.setTime(kraj);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		kraj = cal.getTime();


		if((pocetak.after(zahtevzaGodisnji.getPrviDanGodisnjeg()) || pocetak.equals(zahtevzaGodisnji.getPrviDanGodisnjeg())) && 
		pocetak.before(zahtevzaGodisnji.getPoslednjiDanGodisnjeg()) || pocetak.equals(zahtevzaGodisnji.getPoslednjiDanGodisnjeg()))
			return true;
		if((zahtevzaGodisnji.getPrviDanGodisnjeg().after(pocetak) || zahtevzaGodisnji.getPrviDanGodisnjeg().equals(pocetak)) && 
				zahtevzaGodisnji.getPrviDanGodisnjeg().before(kraj) || zahtevzaGodisnji.getPrviDanGodisnjeg().equals(kraj))
			return true;
		return false;
	}
	
}
