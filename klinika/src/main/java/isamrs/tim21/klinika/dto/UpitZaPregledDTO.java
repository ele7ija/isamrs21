package isamrs.tim21.klinika.dto;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.TipPregleda;
import isamrs.tim21.klinika.jsonSerialize.IdentitySerializable;
import isamrs.tim21.klinika.jsonSerialize.IdentitySerializer;

public class UpitZaPregledDTO {
	private Boolean odobren;
	
	private Boolean potvrdjen;

	//@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
	private Date pocetakPregleda;
	//@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
	private Date krajPregleda;
	private Date datumKreiranjaUpita;
	private Long tipPregleda;
	private Long lekar;
	private Long klinika;
	private String pacijent;
	private Long pregled;
	public Boolean getOdobren() {
		return odobren;
	}
	public void setOdobren(Boolean odobren) {
		this.odobren = odobren;
	}
	public Boolean getPotvrdjen() {
		return potvrdjen;
	}
	public void setPotvrdjen(Boolean potvrdjen) {
		this.potvrdjen = potvrdjen;
	}
	public Date getPocetakPregleda() {
		return pocetakPregleda;
	}
	public void setPocetakPregleda(Date pocetakPregleda) {
		this.pocetakPregleda = pocetakPregleda;
	}
	public Date getKrajPregleda() {
		return krajPregleda;
	}
	public void setKrajPregleda(Date krajPregleda) {
		this.krajPregleda = krajPregleda;
	}
	public Long getTipPregleda() {
		return tipPregleda;
	}
	public void setTipPregleda(Long tipPregleda) {
		this.tipPregleda = tipPregleda;
	}
	public Long getLekar() {
		return lekar;
	}
	public void setLekar(Long lekar) {
		this.lekar = lekar;
	}
	public Long getKlinika() {
		return klinika;
	}
	public void setKlinika(Long klinika) {
		this.klinika = klinika;
	}
	public String getPacijent() {
		return pacijent;
	}
	public void setPacijent(String pacijent) {
		this.pacijent = pacijent;
	}
	public Long getPregled() {
		return pregled;
	}
	public void setPregled(Long pregled) {
		this.pregled = pregled;
	}
	public Date getDatumKreiranjaUpita() {
		return datumKreiranjaUpita;
	}
	public void setDatumKreiranjaUpita(Date datumKreiranjaUpita) {
		this.datumKreiranjaUpita = datumKreiranjaUpita;
	}
	
	

	
}
