package isamrs.tim21.klinika.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.Cenovnik;
import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.TipPregleda;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.exceptions.BusinessLogicException;
import isamrs.tim21.klinika.exceptions.EntityNotFoundException;
import isamrs.tim21.klinika.repository.CenovnikRepository;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.TipPregledaRepository;

@Service
public class CenovnikService {
	@Autowired
	KlinikaRepository klinikaRepository;
	
	@Autowired
	CenovnikRepository cenovnikRepository;
	
	@Autowired
	TipPregledaRepository tipPregledaRepository;
	
	@Transactional(readOnly=true, isolation=Isolation.READ_COMMITTED)
	public List<Cenovnik> findAll(){
		return cenovnikRepository.findAll();
	}

	@Transactional(readOnly=true, isolation=Isolation.READ_COMMITTED)
	public List<Cenovnik> findAllByIdKlinike(Long idKlinike) throws EntityNotFoundException{
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			throw new EntityNotFoundException("Klinika");
		}else{
			return cenovnikRepository.findAllByIdKlinike(klinika.getId());
		}
	}

	@Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED)
	public Cenovnik add(Long idKlinike, Cenovnik cenovnikToAdd) throws EntityNotFoundException{
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			throw new EntityNotFoundException("Klinika");
		}else{
			//POSTAVI JOS JEDNOM SVE PARAMETRE NA BEKU
			cenovnikToAdd.setKlinika(klinika);
			cenovnikToAdd.setId(null);
			cenovnikToAdd.setTipoviPregleda(new ArrayList<TipPregleda>());
			cenovnikToAdd.setDatumKreiranja(new Date());
			return cenovnikRepository.save(cenovnikToAdd);
		}
	}

	@Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED)
	public ResponseEntity<CustomResponse<Cenovnik>> update(Long idKlinike, Long idCenovnika,
			Cenovnik cenovnikToChange) throws EntityNotFoundException, ObjectOptimisticLockingFailureException{
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null)
			throw new EntityNotFoundException("Klinika");
		else{
			cenovnikToChange.setId(idCenovnika);
			cenovnikToChange.setKlinika(klinika);
			Cenovnik cenovnik = cenovnikRepository.findByIdKlinikeAndIdCenovnikaPessimisticWrite(idKlinike, idCenovnika);
			if(cenovnik == null)
				throw new EntityNotFoundException("Cenovnik");
			cenovnikToChange.setTipoviPregleda(cenovnik.getTipoviPregleda()); //potencijalni dirty read sprecen sa READ_COMMITTED
			cenovnik = cenovnikRepository.save(cenovnikToChange); //moze doci do nepoklapanja verzije i do OptimisticLockException-a
			return new ResponseEntity<CustomResponse<Cenovnik>>(
					new CustomResponse<Cenovnik>(cenovnik, true, "OK."), HttpStatus.OK);
		}
	}

	@Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED)
	public ResponseEntity<CustomResponse<Boolean>> delete(Long idKlinike, Long idCenovnika, Long version)
			throws EntityNotFoundException, BusinessLogicException {
		/*
				Potrebno je spreciti dodavanje ili izmenu tipa pregleda(izmena u smislu da se novi cenovnik tipa pregleda settuje na ovaj) za dati cenovnik koji se brise
				To se postize tako sto se u ovoj metodi dobavi pessimistic write lock nad cenovnikom koji se brise
				Metode za dodavanje i azuriranje tipa pregleda dobavljaju cenovnik u pessimistic read rezimu
				To ce prakticno spreciti ovu metodu da se izvrsava konkurentno sa metodama za dodavanje i azuriranje tipa pregleda
				koje koriste cenovnik koji se brise
		*/
			
		//get pessimistic write lock
		Cenovnik cenovnik = cenovnikRepository.findByIdKlinikeAndIdCenovnikaPessimisticWrite(idKlinike, idCenovnika);

		if(cenovnik == null){
			throw new EntityNotFoundException("Cenovnik.");
		}

		//kako je cenovnik zakljucan u pessimistic write rezimu, mozemo mu rucno porediti verzije
		if(!version.equals(cenovnik.getVersion()))
			throw new BusinessLogicException("Greska. Vas podatak ima zastarelu verziju. Osvezite stranicu.");

		if(tipPregledaRepository.findByIdCenovnika(idCenovnika).size() != 0){
			throw new BusinessLogicException("Greska. Ne mozete obrisati cenovnik za koji postoje definisani tipovi pregleda");
		}

		cenovnikRepository.delete(cenovnik);
		return new ResponseEntity<CustomResponse<Boolean>>(
				new CustomResponse<Boolean>(true, true, "OK."),
				HttpStatus.OK);
	}
}
