package isamrs.tim21.klinika.domain;

public class Pacijent extends Korisnik{
	private Long idZdravstvenogKartona;

	public Pacijent() {
		super();
	}

	public Pacijent(Long id, String email, String sifra, String ime, String prezime, Long idZahtevaZaReg,
			Long idZdravstvenogKartona) {
		super(id, email, sifra, ime, prezime, idZahtevaZaReg);
		this.idZdravstvenogKartona = idZdravstvenogKartona;
	}

	public Long getIdZdravstvenogKartona() {
		return idZdravstvenogKartona;
	}

	public void setIdZdravstvenogKartona(Long idZdravstvenogKartona) {
		this.idZdravstvenogKartona = idZdravstvenogKartona;
	} 
	
	
}
