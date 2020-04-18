package isamrs.tim21.klinika.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isamrs.tim21.klinika.domain.Klinika;

@Repository
public interface KlinikaRepository extends JpaRepository<Klinika, Long> {
	
}
