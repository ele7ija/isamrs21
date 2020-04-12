package isamrs.tim21.klinika.repository;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.stereotype.Repository;

import isamrs.tim21.klinika.domain.Pacijent;


@Repository
public class InMemoryPacijentRepository {
	private Set<Pacijent> pacijenti;

	public InMemoryPacijentRepository() {
		pacijenti = new HashSet<Pacijent>();
		pacijenti.add(new Pacijent(0L, "jane@doe.com", "janedoe", "Jane", "Doe"));
		pacijenti.add(new Pacijent(1L, "john@doe.com", "johndoe", "John", "Doe"));
	}
	
	public Set<Pacijent> findAll() {
		return pacijenti;
	}

	public void setKlinike(Set<Pacijent> pacijenti) {
		this.pacijenti = pacijenti;
	}
	
	
}
