package isamrs.tim21.klinika.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.jsonSerialize.IdentitySerializer;


@Entity
public abstract class MedicinskoOsoblje extends Korisnik{
	
	public RadniKalendar getRadniKalendar() {
		return radniKalendar;
	}

	public void setRadniKalendar(RadniKalendar radniKalendar) {
		this.radniKalendar = radniKalendar;
	}

	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JsonSerialize(using=IdentitySerializer.class)
	private Klinika klinika;

	@OneToOne(mappedBy="medicinskoOsoblje", fetch=FetchType.EAGER)
	@JsonSerialize(using=IdentitySerializer.class)
	private RadniKalendar radniKalendar;
	
	public Klinika getKlinika() {
		return klinika;
	}

	public void setKlinika(Klinika klinika) {
		this.klinika = klinika;
	}
	
	
}
