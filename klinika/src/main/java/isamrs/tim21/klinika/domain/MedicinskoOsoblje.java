package isamrs.tim21.klinika.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public abstract class MedicinskoOsoblje extends Korisnik{
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Klinika klinika;

	public Klinika getKlinika() {
		return klinika;
	}

	public void setKlinika(Klinika klinika) {
		this.klinika = klinika;
	}
	
	
}
