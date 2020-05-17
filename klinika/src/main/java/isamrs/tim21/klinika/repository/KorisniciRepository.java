package isamrs.tim21.klinika.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import isamrs.tim21.klinika.domain.Korisnik;

public interface KorisniciRepository extends JpaRepository<Korisnik, Long> {
	
	Korisnik findByEmail(String email);

	@Query("SELECT k from Korisnik k "
			+ "WHERE k.id = :id "
			+ "AND k.sifra = :sifra")
	Korisnik findByIdAndBySifra(@Param("id") Long id, @Param("sifra") String sifra);

}
