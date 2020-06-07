package isamrs.tim21.klinika.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import isamrs.tim21.klinika.domain.Recept;

public interface ReceptRepository extends JpaRepository<Recept, Long>  {
  
}