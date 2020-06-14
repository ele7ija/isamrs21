package isamrs.tim21.klinika.services;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.Pregled;
import isamrs.tim21.klinika.domain.Sala;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.exceptions.BusinessLogicException;
import isamrs.tim21.klinika.exceptions.EntityNotFoundException;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.PregledRepository;
import isamrs.tim21.klinika.repository.SalaRepository;

@Service
public class SalaService {
	@Autowired
	SalaRepository salaRepository;
	
	@Autowired
	PregledRepository pregledRepository;
	
	@Autowired
	private KlinikaRepository klinikaRepository;

	@Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED)
	public CustomResponse<Boolean> delete(Long idKlinike, Long idSale, Long version)
		throws EntityNotFoundException, BusinessLogicException{
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null)
			throw new EntityNotFoundException("Klinika");
		
		//dobavi salu u pessimistic write rezimu kako bi sprecio metode za dodavanje pregleda sa ovom salom da se izvrsavaju konkurentno sa ovom metodom
		Sala sala = salaRepository.findByIdKlinikeAndIdSalePessimisticWrite(idKlinike, idSale);
		if(sala == null)
			throw new EntityNotFoundException("Sala");
		
			//kako je sala zakljucana u pessimistic write rezimu, mozemo mu rucno porediti verzije
		if(!version.equals(sala.getVersion()))
			throw new BusinessLogicException("Greška. Vaš podatak ima zastarelu verziju. Osvežite stranicu.");
		
		if(!pregledRepository.findByIdSale(idSale).isEmpty())
			throw new BusinessLogicException("Greška: Ne možete obrisati salu za koju postoji pregled");
	
		salaRepository.delete(sala); //ovde moze da dodje do nepoklapanja verzija usled optimistickog zakljucavanja
		return new CustomResponse<Boolean>(true, true, "OK");
	}

	@Transactional(readOnly=true, isolation = Isolation.READ_COMMITTED)
	public List<Sala> getAllSale(Long idKlinike) throws EntityNotFoundException{
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null)
			throw new EntityNotFoundException("Klinika");
		else
			return salaRepository.findAllByIdKlinike(klinika.getId());
	}

	@Transactional(readOnly=false, isolation = Isolation.READ_COMMITTED)
	public ResponseEntity<Sala> addSala(Long idKlinike, Sala salaToAdd) throws EntityNotFoundException{
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null)
			throw new EntityNotFoundException("Klinika");
		else{
			salaToAdd.setKlinika(klinika);
			salaToAdd.setId(null);
			salaToAdd.setPregledi(new ArrayList<Pregled>());
			Sala retval = salaRepository.save(salaToAdd);
			return new ResponseEntity<Sala>(retval, HttpStatus.OK);
		}
	}

	@Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED)
	public ResponseEntity<CustomResponse<Sala>> update(Long idKlinike, Long idSale, Sala salaToChange) throws EntityNotFoundException{
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null)
			throw new EntityNotFoundException("Klinika");
		else{
			salaToChange.setId(idSale);
			salaToChange.setKlinika(klinika);

			//kako bi sprecili konkurentno brisanje pregleda da ide sa ovom metodom
			//jer linija 102 moze da settuje preglede sali, koje zatim metoda za brisanja pregleda izbrise
			//a mi zatim u liniji 104 opet dodamo te preglede
			Sala sala = salaRepository.findByIdKlinikeAndIdSalePessimisticWrite(idKlinike, idSale);
			if(sala == null)
				throw new EntityNotFoundException("Sala");

			salaToChange.setPregledi(sala.getPregledi());

			Sala retval = salaRepository.save(salaToChange); 
			return new ResponseEntity<CustomResponse<Sala>>(
					new CustomResponse<Sala>(retval, true, "OK."),
					HttpStatus.OK);	
		}
	}
}
