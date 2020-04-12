package isamrs.tim21.klinika.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="pregled")
public class Pregled {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private TipPregleda tipPregleda;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Sala sala;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Lekar lekar;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
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
	
	
	
	
}
