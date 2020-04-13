package isamrs.tim21.klinika.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import isamrs.tim21.klinika.domain.Sala;

public interface SalaRepository extends JpaRepository<Sala, Long>{
	
	@Query("SELECT s FROM Sala s "
			+ "WHERE s.klinika.id = :idKlinike")
	List<Sala> findAllByIdKlinike(@Param("idKlinike") Long idKlinike);
	
	@Query("SELECT s from Sala s "
			+ "WHERE s.klinika.id = :idKlinike "
			+ "AND s.id = :idSale")
	public Sala findByIdKlinikeAndIdSale(@Param("idKlinike") long idKlinike, @Param("idSale") long isSale);

}
