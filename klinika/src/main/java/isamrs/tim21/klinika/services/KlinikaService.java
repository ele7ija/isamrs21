package isamrs.tim21.klinika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.repository.KlinikaRepository;

@Service

public class KlinikaService {
	@Autowired
	private KlinikaRepository klinikaRepository;
	
	private List<Klinika> findAll() {
		return klinikaRepository.findAll();
	}
}
