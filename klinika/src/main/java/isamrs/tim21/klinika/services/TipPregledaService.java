package isamrs.tim21.klinika.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.Cenovnik;
import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.TipPregleda;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.exceptions.BusinessLogicException;
import isamrs.tim21.klinika.exceptions.EntityNotFoundException;
import isamrs.tim21.klinika.repository.CenovnikRepository;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.PregledRepository;
import isamrs.tim21.klinika.repository.TipPregledaRepository;

@Service
public class TipPregledaService {

	@Autowired
	TipPregledaRepository tipPregledaRepository;
	
	@Autowired
	KlinikaRepository klinikaRepository;
	
	@Autowired
	PregledRepository pregledRepository;
	
	@Autowired
	CenovnikRepository cenovnikRepository;

	@Transactional(readOnly=false, isolation = Isolation.READ_COMMITTED)
	public ResponseEntity<CustomResponse<Boolean>> delete(Long idKlinike, Long idTipaPregleda, Long version)
		throws EntityNotFoundException, BusinessLogicException{

		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null)
			throw new EntityNotFoundException("Klinika");
		
		//dobavi pessimistic write nad tipom pregleda - ovo ce spreciti konkurentne pokusaje dodavanja pregleda
		TipPregleda tipPregleda = tipPregledaRepository.findByIdKlinikeAndIdPessimisticWrite(idKlinike, idTipaPregleda);
		if(tipPregleda == null)
			throw new EntityNotFoundException("Tip pregleda");

		//kako je tip pregleda zakljucan u pessimistic write rezimu, mozemo mu rucno porediti verzije
		if(!version.equals(tipPregleda.getVersion()))
			throw new BusinessLogicException("Greška. Vaš podatak ima zastarelu verziju. Osvežite stranicu.");

		if(!pregledRepository.findByIdTipaPregleda(idTipaPregleda).isEmpty())
			throw new BusinessLogicException("Greška: Ne možete obrisati tip pregleda za koji postoji pregled");
		
		if(tipPregleda.getLekari().size() != 0)
		throw new BusinessLogicException("Greška: Ne možete obrisati tip pregleda za koji postoji specijalizacija lekara.");

		tipPregledaRepository.delete(tipPregleda);
		return new ResponseEntity<CustomResponse<Boolean>>(
			new CustomResponse<Boolean>(true, true, "OK"),
			HttpStatus.OK);
	}

	@Transactional(readOnly=true, isolation=Isolation.READ_COMMITTED)
	public List<TipPregleda> getAllTipoviPregleda(Long idKlinike) throws EntityNotFoundException{
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			throw new EntityNotFoundException("Klinika");
		}else{
			return tipPregledaRepository.findAllByIdKlinike(klinika.getId());
		}
	}

	@Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED)
	public ResponseEntity<TipPregleda> add(Long idKlinike, TipPregleda tipPregledaToAdd) throws EntityNotFoundException{
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null)
			throw new EntityNotFoundException("Klinika");
		else{
			tipPregledaToAdd.setKlinika(klinika);
			tipPregledaToAdd.setId(null);
			tipPregledaToAdd.setLekari(new ArrayList<Lekar>());

			//pessimistic read kako bi uveli shared lock nad cenovnikom i sprecili konkurentno brisanje cenovnika
			Cenovnik cenovnik = cenovnikRepository.findByIdKlinikeAndIdCenovnikaPessimisticRead(idKlinike, tipPregledaToAdd.getCenovnik().getId());
			
			if(cenovnik == null){
				throw new EntityNotFoundException("Cenovnik");
			}
			cenovnik.getTipoviPregleda().add(tipPregledaToAdd);
			tipPregledaToAdd.setCenovnik(cenovnik);
			return new ResponseEntity<TipPregleda>(tipPregledaRepository.save(tipPregledaToAdd), HttpStatus.OK);
		}
	}

	@Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED)
	public ResponseEntity<CustomResponse<TipPregleda>> update(Long idKlinike, Long idTipaPregleda, TipPregleda tipPregledaToChange)
			throws EntityNotFoundException{
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null)
			throw new EntityNotFoundException("Klinika");
		else{
			tipPregledaToChange.setId(idTipaPregleda);
			tipPregledaToChange.setKlinika(klinika);
			
			//pessimistic read kako bi uveli shared lock nad cenovnikom i sprecili konkurentno brisanje cenovnika
			Cenovnik cenovnik = cenovnikRepository.findByIdKlinikeAndIdCenovnikaPessimisticRead(
					idKlinike, tipPregledaToChange.getCenovnik().getId());
			tipPregledaToChange.setCenovnik(cenovnik);
			
			//pessimistic write kako bi sprecili brisanje specijalnosti lekara da ide paraleleno sa ovom metodom
			TipPregleda tipPregleda = tipPregledaRepository.findByIdKlinikeAndIdPessimisticWrite(idKlinike, idTipaPregleda);
			if(tipPregleda == null)
				throw new EntityNotFoundException("Tip pregleda");
			
			tipPregledaToChange.setLekari(tipPregleda.getLekari());
			
			TipPregleda retval = tipPregledaRepository.save(tipPregledaToChange);
			return new ResponseEntity<CustomResponse<TipPregleda>>(
					new CustomResponse<TipPregleda>(retval, true, "OK."),
					HttpStatus.OK);		
		}
	}
	
}
