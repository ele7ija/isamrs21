package isamrs.tim21.klinika.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import isamrs.tim21.klinika.domain.Klinika;

@Repository
public interface KlinikaRepository extends JpaRepository<Klinika, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT k from Klinika k "
			+ "WHERE k.id = :idKlinike")
	Klinika findByIdPessimisticWrite(@Param("idKlinike") long idKlinike);
}
