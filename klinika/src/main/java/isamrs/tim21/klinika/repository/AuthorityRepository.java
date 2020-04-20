package isamrs.tim21.klinika.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import isamrs.tim21.klinika.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	Authority findByName(String name);
}
