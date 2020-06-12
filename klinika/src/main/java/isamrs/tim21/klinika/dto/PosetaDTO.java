package isamrs.tim21.klinika.dto;

public class PosetaDTO {
	private Long pregledId;
	private String opis;
	
	public PosetaDTO(){}

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
	
	
}
