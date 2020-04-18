package isamrs.tim21.klinika.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isamrs.tim21.klinika.domain.Pacijent;

@Repository
public interface PacijentRepository extends JpaRepository<Pacijent, Long> {

	Pacijent findByEmail(String email);
	
}
