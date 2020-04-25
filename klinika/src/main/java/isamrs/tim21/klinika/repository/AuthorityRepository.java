package isamrs.tim21.klinika.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isamrs.tim21.klinika.domain.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	Authority findByName(String name);

}
