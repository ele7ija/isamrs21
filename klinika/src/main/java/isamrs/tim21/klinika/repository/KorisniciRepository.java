package isamrs.tim21.klinika.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import isamrs.tim21.klinika.domain.Korisnik;

public interface KorisniciRepository extends JpaRepository<Korisnik, Long> {

	Korisnik findByEmail(String email);

}
