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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import isamrs.tim21.klinika.jsonSerialize.IdentitySerializer;
import isamrs.tim21.klinika.jsonSerialize.IdentityListSerializer;
import isamrs.tim21.klinika.jsonSerialize.IdentitySerializable;

@Entity
@Table(name="tip_pregleda")
public class TipPregleda implements IdentitySerializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="naziv", nullable=false, unique=true)
	private String naziv;
	
	@Column(name="opis", nullable=false)
	private String opis;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JsonSerialize(using=IdentitySerializer.class)
	private Klinika klinika;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="specijalnost_lekara", joinColumns=@JoinColumn(name="id_tipa_pregleda", referencedColumnName="id"),
	inverseJoinColumns=@JoinColumn(name="id_lekara", referencedColumnName="id"))
	@JsonSerialize(using=IdentityListSerializer.class)
	private List<Lekar> lekari;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JsonSerialize(using=IdentityListSerializer.class)
	private Cenovnik cenovnik;

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

	public Cenovnik getCenovnik() {
		return cenovnik;
	}

	public void setCenovnik(Cenovnik cenovnik) {
		this.cenovnik = cenovnik;
	}

	
	
	
}
