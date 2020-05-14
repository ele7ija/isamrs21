package isamrs.tim21.klinika.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isamrs.tim21.klinika.domain.ZahtevZaRegistraciju;

@Repository
public interface ZahtevZaRegistracijuRepository extends JpaRepository<ZahtevZaRegistraciju, Long> {
  
  
}