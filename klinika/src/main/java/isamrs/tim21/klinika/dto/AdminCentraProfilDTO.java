package isamrs.tim21.klinika.dto;

import isamrs.tim21.klinika.domain.AdministratorCentra;

public class AdminCentraProfilDTO {
  private String poslednjaSifra;
  private AdministratorCentra admin;

  public AdminCentraProfilDTO() {}
	
	public String getPoslednjaSifra() {
		return poslednjaSifra;
	}
	public void setPoslednjaSifra(String poslednjaSifra) {
		this.poslednjaSifra = poslednjaSifra;
  }
  
  public AdministratorCentra getAdmin() {
    return admin;
  }
  public void setAdmin(AdministratorCentra admin){
    this.admin = admin;
  }
}