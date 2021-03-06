package isamrs.tim21.klinika.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import isamrs.tim21.klinika.jsonSerialize.IdentitySerializable;

@Entity
@Table(name="zahtev_za_registraciju")
public class ZahtevZaRegistraciju implements IdentitySerializable{
	@Version
	@Column(name="version", columnDefinition="integer DEFAULT 0", nullable=false)
	private Long version;
	
	@Id
	private Long id;
	
	@Column(name="datum_podnosenja")
	private Timestamp datumPodnosenja;
	
	@Column(name="datum_odobrenja")
	private Timestamp datumOdobrenja;
	
	@Cascade(CascadeType.MERGE)
	@OneToOne(fetch=FetchType.EAGER)
	@MapsId
	private Pacijent pacijent;
	
	@Column(name="odobren")
	private Boolean odobren;

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

	public Boolean getOdobren() {
		return odobren;
	}

	public void setOdobren(Boolean odobren) {
		this.odobren = odobren;
	}
	
}
