package isamrs.tim21.klinika.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import isamrs.tim21.klinika.domain.UpitZaPregled;

@Repository
public interface UpitZaPregledRepository extends JpaRepository<UpitZaPregled, Long> {

	@Query("SELECT u FROM UpitZaPregled u "
			+ "WHERE u.odobren = false")
	List<UpitZaPregled> findNeodobreni();

	@Query("SELECT u FROM UpitZaPregled u "
			+ "WHERE u.potvrdjen = false")
	List<UpitZaPregled> findNepotvrdjeni();

}
