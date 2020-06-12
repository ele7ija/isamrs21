package isamrs.tim21.klinika.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import isamrs.tim21.klinika.domain.Pacijent;

@Repository
public interface PacijentRepository extends JpaRepository<Pacijent, Long> {

	Pacijent findByEmail(String email);
	
	@Lock(LockModeType.PESSIMISTIC_READ)
	@Query("SELECT k from Korisnik k "
			+ "WHERE TYPE(k)='PA' "
			+ "AND k.id = :idPacijenta")
	Pacijent findByIdPacijentaPessimisticRead(@Param("idPacijenta") long idPacijenta);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT k from Korisnik k "
			+ "WHERE TYPE(k)='PA' "
			+ "AND k.id = :idPacijenta")
	Pacijent findByIdPacijentaPessimisticWrite(@Param("idPacijenta") long idPacijenta);
	
}
