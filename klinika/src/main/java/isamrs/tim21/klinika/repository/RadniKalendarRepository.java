package isamrs.tim21.klinika.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import isamrs.tim21.klinika.domain.RadniKalendar;

public interface RadniKalendarRepository extends JpaRepository<RadniKalendar, Long> {

	@Query("SELECT r FROM RadniKalendar r "
			+ "WHERE r.id = :idKalendara ")
	RadniKalendar findByIdPessimisticWrite(@Param("idKalendara") Long idKalendara);

}
