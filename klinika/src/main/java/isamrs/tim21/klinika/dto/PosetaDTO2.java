package isamrs.tim21.klinika.dto;

public class PosetaDTO2 {
	private Long pregledId;
	private String opis;
	private String email;
	
	public PosetaDTO2(){
		//default constructor
	}

	public Long getPregledId() {
		return pregledId;
	}
	public void setPregledId(Long pregledId) {
		this.pregledId = pregledId;
	}
	public String getOpis() {
		return opis;
	}
	public void setOpis(String opis) {
		this.opis = opis;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
