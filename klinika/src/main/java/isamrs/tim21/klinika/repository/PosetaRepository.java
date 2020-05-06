package isamrs.tim21.klinika.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import isamrs.tim21.klinika.domain.Poseta;

@Repository
public interface PosetaRepository extends JpaRepository<Poseta, Long> {

	@Query("SELECT p from Poseta p "
			+ "WHERE p.pregled.id = :idPregleda")
	Poseta findByIdPregleda(@Param("idPregleda") Long idPregleda);

}
