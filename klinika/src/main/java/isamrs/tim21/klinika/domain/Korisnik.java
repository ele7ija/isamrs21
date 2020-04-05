package isamrs.tim21.klinika.domain;

public abstract class Korisnik {
	protected Long id;
	protected String email;
	protected String sifra;
	protected String ime;
	protected String prezime;
	protected Long idZahtevaZaReg;
	
	public Korisnik(){}

	public Korisnik(Long id, String email, String sifra, String ime, String prezime, Long idZahtevaZaReg) {
		super();
		this.id = id;
		this.email = email;
		this.sifra = sifra;
		this.ime = ime;
		this.prezime = prezime;
		this.idZahtevaZaReg = idZahtevaZaReg;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public Long getIdZahtevaZaReg() {
		return idZahtevaZaReg;
	}

	public void setIdZahtevaZaReg(Long idZahtevaZaReg) {
		this.idZahtevaZaReg = idZahtevaZaReg;
	}
	
	
}
