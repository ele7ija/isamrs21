package isamrs.tim21.klinika.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import isamrs.tim21.klinika.jsonSerialize.IdentitySerializable;

@Entity
@Table(name="zahtev_za_godisnji")
public class ZahtevZaGodisnji implements IdentitySerializable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Version
	@Column(name="version", columnDefinition="integer DEFAULT 0", nullable=false)
	private Long version;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JsonIgnoreProperties(value={"zahteviZaGodisnjiOdmor"}, allowSetters=true)
	private RadniKalendar radniKalendar;
	
	@Column(name="odobreno")
	private boolean odobreno;
	
	@Column(name="admin_obradio")
	private boolean adminObradio;
	
	@Column(name="osoblje_obradilo")
	private boolean osobljeObradilo;
	
	@Column(name="razlog_odbijanja")
	private String razlogOdbijanja;
	
	@Temporal(TemporalType.DATE)
	private Date prviDanGodisnjeg;
	
	@Temporal(TemporalType.DATE)
	private Date poslednjiDanGodisnjeg;

	public RadniKalendar getRadniKalendar() {
		return radniKalendar;
	}

	public void setRadniKalendar(RadniKalendar radniKalendar) {
		this.radniKalendar = radniKalendar;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isOdobreno() {
		return odobreno;
	}

	public void setOdobreno(boolean odobreno) {
		this.odobreno = odobreno;
	}

	public Date getPrviDanGodisnjeg() {
		return prviDanGodisnjeg;
	}

	public void setPrviDanGodisnjeg(Date prviDanGodisnjeg) {
		this.prviDanGodisnjeg = prviDanGodisnjeg;
	}

	public Date getPoslednjiDanGodisnjeg() {
		return poslednjiDanGodisnjeg;
	}

	public void setPoslednjiDanGodisnjeg(Date poslednjiDanGodisnjeg) {
		this.poslednjiDanGodisnjeg = poslednjiDanGodisnjeg;
	}

	public String getRazlogOdbijanja() {
		return razlogOdbijanja;
	}

	public void setRazlogOdbijanja(String razlogOdbijanja) {
		this.razlogOdbijanja = razlogOdbijanja;
	}

	public boolean intersects(ZahtevZaGodisnji zahtevToAdd) {
		if((prviDanGodisnjeg.after(zahtevToAdd.getPrviDanGodisnjeg()) || prviDanGodisnjeg.equals(zahtevToAdd.getPrviDanGodisnjeg())) && 
				prviDanGodisnjeg.before(zahtevToAdd.getPoslednjiDanGodisnjeg()) || prviDanGodisnjeg.equals(zahtevToAdd.getPoslednjiDanGodisnjeg()))
			return true;
		if((zahtevToAdd.getPrviDanGodisnjeg().after(prviDanGodisnjeg) || zahtevToAdd.getPrviDanGodisnjeg().equals(prviDanGodisnjeg)) && 
				zahtevToAdd.getPrviDanGodisnjeg().before(poslednjiDanGodisnjeg) || zahtevToAdd.getPrviDanGodisnjeg().equals(poslednjiDanGodisnjeg))
			return true;
		return false;
	}

	public boolean isOsobljeObradilo() {
		return osobljeObradilo;
	}

	public void setOsobljeObradilo(boolean osobljeObradilo) {
		this.osobljeObradilo = osobljeObradilo;
	}
	
	public boolean isAdminObradio() {
		return adminObradio;
	}

	public void setAdminObradio(boolean adminObradio) {
		this.adminObradio = adminObradio;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
