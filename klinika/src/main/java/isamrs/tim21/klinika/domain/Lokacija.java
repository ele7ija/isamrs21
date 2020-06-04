package isamrs.tim21.klinika.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="lokacija")
public class Lokacija {
  @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;
  
  @Column(name="adresa")
  private String adresa;
	
	@Column(name="grad")
	private String grad;
	
	@Column(name="drzava")
	private String drzava;
	
	@Column(name="geografska_duzina")
	private Double geografskaDuzina;

	@Column(name="geografska_sirina")
	private Double geografskaSirina;
  
  public Lokacija(){}

  public Lokacija(String adresa, String grad, String drzava, Double geografskaDuzina, Double geografskaSirina){
    this.adresa = adresa;
    this.grad = grad;
		this.drzava = drzava;
		this.geografskaDuzina = geografskaDuzina;
		this.geografskaSirina = geografskaSirina;
  }

  public Long getId() {
		return id;
  }

	public void setId(Long id) {
		this.id = id;
  }
  
  public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

  public String getGrad() {
		return grad;
	}

	public void setGrad(String grad) {
		this.grad = grad;
	}

	public String getDrzava() {
		return drzava;
	}

	public void setDrzava(String drzava) {
		this.drzava = drzava;
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