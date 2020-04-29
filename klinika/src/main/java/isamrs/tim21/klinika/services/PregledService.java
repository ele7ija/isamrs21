package isamrs.tim21.klinika.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.Pregled;
import isamrs.tim21.klinika.repository.PregledRepository;

@Service
public class PregledService {

	@Autowired
	PregledRepository pregledRepository;

	public List<Pregled> findAll(Long idKlinike) {
		return pregledRepository.findAllByIdKlinike(idKlinike);
	}

	public Pregled find(Long idKlinike, Long idPregleda) {
		return pregledRepository.findByIdKlinikeAndIdPregleda(idKlinike, idPregleda); 
	}

	public Pregled add(Klinika klinika, Pregled pregled) {
		pregled.setId(null);
		pregled.setKlinika(klinika);
		/* GOMILA VALIDACIJA:
		 * Da li je trazena sala zauzeta?
		 * Da li je lekar slobodan u to vreme?
		 * Da li je lekar specijalizovan za dati tip pregleda?
		 * Da li su svi entiteti unutar iste klinike kao i pregled koji se dodaje?
		 */
		return pregledRepository.save(pregled);
	}

	public Pregled update(Klinika klinika, Pregled pregled, Long idPregleda) {
		/* GOMILA VALIDACIJA:
		 * Da li je trazena sala zauzeta?
		 * Da li je lekar slobodan u to vreme?
		 * Da li je lekar specijalizovan za dati tip pregleda?
		 * Da li su svi entiteti unutar iste klinike kao i pregled koji se dodaje?
		 */
		
		pregled.setId(idPregleda);
		if(!pregledRepository.findById(idPregleda).isPresent()){
			return null;
		}
		pregled.setKlinika(klinika);
		return pregledRepository.save(pregled);
	}
	
	@Transactional
	public Boolean delete(Long idKlinike, Long idPregleda) {
		int numberOfRemovals = pregledRepository.deleteByIdAndKlinikaId(idKlinike, idPregleda);
		System.out.println(numberOfRemovals);
		return numberOfRemovals == 1;
	}
	
	
}
