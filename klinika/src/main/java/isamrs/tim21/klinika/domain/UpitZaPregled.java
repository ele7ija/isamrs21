package isamrs.tim21.klinika.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.dto.UpitZaPregledDTO;
import isamrs.tim21.klinika.jsonSerialize.IdentitySerializable;
import isamrs.tim21.klinika.jsonSerialize.IdentitySerializer;

/** 
 * Pacijent pravi UpitZaPregled kada zeli da rezervise pregled.
 * Ovaj upit potom admin klinike treba da odobri. Postupak odobravanja
 * upita podrazumeva: postavljanje atributa odobren na true; pronalazak
 * unapred definisanog ili kreiranje novog {@link Pregled}-a; kreiranje
 * nove Posete i uvezivanja Pregleda i Posete.  
 * */
@Entity
@Table(name="upit_za_pregled")
public class UpitZaPregled implements IdentitySerializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/** Da li ga je admin odobrio */
	@Column(name="odobren")
	private Boolean odobren;
	
	/** Da li ga je pacijent potvrdio */
	@Column(name="potvrdjen")
	private Boolean potvrdjen;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "izmenjeni_id", referencedColumnName = "id")
	private UpitZaPregled izmenjeniPregled;
	
	@OneToOne(mappedBy = "izmenjeniPregled")
	private UpitZaPregled originalniPregled;
	
	@Column(name="pocetak_pregleda")
    @Temporal(TemporalType.TIMESTAMP)
	private Date pocetakPregleda;
	
	@Column(name="kraj_pregleda")
    @Temporal(TemporalType.TIMESTAMP)
	private Date krajPregleda;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private TipPregleda tipPregleda;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Lekar lekar;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonSerialize(using=IdentitySerializer.class)
	private Klinika klinika;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonSerialize(using=IdentitySerializer.class)
	private Pacijent pacijent;

	public UpitZaPregled(UpitZaPregledDTO u) {
		odobren = u.getOdobren();
		potvrdjen = u.getPotvrdjen();
		pocetakPregleda = u.getPocetakPregleda();
		krajPregleda = u.getKrajPregleda();
	}

	@Override
	public Long getId() {
		return id;
	}

	
	
	public UpitZaPregled() {
		super();
	}

	public Boolean getOdobren() {
		return odobren;
	}

	public void setOdobren(Boolean odobren) {
		this.odobren = odobren;
	}

	public Boolean getPotvrdjen() {
		return potvrdjen;
	}

	public void setPotvrdjen(Boolean potvrdjen) {
		this.potvrdjen = potvrdjen;
	}

	public UpitZaPregled getIzmenjeniPregled() {
		return izmenjeniPregled;
	}

	public void setIzmenjeniPregled(UpitZaPregled izmenjeniPregled) {
		this.izmenjeniPregled = izmenjeniPregled;
	}

	public UpitZaPregled getOriginalniPregled() {
		return originalniPregled;
	}

	public void setOriginalniPregled(UpitZaPregled originalniPregled) {
		this.originalniPregled = originalniPregled;
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

	public TipPregleda getTipPregleda() {
		return tipPregleda;
	}

	public void setTipPregleda(TipPregleda tipPregleda) {
		this.tipPregleda = tipPregleda;
	}

	public Lekar getLekar() {
		return lekar;
	}

	public void setLekar(Lekar lekar) {
		this.lekar = lekar;
	}

	public Klinika getKlinika() {
		return klinika;
	}

	public void setKlinika(Klinika klinika) {
		this.klinika = klinika;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Pacijent getPacijent() {
		return pacijent;
	}

	public void setPacijent(Pacijent pacijent) {
		this.pacijent = pacijent;
	}
	
	
}