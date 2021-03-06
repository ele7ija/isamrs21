package isamrs.tim21.klinika.dto;

public class KlinikaDTO {
  public String naziv;
  public String adresa;
  public String grad;
  public String drzava;
  public String opis;
	public Double geografskaDuzina;
	public Double geografskaSirina;

  public KlinikaDTO(){}


  public KlinikaDTO(String naziv, String adresa, String grad, String drzava, String opis) {
    this.naziv = naziv;
    this.adresa = adresa;
    this.grad = grad;
    this.drzava = drzava;
    this.opis = opis;
  }

  public String getNaziv() {
    return this.naziv;
  }

  public String getAdresa() {
    return this.adresa;
  }

  public String getGrad() {
    return this.grad;
  }

  public String getDrzava() {
    return this.drzava;
  }

  public String getOpis() {
    return this.opis;
  }

  public Double getGeografskaDuzina() {
		return geografskaDuzina;
	}

	public void setGeografskaDuzina(Double geografskaDuzina) {
		this.geografskaDuzina = geografskaDuzina;
	}

	public Double getGeografskaSirina() {
		return geografskaSirina;
	}

	public void setGeografskaSirina(Double geografskaSirina) {
		this.geografskaSirina = geografskaSirina;
	}
}