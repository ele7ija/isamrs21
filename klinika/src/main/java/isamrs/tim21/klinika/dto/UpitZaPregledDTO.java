package isamrs.tim21.klinika.dto;

import java.util.Date;

public class UpitZaPregledDTO {
	private Boolean odobren;
	
	private Boolean potvrdjen;

	//@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
	private Date pocetakPregleda;
	//@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
	private Date krajPregleda;
	
	private Long tipPregleda;
	private Long lekar;
	private Long klinika;
	private String pacijent;
	private Long pregled;

	private Long pregledVerzija;
	private Long tipPregledaVerzija;
	private Long lekarVerzija;

	public Long getPregledVerzija() {
		return this.pregledVerzija;
	}

	public void setPregledVerzija(Long pregledVerzija) {
		this.pregledVerzija = pregledVerzija;
	}

	public Long getTipPregledaVerzija() {
		return this.tipPregledaVerzija;
	}

	public void setTipPregledaVerzija(Long tipPregledaVerzija) {
		this.tipPregledaVerzija = tipPregledaVerzija;
	}

	public Long getLekarVerzija() {
		return this.lekarVerzija;
	}

	public void setLekarVerzija(Long lekarVerzija) {
		this.lekarVerzija = lekarVerzija;
	}

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
	
	

	
}
