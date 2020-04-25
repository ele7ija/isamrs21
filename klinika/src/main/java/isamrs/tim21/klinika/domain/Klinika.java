package isamrs.tim21.klinika.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.jsonSerialize.IdentityListSerializer;
import isamrs.tim21.klinika.jsonSerialize.IdentitySerializable;

@Entity
@Table(name="klinika")
public class Klinika implements IdentitySerializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="naziv")
	private String naziv;
	
	@Column(name="adresa")
	private String adresa;
	
	@OneToMany(mappedBy="klinika", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JsonSerialize(using=IdentityListSerializer.class)
	private List<Sala> sale;
	
	@OneToMany(mappedBy="klinika", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JsonSerialize(using=IdentityListSerializer.class)
	private List<TipPregleda> tipoviPregleda;
	
	@OneToMany(mappedBy="klinika", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JsonSerialize(using=IdentityListSerializer.class)
	private List<Pregled> pregledi;
	
	@OneToMany(mappedBy="klinika", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JsonSerialize(using=IdentityListSerializer.class)
	private List<MedicinskoOsoblje> medicinskoOsoblje;
	
	@OneToMany(mappedBy="klinika", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JsonSerialize(using=IdentityListSerializer.class)
	private List<AdministratorKlinike> administratoriKlinike;
	
	public Klinika(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Sala> getSale() {
		return sale;
	}

	public void setSale(List<Sala> sale) {
		this.sale = sale;
	}

	public List<TipPregleda> getTipoviPregleda() {
		return tipoviPregleda;
	}

	public void setTipoviPregleda(List<TipPregleda> tipoviPregleda) {
		this.tipoviPregleda = tipoviPregleda;
	}

	public List<Pregled> getPregledi() {
		return pregledi;
	}

	public void setPregledi(List<Pregled> pregledi) {
		this.pregledi = pregledi;
	}

	public List<MedicinskoOsoblje> getMedicinskoOsoblje() {
		return medicinskoOsoblje;
	}

	public void setMedicinskoOsoblje(List<MedicinskoOsoblje> medicinskoOsoblje) {
		this.medicinskoOsoblje = medicinskoOsoblje;
	}

	public List<AdministratorKlinike> getAdministratoriKlinike() {
		return administratoriKlinike;
	}

	public void setAdministratoriKlinike(List<AdministratorKlinike> administratoriKlinike) {
		this.administratoriKlinike = administratoriKlinike;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
	
	
}
