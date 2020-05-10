package isamrs.tim21.klinika.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.Authority;
import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.MedicinskaSestra;
import isamrs.tim21.klinika.domain.MedicinskoOsoblje;
import isamrs.tim21.klinika.domain.TipPregleda;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.repository.AuthorityRepository;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.OsobljeRepository;
import isamrs.tim21.klinika.repository.PregledRepository;
import isamrs.tim21.klinika.repository.RadniKalendarRepository;
import isamrs.tim21.klinika.repository.TipPregledaRepository;

@Service
public class OsobljeService {
	@Autowired
	public PasswordEncoder passwordEncoder;
	
	@Autowired
	public OsobljeRepository osobljeRepository;
	
	@Autowired
	public AuthorityRepository authorityRepository;

	@Autowired
	public TipPregledaRepository tipPregledaRepository;
	
	@Autowired
	public RadniKalendarRepository radniKalendarRepository;
	
	@Autowired
	public PregledRepository pregledRepository;
	
	@Autowired
	public KlinikaRepository klinikaRepository;
	
	@Transactional(readOnly=false, propagation=Propagation.MANDATORY)
	public MedicinskoOsoblje save(MedicinskoOsoblje osobljeToSave){
		osobljeToSave.setSifra(passwordEncoder.encode(osobljeToSave.getSifra()));
		osobljeToSave.setEnabled(true);
		Authority authority;
		if(osobljeToSave instanceof Lekar){
			Lekar l = (Lekar) osobljeToSave;
			authority = authorityRepository.findByName("lekar");
			List<TipPregleda> tipoviPregleda = new ArrayList<TipPregleda>();
			for(TipPregleda tp: l.getTipovi_pregleda()){
				TipPregleda tp2 = tipPregledaRepository.findById(tp.getId()).get();
				tp2.getLekari().add(l);
				tipoviPregleda.add(tp2);
			}
			l.setTipovi_pregleda(tipoviPregleda);
		}else{
			authority = authorityRepository.findByName("medicinska-sestra");
		}
		osobljeToSave.getAuthorities().add(authority);
		osobljeToSave.getRadniKalendar().setMedicinskoOsoblje(osobljeToSave);
		MedicinskoOsoblje retval = osobljeRepository.save(osobljeToSave);
		return retval;
	}

	@Transactional(readOnly=false)
	public CustomResponse<MedicinskoOsoblje> addSpecijalnostOsoblja(Long idOsoblja, List<Long> idTipovaPregleda, Long version){
		MedicinskoOsoblje osoblje = osobljeRepository.findById(idOsoblja).orElse(null);
		if(osoblje == null || osoblje instanceof MedicinskaSestra){
			return new CustomResponse<MedicinskoOsoblje>(null, false, "Greska: Lekar nije pronadjen u trazenoj klinici.");
		}
		if(osoblje.getVersion() != version){
			return new CustomResponse<MedicinskoOsoblje>(osoblje, false, "Verzija podatka je zastarela. Osvezite stranicu");
		}
		Lekar lekar = (Lekar) osoblje;
		for(Long idTipaPregleda : idTipovaPregleda){
			TipPregleda tipPregleda = tipPregledaRepository.findById(idTipaPregleda).orElse(null);
			if(tipPregleda == null){
				return new CustomResponse<MedicinskoOsoblje>(null, false, "Greska: Tip pregleda nije pronadjen.");
			}
			for(TipPregleda tp : lekar.getTipovi_pregleda()){
				if(tp.getId() == tipPregleda.getId()){
					return new CustomResponse<MedicinskoOsoblje>(null, false, "Greska: Tip vec postoji kao specijalnost lekara. Osvezite stranicu.");
				}
			}
			lekar.getTipovi_pregleda().add(tipPregleda);
			tipPregleda.getLekari().add(lekar);
			tipPregledaRepository.save(tipPregleda);
		}
		lekar.setVersion(lekar.getVersion() + 1);
		lekar = osobljeRepository.save(lekar); //obavezno zbog uvecanja verzije
		return new CustomResponse<MedicinskoOsoblje>(lekar, true, "OK.");
	}

	@Transactional(readOnly=false, propagation=Propagation.MANDATORY)
	public CustomResponse<MedicinskoOsoblje> deleteSpecijalnostOsoblja(Long idOsoblja, Long idTipaPregleda, Long version){
		MedicinskoOsoblje osoblje = osobljeRepository.findById(idOsoblja).orElse(null);
		if(osoblje == null || osoblje instanceof MedicinskaSestra){
			return new CustomResponse<MedicinskoOsoblje>(osoblje, false, "Greska: Lekar nije pronadjen.");
		}
		if(osoblje.getVersion() != version){
			return new CustomResponse<MedicinskoOsoblje>(osoblje, false, "Verzija podatka je zastarela. Osvezite stranicu");
		}
		if(!pregledRepository.findByIdLekaraAndIdTipaPregleda(idOsoblja, idTipaPregleda).isEmpty()){
			return new CustomResponse<MedicinskoOsoblje>(osoblje, false, "Greska: Ne mozete obrisati specijalizaciju lekara na osnovu koje postoji kreiran pregled");
		}
		
		TipPregleda tipPregleda = tipPregledaRepository.findById(idTipaPregleda).orElse(null);
		if(tipPregleda == null){
			return new CustomResponse<MedicinskoOsoblje>(osoblje, false, "Greska: Tip pregleda nije pronadjen");
		}
		Lekar lekar = (Lekar) osoblje;
		boolean success = lekar.getTipovi_pregleda().remove(tipPregleda);
		if(!success){
			return new CustomResponse<MedicinskoOsoblje>(osoblje, false, "Greska: Specijalizacija lekara nije pronadjena");
		}
		tipPregleda.getLekari().remove(lekar);
		
		lekar.setVersion(lekar.getVersion() + 1);
		lekar = osobljeRepository.saveAndFlush(lekar); //obavezno zbog uvecanja verzije
		tipPregledaRepository.saveAndFlush(tipPregleda);
		
		return new CustomResponse<MedicinskoOsoblje>(lekar, true, "OK.");
		
	}

	@Transactional(readOnly=false, propagation=Propagation.MANDATORY)
	public CustomResponse<Boolean> delete(Long idKlinike, Long idOsoblja){
		MedicinskoOsoblje osobaToDelete = osobljeRepository.findByIdKlinikeAndById(idKlinike, idOsoblja);
		if(osobaToDelete == null){
			return new CustomResponse<Boolean>(false, false, "Greska. Medicinska osoba ne postoji");
		}
		if(osobaToDelete instanceof MedicinskaSestra){
			osobljeRepository.deleteById(idOsoblja);
			return new CustomResponse<Boolean>(true, true, "OK.");
		}
		else{
			if(!pregledRepository.findByIdLekara(idOsoblja).isEmpty()){
				return new CustomResponse<Boolean>(false, false, "Greska: Ne mozete obrisati lekara za kojeg postoji pregled");
			}
			Lekar lekar = (Lekar) osobljeRepository.findById(idOsoblja).get();
			for(TipPregleda tp : lekar.getTipovi_pregleda()){
				tp.getLekari().remove(lekar); //mora posto je tip pregleda vlasnik veze
			}
			osobljeRepository.deleteById(idOsoblja);
			return new CustomResponse<Boolean>(true, true, "OK.");
		}
		
	}

	@Transactional(readOnly=true)
	public List<MedicinskoOsoblje> findAllByIdKlinike(Long idKlinike) {
		Klinika klinika = klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return null;
		}
		else{
			return osobljeRepository.findAllByIdKlinike(idKlinike);
		}
	}
	
	@Transactional(readOnly=true)
	public MedicinskoOsoblje findByIdKlinikeAndById(Long idKlinike, Long idOsoblja) {
		Klinika klinika = klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return null;
		}else{
			return osobljeRepository.findByIdKlinikeAndById(idKlinike, idOsoblja);
		}
	}

	@Transactional(readOnly=false)
	public MedicinskoOsoblje add(Long idKlinike, MedicinskoOsoblje osobljeToAdd) {
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return null;
		}else{
			osobljeToAdd.setId(null);
			osobljeToAdd.setKlinika(klinika);
			return save(osobljeToAdd);
		}
	}

	@Transactional(readOnly=false)
	public ResponseEntity<CustomResponse<MedicinskoOsoblje>> updateSpecijalnosti(Long idKlinike, Long idOsoblja,
			List<Long> idTipovaPregleda, Long version){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return new ResponseEntity<CustomResponse<MedicinskoOsoblje>>(
					new CustomResponse<MedicinskoOsoblje>(null, false, "Greska: Klinika nije pronadjena."),
					HttpStatus.OK);
		}else{
			CustomResponse<MedicinskoOsoblje> retval = addSpecijalnostOsoblja(idOsoblja, idTipovaPregleda, version);
			return new ResponseEntity<CustomResponse<MedicinskoOsoblje>>(retval, HttpStatus.OK);
		}
	}

	@Transactional(readOnly=false)
	public ResponseEntity<CustomResponse<MedicinskoOsoblje>> deleteSpecijalnostOsoblja(Long idKlinike, Long idOsoblja,
			Long idTipaPregleda, Long version) {
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return new ResponseEntity<CustomResponse<MedicinskoOsoblje>>(new CustomResponse<MedicinskoOsoblje>(null, false, "Greska. Klinika ne postoji"), HttpStatus.NOT_FOUND);
		}else{
			CustomResponse<MedicinskoOsoblje> customResponse = deleteSpecijalnostOsoblja(idOsoblja, idTipaPregleda, version);
			return new ResponseEntity<CustomResponse<MedicinskoOsoblje>>(customResponse, HttpStatus.OK);
		}
	}

	@Transactional(readOnly=false)
	public ResponseEntity<CustomResponse<Boolean>> deleteMain(Long idKlinike, Long idOsoblja) {
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return new ResponseEntity<CustomResponse<Boolean>>(new CustomResponse<Boolean>(false, false, "Greska. Klinika ne postoji"), HttpStatus.NOT_FOUND);
		}else{
			CustomResponse<Boolean> customResponse = delete(idKlinike, idOsoblja);
			return new ResponseEntity<CustomResponse<Boolean>>(customResponse, HttpStatus.OK);
		}
	}
}
