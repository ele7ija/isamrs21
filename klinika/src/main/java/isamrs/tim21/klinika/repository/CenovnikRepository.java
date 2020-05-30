package isamrs.tim21.klinika.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import isamrs.tim21.klinika.domain.Cenovnik;

@Repository
public interface CenovnikRepository extends JpaRepository<Cenovnik, Long> {
	@Query("SELECT c FROM Cenovnik c "
			+ "WHERE c.klinika.id = :idKlinike")
	public List<Cenovnik> findAllByIdKlinike(@Param("idKlinike") long idKlinike);
	
	@Query("SELECT c from Cenovnik c "
			+ "WHERE c.klinika.id = :idKlinike "
			+ "AND c.id = :idCenovnika")
	public Cenovnik findByIdKlinikeAndIdCenovnika(@Param("idKlinike") long idKlinike, @Param("idCenovnika") long idCenovnika);

	@Lock(LockModeType.PESSIMISTIC_READ)
	@Query("SELECT c from Cenovnik c "
			+ "WHERE c.klinika.id = :idKlinike "
			+ "AND c.id = :idCenovnika")
	public Cenovnik findByIdKlinikeAndIdCenovnikaPessimisticRead(@Param("idKlinike") long idKlinike, @Param("idCenovnika") long idCenovnika);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT c from Cenovnik c "
			+ "WHERE c.klinika.id = :idKlinike "
			+ "AND c.id = :idCenovnika")
	public Cenovnik findByIdKlinikeAndIdCenovnikaPessimisticWrite(@Param("idKlinike") long idKlinike, @Param("idCenovnika") long idCenovnika);
}
