package isamrs.tim21.klinika.dto;

import isamrs.tim21.klinika.domain.Lekar;

public class LekarProfilDTO {
	private String poslednjaSifra;
	private Lekar lekar;
	
	public LekarProfilDTO(){}
	
	public String getPoslednjaSifra() {
		return poslednjaSifra;
	}
	public void setPoslednjaSifra(String poslednjaSifra) {
		this.poslednjaSifra = poslednjaSifra;
	}
	public Lekar getLekar() {
		return lekar;
	}
	public void setLekar(Lekar lekar) {
		this.lekar = lekar;
	}
	
	
}
