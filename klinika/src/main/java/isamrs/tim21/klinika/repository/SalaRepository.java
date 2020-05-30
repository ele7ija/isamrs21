package isamrs.tim21.klinika.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import isamrs.tim21.klinika.domain.Sala;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long>{
	
	@Query("SELECT s FROM Sala s "
			+ "WHERE s.klinika.id = :idKlinike")
	List<Sala> findAllByIdKlinike(@Param("idKlinike") Long idKlinike);
	
	@Query("SELECT s from Sala s "
			+ "WHERE s.klinika.id = :idKlinike "
			+ "AND s.id = :idSale")
	public Sala findByIdKlinikeAndIdSale(@Param("idKlinike") long idKlinike, @Param("idSale") long idSale);

	@Lock(LockModeType.PESSIMISTIC_READ)
	@Query("SELECT s from Sala s "
			+ "WHERE s.klinika.id = :idKlinike "
			+ "AND s.id = :idSale")
	public Sala findByIdKlinikeAndIdSalePessimisticRead(@Param("idKlinike") long idKlinike, @Param("idSale") long idSale);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT s from Sala s "
			+ "WHERE s.klinika.id = :idKlinike "
			+ "AND s.id = :idSale")
	public Sala findByIdKlinikeAndIdSalePessimisticWrite(@Param("idKlinike") long idKlinike, @Param("idSale") long idSale);
}
