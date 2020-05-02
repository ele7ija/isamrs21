package isamrs.tim21.klinika.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

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
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.repository.AuthorityRepository;
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

	public CustomResponse<MedicinskoOsoblje> deleteSpecijalnostOsoblja(Long idOsoblja, Long idTipaPregleda) {
		MedicinskoOsoblje osoblje = osobljeRepository.findById(idOsoblja).orElse(null);
		if(osoblje == null || osoblje instanceof MedicinskaSestra){
			return new CustomResponse<MedicinskoOsoblje>(osoblje, false, "Greska: Lekar nije pronadjen.");
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
		
		osobljeRepository.save(lekar);
		tipPregledaRepository.save(tipPregleda);
		
		return new CustomResponse<MedicinskoOsoblje>(osoblje, true, "OK.");
		
	}

	@Transactional
	public CustomResponse<Boolean> delete(Long idKlinike, Long idOsoblja) {
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
}
