package isamrs.tim21.klinika.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.dto.PacijentDTO;
import isamrs.tim21.klinika.jsonSerialize.IdentitySerializer;

@Entity
@DiscriminatorValue(value="PA")
public class Pacijent extends Korisnik{
	
	@Column(name="drzava", nullable=false)
	private String drzava;
	
	@Column(name="grad")
	private String grad;
	
	@Column(name="adresa")
	private String adresa;
	
	@Column(name="brojTelefona", nullable=false)
	private String brojTelefona;
	
	@Column(name="jbo", nullable=false)
	private String jbo;
	
	@OneToOne(cascade=CascadeType.ALL)
	private ZdravstveniKarton zdravstveniKarton;
	
	@OneToOne(cascade=CascadeType.ALL)
	private ZahtevZaRegistraciju zahtev;
	
	public Pacijent() {
		super();
	}

	public Pacijent(String email, String sifra, String ime, String prezime) {
		super(email, sifra, ime, prezime);
	}

	public Pacijent(PacijentDTO noviPacijentDTO) {
		super(noviPacijentDTO.email, noviPacijentDTO.sifra, noviPacijentDTO.ime, noviPacijentDTO.prezime);
		this.drzava = noviPacijentDTO.drzava;
		this.grad = noviPacijentDTO.grad;
		this.adresa = noviPacijentDTO.adresa;
		this.brojTelefona = noviPacijentDTO.brojTelefona;
		this.jbo = noviPacijentDTO.jbo;
	}

	public ZdravstveniKarton getZdravstveniKarton() {
		return zdravstveniKarton;
	}

	public void setZdravstveniKarton(ZdravstveniKarton zdravstveniKarton) {
		this.zdravstveniKarton = zdravstveniKarton;
	}

	public String getDrzava() {
		return drzava;
	}

	public void setDrzava(String drzava) {
		this.drzava = drzava;
	}

	public String getGrad() {
		return grad;
	}

	public void setGrad(String grad) {
		this.grad = grad;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getBrojTelefona() {
		return brojTelefona;
	}

	public void setBrojTelefona(String brojTelefona) {
		this.brojTelefona = brojTelefona;
	}

	public String getJbo() {
		return jbo;
	}

	public void setJbo(String jbo) {
		this.jbo = jbo;
	}

	public ZahtevZaRegistraciju getZahtev() {
		return zahtev;
	}

	public void setZahtevZaReg(ZahtevZaRegistraciju zahtev) {
		this.zahtev = zahtev;
	}
	
	
}
