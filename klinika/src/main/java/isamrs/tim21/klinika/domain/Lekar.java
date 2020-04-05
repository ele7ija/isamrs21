package isamrs.tim21.klinika.domain;

import java.util.List;

public class Lekar extends MedicinskoOsoblje{
	private List<Long> idTipovaPregleda;

	public List<Long> getIdTipovaPregleda() {
		return idTipovaPregleda;
	}

	public void setIdTipovaPregleda(List<Long> idTipovaPregleda) {
		this.idTipovaPregleda = idTipovaPregleda;
	}
}
