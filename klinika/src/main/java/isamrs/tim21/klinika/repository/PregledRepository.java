package isamrs.tim21.klinika.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import isamrs.tim21.klinika.domain.Pregled;

@Repository
public interface PregledRepository extends JpaRepository<Pregled, Long> {
	@Query("SELECT p FROM Pregled p "
			+ "WHERE p.klinika.id = :idKlinike")
	List<Pregled> findAllByIdKlinike(@Param("idKlinike") Long idKlinike);

	@Query("SELECT p FROM Pregled p "
			+ "WHERE p.klinika.id = :idKlinike "
			+ "AND p.id = :idPregleda")
	Pregled findByIdKlinikeAndIdPregleda(@Param("idKlinike") Long idKlinike, @Param("idPregleda") Long idPregleda);

	@Modifying
	@Query("DELETE from Pregled p "
			+ "WHERE p.klinika.id = :idKlinike "
			+ "AND p.id = :idPregleda")
	int deleteByIdAndKlinikaId(@Param("idKlinike") Long idKlinike, @Param("idPregleda") Long idPregleda);
}