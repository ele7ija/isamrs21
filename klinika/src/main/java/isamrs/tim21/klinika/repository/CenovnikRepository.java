package isamrs.tim21.klinika.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import isamrs.tim21.klinika.domain.Cenovnik;

@Repository
public interface CenovnikRepository extends JpaRepository<Cenovnik, Long> {
	@Query("SELECT c FROM Cenovnik c "
			+ "WHERE c.klinika.id = :idKlinike")
	public List<Cenovnik> findAllByIdKlinike(@Param("idKlinike") long idKlinike);
	
	@Query("SELECT c from TipPregleda c "
			+ "WHERE c.klinika.id = :idKlinike "
			+ "AND c.id = :idCenovnika")
	public Cenovnik findByIdKlinikeAndIdCenovnika(@Param("idKlinike") long idKlinike, @Param("idCenovnika") long idCenovnika);
}