package isamrs.tim21.klinika.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.MedicinskoOsoblje;

@Repository
public interface OsobljeRepository extends JpaRepository<MedicinskoOsoblje, Long> {

	@Query("SELECT k from Korisnik k "
			+ "WHERE TYPE(k)='MS' OR TYPE(k)='LE' "
			+ "AND k.klinika.id= :idKlinike")
	List<MedicinskoOsoblje> findAllByIdKlinike(@Param("idKlinike") Long idKlinike);

	@Query("SELECT k from Korisnik k "
			+ "WHERE TYPE(k)='LE' "
			+ "AND k.klinika.id= :idKlinike")
	List<Lekar> findAllLekariByIdKlinike(@Param("idKlinike") Long idKlinike);

	@Query("SELECT k from Korisnik k "
			+ "WHERE (TYPE(k)='MS' OR TYPE(k)='LE') "
			+ "AND k.klinika.id= :idKlinike "
			+ "AND k.id= :idOsoblja")
	MedicinskoOsoblje findByIdKlinikeAndById(@Param("idKlinike") Long idKlinike, @Param("idOsoblja") Long idOsoblja);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT k from Korisnik k "
			+ "WHERE (TYPE(k)='MS' OR TYPE(k)='LE') "
			+ "AND k.klinika.id= :idKlinike "
			+ "AND k.id= :idOsoblja")
	MedicinskoOsoblje findByIdKlinikeAndByIdPessimisticWrite(@Param("idKlinike") Long idKlinike, @Param("idOsoblja") Long idOsoblja);

	@Lock(LockModeType.PESSIMISTIC_READ)
	@Query("SELECT k FROM Korisnik k "
			+ "WHERE TYPE(k)='LE' "
			+ "AND k.klinika.id = :idKlinike "
			+ "AND k.id = :idOsoblja")
	Lekar findLekarByIdKlinikeAndByIdPessimisticRead(@Param("idKlinike") Long idKlinike, @Param("idOsoblja") Long idOsoblja);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT k FROM Korisnik k "
			+ "WHERE TYPE(k)='LE' "
			+ "AND k.id = :idOsoblja")
	Lekar findLekarByIdPessimisticWrite(@Param("idOsoblja") Long idOsoblja);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT k FROM Korisnik k "
			+ "WHERE (TYPE(k)='LE' OR TYPE(k)='MS') "
			+ "AND k.id = :idOsoblja")
	MedicinskoOsoblje findByIdPessimisticWrite(@Param("idOsoblja") Long idOsoblja);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT k FROM Korisnik k "
			+ "WHERE TYPE(k)='LE' "
			+ "AND k.klinika.id = :idKlinike "
			+ "AND k.id = :idOsoblja")
	Lekar findLekarByIdKlinikeAndByIdPessimisticWrite(@Param("idKlinike") Long idKlinike, @Param("idOsoblja") Long idOsoblja);


	@Lock(LockModeType.PESSIMISTIC_READ)
	@Query("SELECT k FROM Korisnik k "
			+ "WHERE k.id = :idOsoblja")
	MedicinskoOsoblje findByIdPessimisticReadLockExtended(@Param("idOsoblja") Long idOsoblja);

	@Lock(LockModeType.PESSIMISTIC_READ)
	@Query("SELECT k FROM Korisnik k "
			+ "WHERE k.id = :idOsoblja")
	MedicinskoOsoblje findByIdPessimisticRead(@Param("idOsoblja") Long idOsoblja);
}
