package isamrs.tim21.klinika.repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Repository;

import isamrs.tim21.klinika.domain.TipPregleda;

@Repository
public class InMemoryTipoviPregledaRepository {
	private Set<TipPregleda> tipoviPregleda = new HashSet<TipPregleda>();

	public Set<TipPregleda> getTipoviPregleda() {
		return tipoviPregleda;
	}

	public void setTipoviPregleda(Set<TipPregleda> tipoviPregleda) {
		this.tipoviPregleda = tipoviPregleda;
	}

	public List<TipPregleda> findByIdKlinike(Long idKlinike) {
		ArrayList<TipPregleda> retval = new ArrayList<TipPregleda>();
		Iterator<TipPregleda> iterator = tipoviPregleda.iterator();
		while(iterator.hasNext()){
			TipPregleda temp = iterator.next();
			if(temp.getIdKlinike().equals(idKlinike)){
				retval.add(temp);
			}
		}
		return retval;
	}

	public TipPregleda findByIdKlinikeAndIdTipaPregleda(Long idKlinike, Long idTipaPregleda) {
		Iterator<TipPregleda> iterator = tipoviPregleda.iterator();
		while(iterator.hasNext()){
			TipPregleda temp = iterator.next();
			if(temp.getIdKlinike().equals(idKlinike) && temp.getId().equals(idTipaPregleda)){
				return temp;
			}
		}
		return null;
	}

	public void addTipPregleda(TipPregleda tipPregledaToAdd) {
		tipoviPregleda.add(tipPregledaToAdd); //ne zamaramo se validacijom id-ja jer ce o tome voditi racuna hibernate i mySQL kasnije
	}

	public TipPregleda updateTipPregleda(Long idTipaPregleda, TipPregleda tipPregledaToChange) {
		Iterator<TipPregleda> iterator = tipoviPregleda.iterator();
		while(iterator.hasNext()){
			TipPregleda temp = iterator.next();
			if(temp.getId().equals(idTipaPregleda)){
				temp.setNaziv(tipPregledaToChange.getNaziv());
				temp.setOpis(tipPregledaToChange.getOpis());
				return temp;
			}
		}
		return null;
	}

	public Boolean deleteTipPregleda(Long idTipaPregleda) {
		Iterator<TipPregleda> iterator = tipoviPregleda.iterator();
		while(iterator.hasNext()){
			TipPregleda temp = iterator.next();
			if(temp.getId().equals(idTipaPregleda)){
				iterator.remove();
				return true;
			}
		}
		return false;
	}
	
}
