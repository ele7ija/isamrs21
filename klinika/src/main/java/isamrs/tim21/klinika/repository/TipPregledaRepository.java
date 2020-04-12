package isamrs.tim21.klinika.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import isamrs.tim21.klinika.domain.TipPregleda;

public interface TipPregledaRepository extends JpaRepository<TipPregleda, Long> {
	@Query("SELECT t FROM TipPregleda t "
			+ "WHERE t.klinika.id = :idKlinike")
	public List<TipPregleda> findAllByIdKlinike(@Param("idKlinike") long idKlinike);
	
	@Query("SELECT t from TipPregleda t "
			+ "WHERE t.klinika.id = :idKlinike "
			+ "AND t.id = :idTipaPregleda")
	public TipPregleda findByIdKlinikeAndIdTipaPregleda(@Param("idKlinike") long idKlinike, @Param("idTipaPregleda") long idTipaPregleda);
}
