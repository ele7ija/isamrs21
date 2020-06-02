package isamrs.tim21.klinika.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import isamrs.tim21.klinika.domain.Ocena;

public interface OcenaRepository extends JpaRepository<Ocena, Long>{
    
}