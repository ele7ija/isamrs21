package isamrs.tim21.klinika.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.repository.PregledRepository;
import isamrs.tim21.klinika.repository.TipPregledaRepository;

@Service
public class TipPregledaService {

	@Autowired
	TipPregledaRepository tipPregledaRepository;
	
	@Autowired
	PregledRepository pregledRepository;

	@Transactional
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
	
	
}
