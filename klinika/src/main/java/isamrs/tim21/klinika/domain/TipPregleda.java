package isamrs.tim21.klinika.domain;

import java.util.List;

public class TipPregleda {
	private Long id;
	private String naziv;
	private String opis;
	private Long idKlinike;
	private List<Long> idLekaraSpecijalista;

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public TipPregleda(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdKlinike() {
		return idKlinike;
	}

	public void setIdKlinike(Long idKlinike) {
		this.idKlinike = idKlinike;
	}

	public List<Long> getIdLekaraSpecijalista() {
		return idLekaraSpecijalista;
	}

	public void setIdLekaraSpecijalista(List<Long> idLekaraSpecijalista) {
		this.idLekaraSpecijalista = idLekaraSpecijalista;
	}
	
	
}
