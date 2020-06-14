package isamrs.tim21.klinika.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.jsonSerialize.IdentitySerializable;
import isamrs.tim21.klinika.jsonSerialize.IdentitySerializer;
import isamrs.tim21.klinika.jsonSerialize.PregledListSerializer;

@Entity
@Table(name="sala")
public class Sala implements IdentitySerializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Version
	@Column(name="version", columnDefinition="integer DEFAULT 0", nullable=false)
	private Long version;
	
	public String getOznaka() {
		return oznaka;
	}

	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}

	@Column(name="oznaka", nullable=false, unique=true)
	private String oznaka;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JsonSerialize(using=IdentitySerializer.class)
	private Klinika klinika;
	
	@OneToMany(mappedBy="sala", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JsonSerialize(using=PregledListSerializer.class)
	private List<Pregled> pregledi;
	
	public Sala(){}
	
	public List<Pregled> getPregledi() {
		return pregledi;
	}

	public void setPregledi(List<Pregled> pregledi) {
		this.pregledi = pregledi;
	}

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

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
	
}
