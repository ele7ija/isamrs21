package isamrs.tim21.klinika.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.MedicinskaSestra;
import isamrs.tim21.klinika.domain.MedicinskoOsoblje;
import isamrs.tim21.klinika.domain.Pregled;
import isamrs.tim21.klinika.domain.Sala;
import isamrs.tim21.klinika.domain.UpitZaPregled;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.OsobljeRepository;
import isamrs.tim21.klinika.repository.PosetaRepository;
import isamrs.tim21.klinika.repository.PregledRepository;
import isamrs.tim21.klinika.repository.SalaRepository;
import isamrs.tim21.klinika.repository.TipPregledaRepository;

@Service
public class PregledService {

	@Autowired
	PregledRepository pregledRepository;
	
	@Autowired
	SalaRepository salaRepository;
	
	@Autowired
	TipPregledaRepository tipPregledaRepository;
	
	@Autowired
	OsobljeRepository osobljeRepository;
	
	@Autowired
	PosetaRepository posetaRepository;
	
	@Autowired
	KlinikaRepository klinikaRepository;

	public List<Pregled> findAll(Long idKlinike) {
		return pregledRepository.findAllByIdKlinike(idKlinike);
	}

	public Pregled find(Long idKlinike, Long idPregleda) {
		return pregledRepository.findByIdKlinikeAndIdPregleda(idKlinike, idPregleda); 
	}

	public CustomResponse<Pregled> add(Klinika klinika, Pregled pregled) {
		pregled.setId(null);
		pregled.setKlinika(klinika);
		return validateAll(klinika, pregled);
		
	}

	public CustomResponse<Pregled> update(Klinika klinika, Pregled pregled, Long idPregleda) {
		pregled.setId(idPregleda);
		
		/* VALIDACIJE: */		
		/* Da li trazeni pregled postoji*/
		if(pregledRepository.findByIdKlinikeAndIdPregleda(klinika.getId(), idPregleda) == null){
			return new CustomResponse<Pregled>(null, false,
					"Greska: Trazeni pregled nije pronadjen u klinici");
		}
		pregled.setKlinika(klinika);
		return validateAll(klinika, pregled);
		
	}
	
	@Transactional
	public CustomResponse<Boolean> delete(Long idKlinike, Long idPregleda) {
		if(posetaRepository.findById(idPregleda).isPresent()){
			return new CustomResponse<Boolean>(false, false, "Greska: Pregled je rezervisan. Ne mozete ga obrisati.");
		}
		int numberOfRemovals = pregledRepository.deleteByIdAndKlinikaId(idKlinike, idPregleda);
		if(numberOfRemovals == 1){
			return new CustomResponse<Boolean>(true, true, "OK.");
		}
		return new CustomResponse<Boolean>(false, false, "Greska: Pregled nije pronadjen");
	}
	
	private CustomResponse<Pregled> validateAll(Klinika klinika, Pregled pregled) {
		/* VALIDACIJE: */
		/* Da li su svi entiteti unutar iste klinike kao i pregled koji se dodaje? ODRADJENO UNUTAR OSTALIH METODA*/

		/* Da li je trazena sala zauzeta i da li uopste postoji? */
		if(!validateSala(klinika, pregled)){
			return new CustomResponse<Pregled>(null, false,
					"Greska: Trazena sala nije slobodna za uneti vremenski interval trajanja pregleda");
		}
		
		/* Da li je lekar slobodan u to vreme? */
		/* Da li je lekar specijalizovan za dati tip pregleda? */
		if(!validateOsoblje(klinika, pregled)){
			return new CustomResponse<Pregled>(null, false,
					"Greska: Lekar ne moze da izvrsi ovaj pregled. "
					+ "Proverite da li je lekar zauzet u datom vremenskom intervalu, kao i da li je uopste specijalizovan za dati tip pregleda.");
		}
		
		return new CustomResponse<Pregled>(pregledRepository.save(pregled), true, "OK"); 
	}
	
	private boolean validateSala(Klinika klinika, Pregled pregled) {
		Sala sala = salaRepository.findByIdKlinikeAndIdSale(klinika.getId(), pregled.getSala().getId());
		if(sala == null){
			return false;
		}
		for(Pregled p : sala.getPregledi()){
			if(p.getId() == pregled.getId())
				continue;
			if(p.intersects(pregled)){
				return false;
			}
		}
		return true;
	}

	private boolean validateOsoblje(Klinika klinika, Pregled pregled) {
		MedicinskoOsoblje osoblje = osobljeRepository.findByIdKlinikeAndById(klinika.getId(), pregled.getLekar().getId());
		if(osoblje == null || osoblje instanceof MedicinskaSestra)
			return false;
		Lekar lekar = (Lekar) osoblje;
		for(Pregled p : lekar.getPregledi()){
			if(p.getId() == pregled.getId())
				continue;
			if(p.intersects(pregled)){
				return false;
			}
		}
		if(!lekar.getTipovi_pregleda().contains(pregled.getTipPregleda()))
			return false;
		return true;
	}

	public List<Pregled> findSlobodni(Long idKlinike) {
		Klinika k = klinikaRepository.getOne(idKlinike);
		List<Pregled> retval = pregledRepository.findByKlinikaAndPosetaIsNull(k);
		List<Pregled> r = new ArrayList<Pregled>();
		for (Pregled p : retval) {
			boolean flag = false;
			for (UpitZaPregled u : p.getUpiti()) {
				if (u.getOdobren()==true) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				r.add(p);
			}
		}
		return r;
	}
}
