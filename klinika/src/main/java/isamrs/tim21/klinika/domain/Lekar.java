package isamrs.tim21.klinika.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.jsonSerialize.IdentityListSerializer;
import isamrs.tim21.klinika.jsonSerialize.PregledListSerializer;


@Entity
@DiscriminatorValue(value="LE")
public class Lekar extends MedicinskoOsoblje{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4362887523765862539L;

	@ManyToMany(cascade=CascadeType.MERGE, mappedBy="lekari")
	@JsonIgnoreProperties(value={"lekari"}, allowSetters = true)
	private List<TipPregleda> tipovi_pregleda;
	
	@Column(name="broj_specijalizacija", columnDefinition="integer DEFAULT 0")
	private int brojSpecijalizacija;
	
	@OneToMany(mappedBy="lekar", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JsonSerialize(using=PregledListSerializer.class)
	private List<Pregled> pregledi;
	
	@ManyToMany(cascade=CascadeType.MERGE, mappedBy="dodatniLekari")
	private List<Pregled> dodatneOperacije;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "lekar")
	@JsonSerialize(using=IdentityListSerializer.class)
	private List<Ocena> ocene;

	public List<Ocena> getOcene() {
		return this.ocene;
	}

	public void setOcene(List<Ocena> ocene) {
		this.ocene = ocene;
	}

	public List<TipPregleda> getTipovi_pregleda() {
		return tipovi_pregleda;
	}

	public void setTipovi_pregleda(List<TipPregleda> tipovi_pregleda) {
		this.tipovi_pregleda = tipovi_pregleda;
	}

	public List<Pregled> getPregledi() {
		return pregledi;
	}

	public void setPregledi(List<Pregled> pregledi) {
		this.pregledi = pregledi;
	}

	public int getBrojSpecijalizacija() {
		return brojSpecijalizacija;
	}

	public void setBrojSpecijalizacija(int brojSpecijalizacija) {
		this.brojSpecijalizacija = brojSpecijalizacija;
	}

	public Lekar copy() {
		Lekar retval = new Lekar();
		retval.setAuthorities(getAuthorities());
		retval.setBrojSpecijalizacija(brojSpecijalizacija);
		retval.setEmail(email);
		retval.setEnabled(enabled);
		retval.setId(id);
		retval.setIme(ime);
		retval.setKlinika(klinika);
		retval.setPoslednjaPromenaSifre(poslednjaPromenaSifre);
		retval.setPregledi(pregledi);
		retval.setPrezime(prezime);
		retval.setRadniKalendar(radniKalendar);
		retval.setSifra(sifra);
		retval.setTipovi_pregleda(tipovi_pregleda);
		return retval;
	}

	public List<Pregled> getDodatneOperacije() {
		return dodatneOperacije;
	}

	public void setDodatneOperacije(List<Pregled> dodatneOperacije) {
		this.dodatneOperacije = dodatneOperacije;
	}
	
}
