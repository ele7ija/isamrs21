package isamrs.tim21.klinika.repository;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.stereotype.Repository;

import isamrs.tim21.klinika.domain.Klinika;

@Repository
public class InMemoryKlinikaRepository {
	private Set<Klinika> klinike = new HashSet<Klinika>();

	public Set<Klinika> getKlinike() {
		return klinike;
	}

	public void setKlinike(Set<Klinika> klinike) {
		this.klinike = klinike;
	}
	
	public Klinika findById(Long id){
		Iterator<Klinika> iterator = klinike.iterator();
		while(iterator.hasNext()){
			Klinika temp = iterator.next();
			if(temp.getId().equals(id)){
				return temp;
			}
		}
		// return null;
		return new Klinika(); // za sada ovako kako bi radilo administriranje tipova pregleda
	}
	

}
