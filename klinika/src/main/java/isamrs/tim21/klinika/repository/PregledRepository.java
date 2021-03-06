package isamrs.tim21.klinika.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import isamrs.tim21.klinika.domain.Klinika;
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

	@Query("SELECT p FROM Pregled p "
			+ "WHERE p.klinika.id = :idKlinike "
			+ "AND p.poseta = null")
	List<Pregled> findAllSlobodniByIdKlinike(@Param("idKlinike") Long idKlinike);
	
	List<Pregled> findByKlinikaAndPosetaIsNull(Klinika k);

	@Query("SELECT p FROM Pregled p "
	+ "WHERE p.tipPregleda.id = :idTipaPregleda")
	List<Pregled> findByIdTipaPregleda(@Param("idTipaPregleda") Long idTipaPregleda);
	
	@Query("SELECT p FROM Pregled p "
			+ "WHERE p.sala.id = :idSale")
	List<Pregled> findByIdSale(@Param("idSale") Long idSale);
	
	@Query("SELECT p FROM Pregled p "
			+ "WHERE p.lekar.id = :idLekara")
	List<Pregled> findByIdLekara(@Param("idLekara") Long idLekara);
	
	@Query("SELECT p FROM Pregled p "
			+ "WHERE p.lekar.id = :idLekara "
			+ "AND p.tipPregleda.id = :idTipaPregleda")
	List<Pregled> findByIdLekaraAndIdTipaPregleda(@Param("idLekara") Long idLekara, @Param("idTipaPregleda") Long idTipaPregleda);

	@Query("SELECT p FROM Pregled p "
			+ "INNER JOIN p.dodatniLekari lekari "
			+ "WHERE lekari.id = :idLekara")
	List<Pregled> findByIdDodatnogLekara(@Param("idLekara") Long idLekara);

	@Lock(LockModeType.PESSIMISTIC_READ)
	@Query("SELECT p FROM Pregled p "
			+ "WHERE p.klinika.id = :idKlinike "
			+ "AND p.id = :idPregleda")
	Pregled findByIdKlinikeAndIdPregledaPessimisticRead(@Param("idKlinike") Long idKlinike, @Param("idPregleda") Long idPregleda);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Pregled p "
			+ "WHERE p.klinika.id = :idKlinike "
			+ "AND p.id = :idPregleda")
	Pregled findByIdKlinikeAndIdPregledaPessimisticWrite(@Param("idKlinike") Long idKlinike, @Param("idPregleda") Long idPregleda);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Pregled p "
			+ "WHERE p.id = :id")
	Pregled findByIdPessimisticWrite(@Param("id") Long id);
}
