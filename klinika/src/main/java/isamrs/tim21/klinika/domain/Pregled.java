package isamrs.tim21.klinika.domain;

public class Pregled {
	private Long id;
	private Long idTipaPregleda;
	private Long idSale;
	private Long idLekara;
	private Long idPosete;
	
	public Pregled(){}
	
	public Pregled(Long id, Long idTipaPregleda, Long idSale, Long idLekara, Long idPosete) {
		super();
		this.id = id;
		this.idTipaPregleda = idTipaPregleda;
		this.idSale = idSale;
		this.idLekara = idLekara;
		this.idPosete = idPosete;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdTipaPregleda() {
		return idTipaPregleda;
	}
	public void setIdTipaPregleda(Long idTipaPregleda) {
		this.idTipaPregleda = idTipaPregleda;
	}
	public Long getIdSale() {
		return idSale;
	}
	public void setIdSale(Long idSale) {
		this.idSale = idSale;
	}
	public Long getIdLekara() {
		return idLekara;
	}
	public void setIdLekara(Long idLekara) {
		this.idLekara = idLekara;
	}
	public Long getIdPosete() {
		return idPosete;
	}
	public void setIdPosete(Long idPosete) {
		this.idPosete = idPosete;
	}
	
	
}
