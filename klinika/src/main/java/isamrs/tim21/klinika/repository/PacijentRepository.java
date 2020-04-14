package isamrs.tim21.klinika.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import isamrs.tim21.klinika.domain.Pacijent;

public interface PacijentRepository extends JpaRepository<Pacijent, Long> {
	
}
