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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.jsonSerialize.IdentitySerializable;
import isamrs.tim21.klinika.jsonSerialize.IdentitySerializer;

@Entity
@Table(name="zahtev_za_godisnji")
public class ZahtevZaGodisnji implements IdentitySerializable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JsonSerialize(using=IdentitySerializer.class)
	private RadniKalendar radniKalendar;
	
	@Column(name="odobreno")
	private boolean odobreno;
	
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
	
	
}
