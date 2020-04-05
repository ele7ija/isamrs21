package isamrs.tim21.klinika.domain;

import java.util.List;

public class Klinika extends MedicinskaUstanova{
	private List<Long> idSala;
	private List<Long> idTipovaPregleda;
	private List<Long> idPregleda;
	private List<Long> idMedicinskogOsoblja;
	private List<Long> idAdministratoraKlinike;
	
	public Klinika(){}

	public List<Long> getIdSala() {
		return idSala;
	}

	public void setIdSala(List<Long> idSala) {
		this.idSala = idSala;
	}

	public List<Long> getIdTipovaPregleda() {
		return idTipovaPregleda;
	}

	public void setIdTipovaPregleda(List<Long> idTipovaPregleda) {
		this.idTipovaPregleda = idTipovaPregleda;
	}

	public List<Long> getIdPregleda() {
		return idPregleda;
	}

	public void setIdPregleda(List<Long> idPregleda) {
		this.idPregleda = idPregleda;
	}

	public List<Long> getIdMedicinskogOsoblja() {
		return idMedicinskogOsoblja;
	}

	public void setIdMedicinskogOsoblja(List<Long> idMedicinskogOsoblja) {
		this.idMedicinskogOsoblja = idMedicinskogOsoblja;
	}

	public List<Long> getIdAdministratoraKlinike() {
		return idAdministratoraKlinike;
	}

	public void setIdAdministratoraKlinike(List<Long> idAdministratoraKlinike) {
		this.idAdministratoraKlinike = idAdministratoraKlinike;
	}
	
	
}
