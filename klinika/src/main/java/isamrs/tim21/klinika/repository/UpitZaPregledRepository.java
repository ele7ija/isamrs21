package isamrs.tim21.klinika.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import isamrs.tim21.klinika.domain.UpitZaPregled;

@Repository
public interface UpitZaPregledRepository extends JpaRepository<UpitZaPregled, Long> {

	@Query("SELECT u FROM UpitZaPregled u "
			+ "WHERE u.odobren = false")
	List<UpitZaPregled> findNeodobreni();
	
	@Query("SELECT u FROM UpitZaPregled u "
			+ "WHERE u.odobren = false AND "
			+ "u.adminObradio = false AND "
			+ "u.pacijent.email = :email")
	List<UpitZaPregled> findNeodobreniNeodradjeniByEmail(@Param("email") String email);

	@Query("SELECT u FROM UpitZaPregled u "
			+ "WHERE u.odobren = false AND "
			+ "u.adminObradio = true AND "
			+ "u.pacijentObradio = false AND "
			+ "u.pacijent.email = :email")
	List<UpitZaPregled> findNeodobreniOdradjeniByEmail(@Param("email") String email);

	@Query("SELECT u FROM UpitZaPregled u "
			+ "WHERE u.potvrdjen = false AND "
			+ "u.odobren = true AND "
			+ "u.pacijentObradio = false")
	List<UpitZaPregled> findNepotvrdjeni();

	@Query("SELECT u FROM UpitZaPregled u "
			+ "WHERE u.potvrdjen = false AND "
			+ "u.odobren = true AND "
			+ "u.pacijent.email = :email AND "
			+ "u.pacijentObradio = false")
	List<UpitZaPregled> findNepotvrdjeniByEmail(@Param("email") String email);
			
	@Query("SELECT u FROM UpitZaPregled u "
			+ "WHERE u.klinika.id = :idKlinike")
	List<UpitZaPregled> findAllByIdKlinike(@Param("idKlinike") long idKlinike);
	
	@Query("SELECT u FROM UpitZaPregled u "
			+ "WHERE u.unapredDefinisaniPregled.id = :idPregleda")
	List<UpitZaPregled> findAllByIdPregleda(@Param("idPregleda") long idPregleda);
	
	@Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
	@Query("SELECT u FROM UpitZaPregled u "
			+ "WHERE u.klinika.id = :idKlinike "
			+ "AND u.id = :idUpita")
	UpitZaPregled findByIdKlinikeAndByIdPessimisticForceIncrement(@Param("idKlinike") long idKlinike, @Param("idUpita") long idUpita);

	@Lock(LockModeType.PESSIMISTIC_READ)
	@Query("SELECT u FROM UpitZaPregled u "
			+ "WHERE u.klinika.id = :idKlinike "
			+ "AND u.id = :idUpita")
	UpitZaPregled findByIdKlinikeAndByIdPessimisticRead(Long id, Long id2);

}
