package isamrs.tim21.klinika.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import isamrs.tim21.klinika.domain.Klinika;

public interface KlinikaRepository extends JpaRepository<Klinika, Long> {
	
}
