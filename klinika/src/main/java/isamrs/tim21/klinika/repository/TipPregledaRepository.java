package isamrs.tim21.klinika.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import isamrs.tim21.klinika.domain.TipPregleda;

@Repository
public interface TipPregledaRepository extends JpaRepository<TipPregleda, Long> {
	@Query("SELECT t FROM TipPregleda t "
			+ "WHERE t.klinika.id = :idKlinike")
	public List<TipPregleda> findAllByIdKlinike(@Param("idKlinike") long idKlinike);
	
	@Query("SELECT t from TipPregleda t "
			+ "WHERE t.klinika.id = :idKlinike "
			+ "AND t.id = :idTipaPregleda")
	public TipPregleda findByIdKlinikeAndIdTipaPregleda(@Param("idKlinike") long idKlinike, @Param("idTipaPregleda") long idTipaPregleda);

	@Lock(LockModeType.PESSIMISTIC_READ)
	@Query("SELECT t from TipPregleda t "
			+ "WHERE t.klinika.id = :idKlinike "
			+ "AND t.id = :idTipaPregleda")
	public TipPregleda findByIdKlinikeAndIdPessimisticRead(@Param("idKlinike") long idKlinike, @Param("idTipaPregleda") long idTipaPregleda);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT t from TipPregleda t "
			+ "WHERE t.klinika.id = :idKlinike "
			+ "AND t.id = :idTipaPregleda")
	public TipPregleda findByIdKlinikeAndIdPessimisticWrite(@Param("idKlinike") long idKlinike, @Param("idTipaPregleda") long idTipaPregleda);

	@Query("SELECT t FROM TipPregleda t "
			+ "WHERE t.cenovnik.id = :idCenovnika")
	public List<TipPregleda> findByIdCenovnika(@Param("idCenovnika") Long idCenovnika);

	@Modifying
	@Query("DELETE from TipPregleda t "
			+ "WHERE t.klinika.id = :idKlinike "
			+ "AND t.id = :idTipaPregleda")
	public int _deleteById(@Param("idKlinike") long idKlinike, @Param("idTipaPregleda") long idTipaPregleda);
}
