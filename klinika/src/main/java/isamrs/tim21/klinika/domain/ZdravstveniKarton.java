package isamrs.tim21.klinika.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.jsonSerialize.IdentitySerializable;
import isamrs.tim21.klinika.jsonSerialize.KorisnikSerializer;

@Entity
@Table(name="zdravstveni_karton")
public class ZdravstveniKarton implements IdentitySerializable{
	@Id
	private Long id;
	
	@OneToOne(fetch=FetchType.EAGER)
	@MapsId //znaci da je id pacijenta u tabeli zdravstvenog kartona i primarni i strani kljuc
	@JsonSerialize(using=KorisnikSerializer.class)
	private Pacijent pacijent;
	
	@OneToMany(mappedBy="zdravstveniKarton", cascade=CascadeType.ALL)
	private List<Poseta> posete;

	@Column(name="dioptrija")
	private String dioptrija;

	@Column(name="krvna_grupa")
	private String krvnaGrupa;

	@Column(name="visina")
	private Integer visina;

	@Column(name="tezina")
	private Integer tezina;


	public Long getId() {
		return id;
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

	public List<Poseta> getPosete() {
		return posete;
	}

	public void setPosete(List<Poseta> posete) {
		this.posete = posete;
	}
	
	//ostale stvari, pregledi, bolesti...

	public String getDioptrija() {
		return this.dioptrija;
	}

	public void setDioptrija(String dioptrija) {
		this.dioptrija = dioptrija;
	}

	public String getKrvnaGrupa() {
		return this.krvnaGrupa;
	}

	public void setKrvnaGrupa(String krvnaGrupa) {
		this.krvnaGrupa = krvnaGrupa;
	}

	public Integer getVisina() {
		return this.visina;
	}

	public void setVisina(Integer visina) {
		this.visina = visina;
	}

	public Integer getTezina() {
		return this.tezina;
	}

	public void setTezina(Integer tezina) {
		this.tezina = tezina;
	}
	
	
}
