package isamrs.tim21.klinika.domain;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="zahtev_za_registraciju")
public class ZahtevZaRegistraciju {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="datumPodnosenja")
	private Timestamp datumPodnosenja;
	
	@Column(name="datumOdobrenja")
	private Timestamp datumOdobrenja;
	
	@OneToOne(cascade=CascadeType.ALL)
	@MapsId
	private Pacijent pacijent;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private AdministratorCentra adminOdobrio;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getDatumPodnosenja() {
		return datumPodnosenja;
	}

	public void setDatumPodnosenja(Timestamp datumPodnosenja) {
		this.datumPodnosenja = datumPodnosenja;
	}

	public Timestamp getDatumOdobrenja() {
		return datumOdobrenja;
	}

	public void setDatumOdobrenja(Timestamp datumOdobrenja) {
		this.datumOdobrenja = datumOdobrenja;
	}

	public Pacijent getPacijent() {
		return pacijent;
	}

	public void setPacijent(Pacijent pacijent) {
		this.pacijent = pacijent;
	}

	public AdministratorCentra getAdminOdobrio() {
		return adminOdobrio;
	}

	public void setAdminOdobrio(AdministratorCentra adminOdobrio) {
		this.adminOdobrio = adminOdobrio;
	}
	
	
}
