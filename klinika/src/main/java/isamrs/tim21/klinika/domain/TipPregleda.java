package isamrs.tim21.klinika.domain;

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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFilter;

@Entity
@Table(name="tip_pregleda")
public class TipPregleda {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="naziv", nullable=false, unique=true)
	private String naziv;
	
	@Column(name="opis", nullable=false)
	private String opis;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JsonFilter("tipPregleda_to_klinika_filter")
	private Klinika klinika;
	
	@ManyToMany
	@JoinTable(name="specijalnost_lekara", joinColumns=@JoinColumn(name="id_tipa_pregleda", referencedColumnName="id"),
	inverseJoinColumns=@JoinColumn(name="id_lekara", referencedColumnName="id"))
	private List<Lekar> lekari;

	public List<Lekar> getLekari() {
		return lekari;
	}

	public void setLekari(List<Lekar> lekari) {
		this.lekari = lekari;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public TipPregleda(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Klinika getKlinika() {
		return klinika;
	}

	public void setKlinika(Klinika klinika) {
		this.klinika = klinika;
	}

	
	
	
}
