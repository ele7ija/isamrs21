package isamrs.tim21.klinika.dto;

import isamrs.tim21.klinika.domain.AdministratorKlinike;;

public class AdminKlinikeProfilDTO {
	private String poslednjaSifra;
	private AdministratorKlinike adminKlinike;
	
	public AdminKlinikeProfilDTO(){}
	
	public String getPoslednjaSifra() {
		return poslednjaSifra;
	}
	public void setPoslednjaSifra(String poslednjaSifra) {
		this.poslednjaSifra = poslednjaSifra;
	}
	public AdministratorKlinike getAdminKlinike() {
		return adminKlinike;
	}
	public void setAdminKlinike(AdministratorKlinike adminKlinike) {
		this.adminKlinike = adminKlinike;
	}
}
