package isamrs.tim21.klinika.dto;

import isamrs.tim21.klinika.domain.MedicinskaSestra;;

public class SestraProfilDTO {
	private String poslednjaSifra;
	private MedicinskaSestra sestra;
	
	public SestraProfilDTO(){}
	
	public String getPoslednjaSifra() {
		return poslednjaSifra;
	}
	public void setPoslednjaSifra(String poslednjaSifra) {
		this.poslednjaSifra = poslednjaSifra;
	}
	public MedicinskaSestra getSestra() {
		return sestra;
	}
	public void setSestra(MedicinskaSestra sestra) {
		this.sestra = sestra;
	}
	
}
