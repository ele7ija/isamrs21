package isamrs.tim21.klinika.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="poseta")
public class Poseta {
	
	@Id
	private Long id;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private ZdravstveniKarton zdravstveniKarton;
	
	@OneToOne(fetch=FetchType.LAZY)
	@MapsId //znaci da je id pregleda u tabeli poseta i primarni i strani kljuc
	private Pregled pregled;
	
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

	
	
}
