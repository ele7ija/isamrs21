package isamrs.tim21.klinika.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.jsonSerialize.IdentityListSerializer;


@Entity
@DiscriminatorValue(value="LE")
public class Lekar extends MedicinskoOsoblje{
	
	@ManyToMany(mappedBy="lekari")
	@JsonSerialize(using=IdentityListSerializer.class)
	private List<TipPregleda> tipovi_pregleda;
	
	@OneToMany(mappedBy="lekar", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JsonSerialize(using=IdentityListSerializer.class)
	private List<Pregled> pregledi;
	
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
	
	
}
