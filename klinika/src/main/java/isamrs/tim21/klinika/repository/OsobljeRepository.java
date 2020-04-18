package isamrs.tim21.klinika.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import isamrs.tim21.klinika.domain.MedicinskoOsoblje;

@Repository
public interface OsobljeRepository extends JpaRepository<MedicinskoOsoblje, Long> {

	@Query("SELECT k from Korisnik k "
			+ "WHERE TYPE(k)=MS OR TYPE(k)=LE "
			+ "AND k.klinika.id= :idKlinike")
	List<MedicinskoOsoblje> findAllByIdKlinike(@Param("idKlinike") Long idKlinike);

	@Query("SELECT k from Korisnik k "
			+ "WHERE TYPE(k)=MS OR TYPE(k)=LE "
			+ "AND k.klinika.id= :idKlinike "
			+ "AND k.id= :idOsoblja")
	MedicinskoOsoblje findByIdKlinikeAndById(@Param("idKlinike") Long idKlinike, @Param("idOsoblja") Long idOsoblja);

}
