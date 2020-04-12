package isamrs.tim21.klinika.domain;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@DiscriminatorValue(value="PA")
public class Pacijent extends Korisnik{
	
	@OneToOne(mappedBy="pacijent", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private ZdravstveniKarton zdravstveniKarton;
	
	public Pacijent() {
		super();
	}

	public Pacijent(Long id, String email, String sifra, String ime, String prezime) {
		super(id, email, sifra, ime, prezime);
	}

	public ZdravstveniKarton getZdravstveniKarton() {
		return zdravstveniKarton;
	}

	public void setZdravstveniKarton(ZdravstveniKarton zdravstveniKarton) {
		this.zdravstveniKarton = zdravstveniKarton;
	}
	
	
}
