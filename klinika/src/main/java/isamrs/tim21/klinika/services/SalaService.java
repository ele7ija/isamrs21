package isamrs.tim21.klinika.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.repository.PregledRepository;
import isamrs.tim21.klinika.repository.SalaRepository;

@Service
public class SalaService {
	@Autowired
	SalaRepository salaRepository;
	
	@Autowired
	PregledRepository pregledRepository;

	@Transactional
	public CustomResponse<Boolean> delete(Long idKlinike, Long idSale) {
		if(!pregledRepository.findByIdSale(idSale).isEmpty()){
			return new CustomResponse<Boolean>(false, false, "Greska: Ne mozete obrisati salu za koju postoji pregled");
		}
		int rowsAffected = salaRepository._deleteById(idKlinike, idSale);
		if(rowsAffected != 1){
			return new CustomResponse<Boolean>(false, false, "Greska: Sala nije pronadjena.");
		}
		return new CustomResponse<Boolean>(true, true, "OK");
	}

}
