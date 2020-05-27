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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.jsonSerialize.IdentitySerializable;
import isamrs.tim21.klinika.jsonSerialize.IdentitySerializer;
import isamrs.tim21.klinika.jsonSerialize.ZdravstveniKartonSerializer;

@Entity
@Table(name="poseta")
public class Poseta implements IdentitySerializable{
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch=FetchType.EAGER)
	private Sifarnik bolest;

	@ManyToMany()
	@JoinTable(
		name="prepisani_lekovi",
		joinColumns = @JoinColumn(name="id_posete", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name="id_leka", referencedColumnName = "id"))
	private List<Sifarnik> lekovi;
	
	@Column(name="opis")
	private String opis;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JsonSerialize(using=ZdravstveniKartonSerializer.class)
	private ZdravstveniKarton zdravstveniKarton;
	
	@OneToOne(fetch=FetchType.EAGER)
	private Pregled pregled;
	
	@Version
	@Column(name="version", columnDefinition="integer DEFAULT 0", nullable=false)
	private Long version;


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

	public Sifarnik getBolest() {
		return bolest;
	}

	public void setBolest(Sifarnik bolest) {
		this.bolest = bolest;
	}

	public List<Sifarnik> getLekovi() {
		return lekovi;
	}

	public void setLekovi (List<Sifarnik> lekovi) {
		this.lekovi = lekovi;
	}


	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	
	
}
