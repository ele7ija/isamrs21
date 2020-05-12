package isamrs.tim21.klinika.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.Cenovnik;
import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.TipPregleda;
import isamrs.tim21.klinika.dto.CustomResponse;
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
	
	@Transactional(readOnly=true)
	public List<Cenovnik> findAllByIdKlinike(Long idKlinike){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return null;
		}else{
			return cenovnikRepository.findAllByIdKlinike(klinika.getId());
		}
	}
	
	@Transactional(readOnly=true)
	public Cenovnik findByIdKlinike(Long idKlinike, Long idCenovnika){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return null;
		}else{
			return cenovnikRepository.findByIdKlinikeAndIdCenovnika(idKlinike, idCenovnika);
		}
	}

	@Transactional(readOnly=false)
	public Cenovnik add(Long idKlinike, Cenovnik cenovnikToAdd) {
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return null;
		}else{
			//POSTAVI JOS JEDNOM SVE PARAMETRE NA BEKU
			cenovnikToAdd.setKlinika(klinika);
			cenovnikToAdd.setId(null);
			cenovnikToAdd.setTipoviPregleda(new ArrayList<TipPregleda>());
			cenovnikToAdd.setDatumKreiranja(new Date());
			return cenovnikRepository.save(cenovnikToAdd);
		}
	}

	@Transactional(readOnly=false)
	public ResponseEntity<CustomResponse<Cenovnik>> update(Long idKlinike, Long idCenovnika,
			Cenovnik cenovnikToChange) throws Exception{ //baca exception usled optimistickog zakljucavanja koje cemo kasnije implementirati
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return new ResponseEntity<CustomResponse<Cenovnik>>(new CustomResponse<Cenovnik>(null, false, "Greska: Klinika nije pronadjena."), HttpStatus.NOT_FOUND);
		}else{
			cenovnikToChange.setId(idCenovnika);
			cenovnikToChange.setKlinika(klinika);
			Cenovnik cenovnik = cenovnikRepository.findByIdKlinikeAndIdCenovnika(idKlinike, idCenovnika);
			if(cenovnik == null){
				return new ResponseEntity<CustomResponse<Cenovnik>>(
						new CustomResponse<Cenovnik>(null, false, "Greska: Cenovnik nije pronadjen u datoj klinici."), HttpStatus.NOT_FOUND);
			}
			cenovnikToChange.setTipoviPregleda(cenovnik.getTipoviPregleda());
			cenovnik = cenovnikRepository.save(cenovnikToChange); //ovde moze da dodje do nepoklapanja verzija
			return new ResponseEntity<CustomResponse<Cenovnik>>(
					new CustomResponse<Cenovnik>(cenovnik, true, "OK."), HttpStatus.OK);
		}
	}

	@Transactional(readOnly=false)
	public ResponseEntity<CustomResponse<Boolean>> delete(Long idKlinike, Long idCenovnika, Long version)
			throws Exception { //baca exception usled optimistickog zakljucavanja koje cemo kasnije implementirati
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return new ResponseEntity<CustomResponse<Boolean>>(HttpStatus.NOT_FOUND);
		}else{
			if(tipPregledaRepository.findByIdCenovnika(idCenovnika).size() != 0){
				return new ResponseEntity<CustomResponse<Boolean>>(
						new CustomResponse<Boolean>(true, false, "Greska. Ne mozete obrisati cenovnik za koji postoje definisani tipovi pregleda"),
						HttpStatus.OK);
			}
			Cenovnik cenovnik = cenovnikRepository.findById(idCenovnika).orElse(null);
			if(cenovnik == null){
				return new ResponseEntity<CustomResponse<Boolean>>(
						new CustomResponse<Boolean>(false, false, "Greska. Cenovnik nije pronadjen."),
						HttpStatus.NOT_FOUND);
			}
			Cenovnik cenovnikToDelete = new Cenovnik();
			cenovnikToDelete.setId(idCenovnika);
			cenovnikToDelete.setVersion(version);

			cenovnikRepository.delete(cenovnikToDelete);
			return new ResponseEntity<CustomResponse<Boolean>>(
					new CustomResponse<Boolean>(true, true, "OK."),
					HttpStatus.OK);
		}
	}
}
