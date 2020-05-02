package isamrs.tim21.klinika.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.Authority;
import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.MedicinskaSestra;
import isamrs.tim21.klinika.domain.MedicinskoOsoblje;
import isamrs.tim21.klinika.domain.TipPregleda;
import isamrs.tim21.klinika.repository.AuthorityRepository;
import isamrs.tim21.klinika.repository.OsobljeRepository;
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

	public MedicinskoOsoblje updateSpecijalnosti(MedicinskoOsoblje osobljeToChange) {
		Lekar lekar = (Lekar) osobljeToChange;
		Lekar lekarPersistent = (Lekar) osobljeRepository.findById(osobljeToChange.getId()).get();
		for(TipPregleda tp: lekarPersistent.getTipovi_pregleda()){
			tp.getLekari().remove(lekarPersistent);
		}
		lekarPersistent.getTipovi_pregleda().clear();
		for(TipPregleda tp: lekar.getTipovi_pregleda()){
			TipPregleda tipPregledaPersistent = tipPregledaRepository.findById(tp.getId()).get();
			lekarPersistent.getTipovi_pregleda().add(tipPregledaPersistent);
			tipPregledaPersistent.getLekari().add(lekarPersistent);
		}
		return osobljeRepository.save(lekarPersistent);
	}

	public ResponseEntity<MedicinskoOsoblje> addSpecijalnostOsoblja(Long idOsoblja, List<Long> idTipovaPregleda) {
		MedicinskoOsoblje osoblje = osobljeRepository.findById(idOsoblja).orElse(null);
		if(osoblje == null || osoblje instanceof MedicinskaSestra){
			return new ResponseEntity<MedicinskoOsoblje>(osoblje, HttpStatus.NOT_FOUND);
		}
		Lekar lekar = (Lekar) osoblje;
		for(Long idTipaPregleda : idTipovaPregleda){
			TipPregleda tipPregleda = tipPregledaRepository.findById(idTipaPregleda).orElse(null);
			if(tipPregleda == null){
				return new ResponseEntity<MedicinskoOsoblje>(HttpStatus.NOT_FOUND);
			}
			lekar.getTipovi_pregleda().add(tipPregleda);
			tipPregleda.getLekari().add(lekar);
			tipPregledaRepository.save(tipPregleda);
		}
		osobljeRepository.save(lekar);
		return new ResponseEntity<MedicinskoOsoblje>(lekar, HttpStatus.OK);
	}

	public ResponseEntity<MedicinskoOsoblje> deleteSpecijalnostOsoblja(Long idOsoblja, Long idTipaPregleda) {
		MedicinskoOsoblje osoblje = osobljeRepository.findById(idOsoblja).orElse(null);
		if(osoblje == null || osoblje instanceof MedicinskaSestra){
			return new ResponseEntity<MedicinskoOsoblje>(osoblje, HttpStatus.NOT_FOUND);
		}
		
		TipPregleda tipPregleda = tipPregledaRepository.findById(idTipaPregleda).orElse(null);
		if(tipPregleda == null){
			return new ResponseEntity<MedicinskoOsoblje>(HttpStatus.NOT_FOUND);
		}
		Lekar lekar = (Lekar) osoblje;
		boolean success = lekar.getTipovi_pregleda().remove(tipPregleda);
		if(!success){
			return new ResponseEntity<MedicinskoOsoblje>(HttpStatus.NOT_FOUND);
		}
		tipPregleda.getLekari().remove(lekar);
		
		osobljeRepository.save(lekar);
		tipPregledaRepository.save(tipPregleda);
		
		return new ResponseEntity<MedicinskoOsoblje>(lekar, HttpStatus.OK);
		
	}
}