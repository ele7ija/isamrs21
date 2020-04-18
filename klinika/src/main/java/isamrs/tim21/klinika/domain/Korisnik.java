package isamrs.tim21.klinika.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import isamrs.tim21.klinika.jsonSerialize.IdentitySerializable;

@Entity
@Table(name="korisnik")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType=DiscriminatorType.STRING)
public abstract class Korisnik implements IdentitySerializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;
	
	@Column(name="email", nullable=false)
	protected String email;
	
	@Column(name="sifra", nullable=false)
	protected String sifra;
	
	@Column(name="ime", nullable=false)
	protected String ime;
	
	@Column(name="prezime", nullable=false)
	protected String prezime;

	
	public Korisnik(){}

	public Korisnik(String email, String sifra, String ime, String prezime) {
		super();
		this.email = email;
		this.sifra = sifra;
		this.ime = ime;
		this.prezime = prezime;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	
}
