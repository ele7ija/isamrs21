package isamrs.tim21.klinika.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.Cenovnik;
import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.TipPregleda;
import isamrs.tim21.klinika.dto.CustomResponse;
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

	@Transactional(readOnly=false)
	public CustomResponse<Boolean> delete(Long idKlinike, Long idTipaPregleda) {
		if(!pregledRepository.findByIdTipaPregleda(idTipaPregleda).isEmpty()){
			return new CustomResponse<Boolean>(false, false, "Greska: Ne mozete obrisati tip pregleda za koji postoji pregled");
		}
		int rowsAffected = tipPregledaRepository._deleteById(idKlinike, idTipaPregleda);
		if(rowsAffected != 1){
			return new CustomResponse<Boolean>(false, false, "Greska: Tip pregleda nije pronadjen.");
		}
		return new CustomResponse<Boolean>(true, true, "OK");
	}

	@Transactional(readOnly=true)
	public List<TipPregleda> getAllTipoviPregleda(Long idKlinike) {
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return null;
		}else{
			return tipPregledaRepository.findAllByIdKlinike(klinika.getId());
		}
	}
	
	@Transactional(readOnly=true)
	public TipPregleda getTipPRegleda(Long idKlinike, Long idTipaPregleda) {
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return null;
		}else{
			return tipPregledaRepository.findByIdKlinikeAndIdTipaPregleda(idKlinike, idTipaPregleda);
		}
	}

	@Transactional(readOnly=false)
	public ResponseEntity<TipPregleda> add(Long idKlinike, TipPregleda tipPregledaToAdd) {
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<TipPregleda>(HttpStatus.NOT_FOUND);
		}else{
			tipPregledaToAdd.setKlinika(klinika);
			tipPregledaToAdd.setId(null);
			ArrayList<Lekar> lekari = new ArrayList<Lekar>();
			tipPregledaToAdd.setLekari(lekari);
			Cenovnik cenovnik = cenovnikRepository.findById(tipPregledaToAdd.getCenovnik().getId()).orElse(null);
			if(cenovnik == null){
				return new ResponseEntity<TipPregleda>(HttpStatus.NOT_FOUND);
			}
			cenovnik.getTipoviPregleda().add(tipPregledaToAdd);
			tipPregledaToAdd.setCenovnik(cenovnik);
			TipPregleda retval = tipPregledaRepository.save(tipPregledaToAdd);
			return new ResponseEntity<TipPregleda>(retval, HttpStatus.OK);
		}
	}

	@Transactional(readOnly=false)
	public ResponseEntity<CustomResponse<TipPregleda>> update(Long idKlinike, Long idTipaPregleda, TipPregleda tipPregledaToChange)
			throws ObjectOptimisticLockingFailureException{
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<CustomResponse<TipPregleda>>(
					new CustomResponse<TipPregleda>(null, false, "Greska: Klinika nije pronadjena"),
					HttpStatus.OK);
		}else{
			tipPregledaToChange.setId(idTipaPregleda);
			tipPregledaToChange.setKlinika(klinika);
			
			TipPregleda tipPregleda = tipPregledaRepository.findById(idTipaPregleda).orElse(null);
			if(tipPregleda == null){
				return new ResponseEntity<CustomResponse<TipPregleda>>(
						new CustomResponse<TipPregleda>(null, false, "Greska: Tip pregleda nije pronadjen"),
						HttpStatus.OK);
			}
			tipPregledaToChange.setLekari(tipPregleda.getLekari()); //ovo se ne menja kroz api call
			TipPregleda retval = tipPregledaRepository.save(tipPregledaToChange); //ovde moze da dodje do nepoklapanja verzija
			return new ResponseEntity<CustomResponse<TipPregleda>>(
					new CustomResponse<TipPregleda>(retval, true, "OK."),
					HttpStatus.OK);		
		}
	}

	@Transactional(readOnly=false)
	public ResponseEntity<CustomResponse<Boolean>> deleteMain(Long idKlinike, Long idTipaPregleda) {
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<CustomResponse<Boolean>>(new CustomResponse<Boolean>(false, false, "Greska. Klinika ne postoji"), HttpStatus.NOT_FOUND);
		}else{
			CustomResponse<Boolean> customResponse = delete(idKlinike, idTipaPregleda);
			return new ResponseEntity<CustomResponse<Boolean>>(customResponse, HttpStatus.OK);
		}
	}
	
}
