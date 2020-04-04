package isamrs.tim21.klinika.domain;

public class Poseta {
	private Long id;
	private Long idZdravstvenogKartona;
	private Long idPregleda;
	
	public Poseta(){}

	public Poseta(Long id, Long idZdravstvenogKartona, Long idPregleda) {
		super();
		this.id = id;
		this.idZdravstvenogKartona = idZdravstvenogKartona;
		this.idPregleda = idPregleda;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdZdravstvenogKartona() {
		return idZdravstvenogKartona;
	}

	public void setIdZdravstvenogKartona(Long idZdravstvenogKartona) {
		this.idZdravstvenogKartona = idZdravstvenogKartona;
	}

	public Long getIdPregleda() {
		return idPregleda;
	}

	public void setIdPregleda(Long idPregleda) {
		this.idPregleda = idPregleda;
	}
	
	
}
