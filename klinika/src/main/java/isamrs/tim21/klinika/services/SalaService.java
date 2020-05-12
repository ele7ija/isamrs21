package isamrs.tim21.klinika.services;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.Pregled;
import isamrs.tim21.klinika.domain.Sala;
import isamrs.tim21.klinika.dto.CustomResponse;
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

	@Transactional(readOnly=false)
	public CustomResponse<Boolean> delete(Long idKlinike, Long idSale, Long version) throws Exception{
		if(!pregledRepository.findByIdSale(idSale).isEmpty()){
			return new CustomResponse<Boolean>(true, false, "Greska: Ne mozete obrisati salu za koju postoji pregled");
		}
		Sala sala = salaRepository.findByIdKlinikeAndIdSale(idKlinike, idSale);
		if(sala == null){
			return new CustomResponse<Boolean>(false, false, "Greska: Sala nije pronadjena.");
		}
		Sala salaToDelete = new Sala();
		sala.setId(idSale);
		sala.setVersion(version);
		salaRepository.delete(salaToDelete); //ovde moze da dodje do nepoklapanja verzija usled optimistickog zakljucavanja
		return new CustomResponse<Boolean>(true, true, "OK");
	}

	@Transactional(readOnly=true)
	public List<Sala> getAllSale(Long idKlinike) {
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return null;
		}else{
			List<Sala> retval = salaRepository.findAllByIdKlinike(klinika.getId());
			return retval;
		}
	}

	@Transactional(readOnly=true)
	public Sala getSala(Long idKlinike, Long idSale) {
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return null;
		}else{
			Sala retval = salaRepository.findByIdKlinikeAndIdSale(idKlinike, idSale);
			return retval;
		}
	}

	@Transactional(readOnly=false)
	public ResponseEntity<Sala> addSala(Long idKlinike, Sala salaToAdd) {
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return new ResponseEntity<Sala>(HttpStatus.NOT_FOUND);
		}else{
			salaToAdd.setKlinika(klinika);
			salaToAdd.setId(null);
			salaToAdd.setPregledi(new ArrayList<Pregled>());
			Sala retval = salaRepository.save(salaToAdd);
			return new ResponseEntity<Sala>(retval, HttpStatus.OK);
		}
	}

	@Transactional(readOnly=false)
	public ResponseEntity<CustomResponse<Sala>> update(Long idKlinike, Long idSale, Sala salaToChange) throws Exception{
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return new ResponseEntity<CustomResponse<Sala>>(
					new CustomResponse<Sala>(null, false, "Greska: Klinika nije pronadjena."),
					HttpStatus.NOT_FOUND);
		}else{
			salaToChange.setId(idSale);
			salaToChange.setKlinika(klinika);			
			Sala sala = salaRepository.findByIdKlinikeAndIdSale(idKlinike, idSale);
			if(sala == null){
				return new ResponseEntity<CustomResponse<Sala>>(
						new CustomResponse<Sala>(null, false, "Greska: Sala nije pronadjena."),
						HttpStatus.NOT_FOUND);
			}	
			salaToChange.setPregledi(sala.getPregledi());
			Sala retval = salaRepository.save(salaToChange); //ovde moze da dodje do nepoklapanja verzija
			return new ResponseEntity<CustomResponse<Sala>>(
					new CustomResponse<Sala>(retval, true, "OK."),
					HttpStatus.OK);	
		}
	}

	@Transactional(readOnly=false)
	public ResponseEntity<CustomResponse<Boolean>> deleteMain(Long idKlinike, Long idSale, Long version) throws Exception{
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return new ResponseEntity<CustomResponse<Boolean>>(new CustomResponse<Boolean>(false, false, "Greska. Klinika ne postoji"), HttpStatus.NOT_FOUND);
		}else{
			CustomResponse<Boolean> customResponse = delete(idKlinike, idSale, version);
			return new ResponseEntity<CustomResponse<Boolean>>(customResponse, customResponse.getResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
		}
	}

}
