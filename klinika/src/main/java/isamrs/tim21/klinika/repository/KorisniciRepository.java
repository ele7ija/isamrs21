package isamrs.tim21.klinika.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import isamrs.tim21.klinika.domain.AdministratorCentra;
import isamrs.tim21.klinika.domain.AdministratorKlinike;
import isamrs.tim21.klinika.domain.Korisnik;

@Repository
public interface KorisniciRepository extends JpaRepository<Korisnik, Long> {
	
	Korisnik findByEmail(String email);

	@Query("SELECT k from Korisnik k "
			+ "WHERE k.id = :id "
			+ "AND k.sifra = :sifra")
	Korisnik findByIdAndBySifra(@Param("id") Long id, @Param("sifra") String sifra);


	@Query("SELECT k from Korisnik k"
			+  " WHERE TYPE(k)='AK'" )
	List<AdministratorKlinike> findAllAdminiKlinike();

	@Query("SELECT k from Korisnik k"
			+  " WHERE TYPE(k)='AC'" )
	List<AdministratorCentra> findAllAdminiCentra();
	
}
