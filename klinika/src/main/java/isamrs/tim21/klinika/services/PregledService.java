package isamrs.tim21.klinika.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import isamrs.tim21.klinika.repository.UpitZaPregledRepository;

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
	
	@Autowired
	UpitZaPregledRepository upitZaPregledRepository;

	@Transactional(readOnly=false)
	public CustomResponse<Pregled> add(Klinika klinika, Pregled pregled) {
		pregled.setId(null);
		pregled.setKlinika(klinika);
		pregled.setUpiti(new ArrayList<UpitZaPregled>());
		pregled.setLekar((Lekar)osobljeRepository.findByIdKlinikeAndById(klinika.getId(), pregled.getLekar().getId()));
		return validateAll(klinika, pregled);
		
	}

	public CustomResponse<Pregled> update(Klinika klinika, Pregled pregled, Long idPregleda) throws Exception{
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
	
	@Transactional(readOnly=false)
	public ResponseEntity<CustomResponse<Boolean>> delete(Long idKlinike, Long idPregleda, Long version) throws Exception{
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return new ResponseEntity<CustomResponse<Boolean>>(
					new CustomResponse<Boolean>(false, false, "Greska: Klinika nije pronadjen."), HttpStatus.NOT_FOUND);
		}
		if(posetaRepository.findByIdPregleda(idPregleda) != null){
			return new ResponseEntity<CustomResponse<Boolean>>(
					new CustomResponse<Boolean>(true, false, "Greska: Pregled je rezervisan. Ne mozete ga obrisati. Osvezite stranicu"), HttpStatus.OK);
		}
		Pregled p = pregledRepository.findById(idPregleda).get();
		if(p == null){
			return new ResponseEntity<CustomResponse<Boolean>>(
					new CustomResponse<Boolean>(false, false, "Greska: Pregled nije pronadjen."), HttpStatus.NOT_FOUND);
		}
		for(UpitZaPregled up : p.getUpiti()){
			if(!up.getAdminObradio()){
				return new ResponseEntity<CustomResponse<Boolean>>(
						new CustomResponse<Boolean>(true, false, "Greska: Postoji upit za ovaj pregled za koji administrator jos uvek nije obradio."), HttpStatus.OK);
			}
			if(!up.getPacijentObradio()){
				return new ResponseEntity<CustomResponse<Boolean>>(
						new CustomResponse<Boolean>(true, false, "Greska: Postoji upit za ovaj pregled za koji pacijent jos uvek nije video odgovor administratora."), HttpStatus.OK);
			}
		}
		upitZaPregledRepository.deleteAll(p.getUpiti());
		p.setUpiti(new ArrayList<>());
		pregledRepository.save(p); //nece inkrementirati verziju?
		
		Pregled pregledToDelete = new Pregled();
		pregledToDelete.setId(idPregleda);
		pregledToDelete.setVersion(version);
		pregledRepository.delete(pregledToDelete);
		
		return new ResponseEntity<CustomResponse<Boolean>>(
				new CustomResponse<Boolean>(true, true, "OK"), HttpStatus.OK);
	}
	
	@Transactional(readOnly=false)
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
				if (u.getPotvrdjen()) {
					flag = true;
					break;
				}
				else if (u.getOdobren() && !u.getPacijentObradio()) {
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

	@Transactional(readOnly=true)
	public List<Pregled> getAll(Long idKlinike) {
		if(!klinikaRepository.findById(idKlinike).isPresent()){
			return null;
		}else{
			return pregledRepository.findAllByIdKlinike(idKlinike);
		}
	}
	
	@Transactional(readOnly=true)
	public Pregled get(Long idKlinike, Long idPregleda) {
		if(!klinikaRepository.findById(idKlinike).isPresent()){
			return null;
		}else{
			return pregledRepository.findByIdKlinikeAndIdPregleda(idKlinike, idPregleda);
		}
	}

	@Transactional(readOnly=false)
	public ResponseEntity<CustomResponse<Pregled>> add(Long idKlinike, Pregled pregled) {
		Klinika klinika = klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			CustomResponse<Pregled> customResponse = new CustomResponse<Pregled>(null, false,
					"Greska: Trazena klinika ne postoji");
			return new ResponseEntity<CustomResponse<Pregled>>(customResponse, HttpStatus.NOT_FOUND);
		}else{
			CustomResponse<Pregled> customResponse = add(klinika, pregled);
			return new ResponseEntity<CustomResponse<Pregled>>(customResponse, HttpStatus.OK);
		}
	}

	@Transactional(readOnly=false)
	public ResponseEntity<CustomResponse<Pregled>> update(Long idKlinike, Long idPregleda, Pregled pregled) {
		Klinika klinika = klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			CustomResponse<Pregled> customResponse = new CustomResponse<Pregled>(null, false,
					"Greska: Trazena klinika ne postoji");
			return new ResponseEntity<CustomResponse<Pregled>>(customResponse, HttpStatus.NOT_FOUND);
		}else{
			CustomResponse<Pregled> customResponse = null;
			try{
				customResponse = update(klinika, pregled, idPregleda);
			}catch(Exception e){
				return new ResponseEntity<CustomResponse<Pregled>>(
						new CustomResponse<Pregled>(null, false, "Izuzetak pri optimistickom zakljucavanju. Osvezite stranicu i pokusajte ponovo"),
						HttpStatus.OK);
			}
			return new ResponseEntity<CustomResponse<Pregled>>(customResponse, HttpStatus.OK);
		}
	}
}
