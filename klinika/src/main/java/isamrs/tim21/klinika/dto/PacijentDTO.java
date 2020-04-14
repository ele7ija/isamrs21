package isamrs.tim21.klinika.dto;

public class PacijentDTO {
	public String email;
	public String sifra;
	public String ime;
	public String prezime;
	public String drzava;
	public String grad;
	public String adresa;
	public String brojTelefona;
	public String jbo;
	
	public PacijentDTO() {
	}
	
	public PacijentDTO(String email, String sifra, String ime, String prezime, String drzava, String grad,
			String adresa, String brojTelefona, String jbo) {
		super();
		this.email = email;
		this.sifra = sifra;
		this.ime = ime;
		this.prezime = prezime;
		this.drzava = drzava;
		this.grad = grad;
		this.adresa = adresa;
		this.brojTelefona = brojTelefona;
		this.jbo = jbo;
	}
}
