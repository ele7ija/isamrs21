package isamrs.tim21.klinika.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.Authority;
import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.MedicinskaSestra;
import isamrs.tim21.klinika.domain.MedicinskoOsoblje;
import isamrs.tim21.klinika.domain.TipPregleda;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.dto.LekarProfilDTO;
import isamrs.tim21.klinika.dto.SestraProfilDTO;
import isamrs.tim21.klinika.exceptions.BusinessLogicException;
import isamrs.tim21.klinika.exceptions.EntityNotFoundException;
import isamrs.tim21.klinika.repository.AuthorityRepository;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.OsobljeRepository;
import isamrs.tim21.klinika.repository.PregledRepository;
import isamrs.tim21.klinika.repository.RadniKalendarRepository;
import isamrs.tim21.klinika.repository.TipPregledaRepository;
import isamrs.tim21.klinika.repository.ZahtevZaGodisnjiRepository;
import isamrs.tim21.klinika.repository.UpitZaPregledRepository;

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

	@Autowired
	public ZahtevZaGodisnjiRepository zahtevZaGodisnjiRepository;

	@Autowired
	public UpitZaPregledRepository upitZaPregledRepository;
	
	@Autowired
	public CustomUserDetailsService userDetailsService;
	
	@Transactional(readOnly=false, propagation=Propagation.MANDATORY) //anotacija se nece ignorisati ako je pozovemo iz drgue instance
	public MedicinskoOsoblje save(MedicinskoOsoblje osobljeToSave) throws BusinessLogicException{
		osobljeToSave.setSifra(passwordEncoder.encode(osobljeToSave.getSifra()));
		osobljeToSave.setEnabled(true);
		Authority authority;
		if(osobljeToSave instanceof Lekar){
			authority = authorityRepository.findByName("lekar");
			osobljeToSave.getAuthorities().add(authority);
			osobljeToSave.getRadniKalendar().setMedicinskoOsoblje(osobljeToSave);
			Lekar retval = (Lekar)osobljeRepository.save(osobljeToSave);

			Lekar l = (Lekar)osobljeToSave;
			List<TipPregleda> tipoviPregleda = new ArrayList<TipPregleda>();

			/*
				za svaki tip pregleda se radi upit u bazu koji ce biti neuspesan ako neka od torki vec ima postavljen exclusive lock
				taj lock se postavlja na primer pri brisanju tipa pregleda
				sa druge strane, ako u ovoj metodi prvi dodjemo do tipa pregleda, stavimo shared lock na njega kako brisanje ne bi uspelo
			*/
			for(TipPregleda tp: l.getTipovi_pregleda()){
				TipPregleda tp2 = tipPregledaRepository.findByIdKlinikeAndIdPessimisticRead(osobljeToSave.getKlinika().getId(), tp.getId());
				if(tp2 == null)
					throw new BusinessLogicException("Greška. Nismo pronašli jedan od tipova pregleda.");
				tp2.getLekari().add(retval);
				tipoviPregleda.add(tp2);
			}
			retval.setTipovi_pregleda(tipoviPregleda);
			return retval;
		}else{
			authority = authorityRepository.findByName("medicinska-sestra");
			osobljeToSave.getAuthorities().add(authority);
			osobljeToSave.getRadniKalendar().setMedicinskoOsoblje(osobljeToSave);
			MedicinskoOsoblje retval = osobljeRepository.save(osobljeToSave);
			return retval;
		}
	}

	@Transactional(readOnly=false, isolation = Isolation.READ_COMMITTED)
	public CustomResponse<MedicinskoOsoblje> addSpecijalnostOsoblja(Long idKlinike, Long idOsoblja, List<Long> idTipovaPregleda,
			Long version) throws EntityNotFoundException, BusinessLogicException {
		/*
				Kako bi sprecili simultano dodavanje iste specijalnosti kod iste osobe, osobu dobavljamo sa exclusive lock-om
		*/
		
		MedicinskoOsoblje osoblje = osobljeRepository.findByIdKlinikeAndByIdPessimisticWrite(idKlinike, idOsoblja);
		if(osoblje == null || osoblje instanceof MedicinskaSestra)
			throw new EntityNotFoundException("Lekar");
		
		Lekar lekar = (Lekar) osoblje;
		//poredjenje verzija je bezbedno raditi ovako jer smo uzeli exclusive lock
		if(!version.equals(lekar.getVersion())){
			throw new BusinessLogicException("Greška. Vaš podatak ima zastarelu verziju. Osvežite stranicu.");
		}
		int brojSpecijalizacija = lekar.getBrojSpecijalizacija();
		for(Long idTipaPregleda : idTipovaPregleda){
			/*
				Svaki tip pregleda zalkjucavamo zato sto je moguce da na primer imamo konkurentnu transakciju koja brise isti tip pregleda
				Uzimamo write lock zato sto se tip pregleda cuva u liniji 123
			*/
			TipPregleda tipPregleda = tipPregledaRepository.findByIdKlinikeAndIdPessimisticWrite(idKlinike, idTipaPregleda);
			if(tipPregleda == null)
				throw new EntityNotFoundException("Tip pregleda");
			
			for(TipPregleda tp : lekar.getTipovi_pregleda()){
				if(tp.getId().equals(tipPregleda.getId()))
					throw new BusinessLogicException("Greska: Tip pregleda vec postoji kao specijalnost lekara. Osvezite stranicu.");
			}

			lekar.getTipovi_pregleda().add(tipPregleda);
			tipPregleda.getLekari().add(lekar);
			tipPregledaRepository.save(tipPregleda);
			brojSpecijalizacija += 1;
		}
		lekar.setBrojSpecijalizacija(brojSpecijalizacija);
		lekar = osobljeRepository.save(lekar); 
		return new CustomResponse<MedicinskoOsoblje>(lekar, true, "OK.");
	}

	@Transactional(readOnly=false, isolation = Isolation.READ_COMMITTED)
	public CustomResponse<MedicinskoOsoblje> deleteSpecijalnostOsoblja(Long idKlinike, Long idOsoblja, Long idTipaPregleda,
			Long version) throws EntityNotFoundException, BusinessLogicException{
		/*
				Dobavljamo osobu u pessimistic write rezimu kako bi sprecili simultano dodavanje pregleda kod osobe cija psecijalnost se brise
				Jer pre dodavanja pregleda, prvo se dobavlja osoba sa shared lock-om
		*/
		MedicinskoOsoblje osoblje = osobljeRepository.findByIdKlinikeAndByIdPessimisticWrite(idKlinike, idOsoblja);
		if(osoblje == null || osoblje instanceof MedicinskaSestra)
			throw new EntityNotFoundException("Lekar");
		
		if(!pregledRepository.findByIdLekaraAndIdTipaPregleda(idOsoblja, idTipaPregleda).isEmpty())
			throw new BusinessLogicException("Greska: Ne mozete obrisati specijalizaciju lekara na osnovu koje postoji kreiran pregled.");
		
		TipPregleda tipPregleda = tipPregledaRepository.findById(idTipaPregleda).orElse(null);
		if(tipPregleda == null)
			throw new EntityNotFoundException("Tip pregleda");
		
		Lekar lekar = (Lekar) osoblje;
		boolean success = lekar.getTipovi_pregleda().remove(tipPregleda);
		if(!success){
			throw new BusinessLogicException("Greska: Specijalizacija lekara nije pronadjena. Osvezite stranicu");
		}
		tipPregleda.getLekari().remove(lekar);
		
		lekar.setBrojSpecijalizacija(lekar.getBrojSpecijalizacija() - 1);
		lekar = osobljeRepository.save(lekar);
		tipPregledaRepository.save(tipPregleda);
		
		return new CustomResponse<MedicinskoOsoblje>(lekar, true, "OK.");
		
	}

	@Transactional(readOnly=false, isolation = Isolation.READ_COMMITTED)
	public CustomResponse<Boolean> delete(Long idKlinike, Long idOsoblja, Long version) 
		throws EntityNotFoundException, BusinessLogicException{
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null)
			throw new EntityNotFoundException("Klinika");
		
		MedicinskoOsoblje osobaToDelete = osobljeRepository.findByIdKlinikeAndByIdPessimisticWrite(idKlinike, idOsoblja);
		if(osobaToDelete == null){
			throw new EntityNotFoundException("Medicinska osoba");
		}
		if(!version.equals(osobaToDelete.getVersion()))
				throw new BusinessLogicException("Greška. Vaš podatak ima zastarelu verziju. Osvežite stranicu.");
		if(osobaToDelete instanceof MedicinskaSestra){
			MedicinskaSestra sestra = (MedicinskaSestra) osobaToDelete;
			osobljeRepository.delete(sestra);
			return new CustomResponse<Boolean>(true, true, "OK.");
		}
		else{
			if(!pregledRepository.findByIdLekara(idOsoblja).isEmpty()){
				throw new BusinessLogicException("Greska: Ne mozete obrisati lekara za kojeg postoji pregled");
			}
			if(!pregledRepository.findByIdDodatnogLekara(idOsoblja).isEmpty()){
				throw new BusinessLogicException("Greska: Ne mozete obrisati lekara koji asistira na nekoj operaciji.");
			}
			if(!upitZaPregledRepository.findByIdLekara(idOsoblja).isEmpty()){
				throw new BusinessLogicException("Greska: Ne mozete obrisati lekara za kojeg postoji upit za pregled");
			}
			Lekar lekar = (Lekar) osobaToDelete;
			for(TipPregleda tp : lekar.getTipovi_pregleda()){
				tp.getLekari().remove(lekar); //mora posto je tip pregleda vlasnik veze
			}
			osobljeRepository.delete(lekar);
			return new CustomResponse<Boolean>(true, true, "OK.");
		}
		
	}

	@Transactional(readOnly=true, isolation = Isolation.READ_COMMITTED)
	public List<MedicinskoOsoblje> findAllByIdKlinike(Long idKlinike) throws EntityNotFoundException{
		Klinika klinika = klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null)
			throw new EntityNotFoundException("Klinika");
		else
			return osobljeRepository.findAllByIdKlinike(idKlinike);
	}

	@Transactional(readOnly=true, isolation = Isolation.READ_COMMITTED)
	public CustomResponse<List<Lekar>> findAllLekariByIdKlinike(Long idKlinike) throws EntityNotFoundException{
		Klinika klinika = klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null)
			throw new EntityNotFoundException("Klinika");
		List<Lekar> lekari = osobljeRepository.findAllLekariByIdKlinike(idKlinike);
		return new CustomResponse<List<Lekar>>(lekari, true, "Lekari pronađeni.");
	}

	@Transactional(readOnly=false, isolation = Isolation.READ_COMMITTED)
	public MedicinskoOsoblje add(Long idKlinike, MedicinskoOsoblje osobljeToAdd) throws EntityNotFoundException{
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null)
			throw new EntityNotFoundException("Klinika");
		else{
			osobljeToAdd.setId(null);
			osobljeToAdd.setKlinika(klinika);
			return save(osobljeToAdd);
		}
	}

	@Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED)
	public CustomResponse<MedicinskoOsoblje> updateProfilLekara(LekarProfilDTO lekar) {
		Lekar retval = (Lekar)userDetailsService.findUserAndChangePassword(lekar.getLekar().getId(), lekar.getPoslednjaSifra(), lekar.getLekar().getSifra());
		retval.setIme(lekar.getLekar().getIme());
		retval.setPrezime(lekar.getLekar().getPrezime());
		if(!lekar.getPoslednjaSifra().equals(retval.getSifra())){
			retval.setPoslednjaPromenaSifre(new Date());
		}
		retval = osobljeRepository.save(retval);
		return new CustomResponse<MedicinskoOsoblje>(retval, true, "OK.");
	}

	@Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED)
	public CustomResponse<MedicinskoOsoblje> updateProfilSestra(SestraProfilDTO sestra) {
		MedicinskaSestra retval = (MedicinskaSestra) userDetailsService.findUserAndChangePassword(sestra.getSestra().getId(),  sestra.getPoslednjaSifra(), sestra.getSestra().getSifra());
		retval.setIme(sestra.getSestra().getIme());
		retval.setPrezime(sestra.getSestra().getPrezime());
		if(!sestra.getPoslednjaSifra().equals(retval.getSifra())){
			retval.setPoslednjaPromenaSifre(new Date());
		}
		retval = osobljeRepository.save(retval);
		return new CustomResponse<MedicinskoOsoblje>(retval, true, "OK.");
	}
}
