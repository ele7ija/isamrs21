package isamrs.tim21.klinika.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import isamrs.tim21.klinika.domain.ZahtevZaGodisnji;

public interface ZahtevZaGodisnjiRepository extends JpaRepository<ZahtevZaGodisnji, Long> {

	@Query("SELECT z FROM ZahtevZaGodisnji z "
			+ "WHERE z.radniKalendar.medicinskoOsoblje.klinika.id = :idKlinike")
	List<ZahtevZaGodisnji> findAllByIdKlinike(@Param("idKlinike") Long idKlinike);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT z FROM ZahtevZaGodisnji z "
			+ "WHERE z.id = :idZahtevaZaGodisnji")
	ZahtevZaGodisnji findByIdPessimisticWrite(@Param("idZahtevaZaGodisnji") Long idZahtevaZaGodisnji);

	@Query("SELECT z FROM ZahtevZaGodisnji z "
			+ "WHERE z.radniKalendar.medicinskoOsoblje.id = :idOsoblja "
			+ "AND z.adminObradio = false")
	List<ZahtevZaGodisnji> findAllPoslatiByIdOsoblja(@Param("idOsoblja") Long idOsoblja);
	
	@Query("SELECT z FROM ZahtevZaGodisnji z "
			+ "WHERE z.radniKalendar.medicinskoOsoblje.id = :idOsoblja "
			+ "AND z.adminObradio = true "
			+ "AND z.osobljeObradilo = false")
	List<ZahtevZaGodisnji> findAllNeobradjeniByIdOsoblja(@Param("idOsoblja") Long idOsoblja);

	@Lock(LockModeType.PESSIMISTIC_READ)
	@Query("SELECT z FROM ZahtevZaGodisnji z "
			+ "WHERE z.id = :idZahtevaZaGodisnji")
	ZahtevZaGodisnji findByIdPessimissticRead(@Param("idZahtevaZaGodisnji") Long idZahtevaZaGodisnji);

}
