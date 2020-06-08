package isamrs.tim21.klinika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.MedicinskoOsoblje;
import isamrs.tim21.klinika.domain.Pregled;
import isamrs.tim21.klinika.domain.RadniKalendar;
import isamrs.tim21.klinika.domain.ZahtevZaGodisnji;
import isamrs.tim21.klinika.exceptions.BusinessLogicException;
import isamrs.tim21.klinika.exceptions.EntityNotFoundException;
import isamrs.tim21.klinika.repository.OsobljeRepository;
import isamrs.tim21.klinika.repository.RadniKalendarRepository;
import isamrs.tim21.klinika.repository.ZahtevZaGodisnjiRepository;


@Service
public class ZahtevZaGodisnjiService {

	@Autowired
	ZahtevZaGodisnjiRepository zahtevZaGodisnjiRepository;
	
	@Autowired
	RadniKalendarRepository radniKalendarRepository;

	@Autowired
	OsobljeRepository osobljeRepository;

	@Autowired
	MailService mailService;
	
	@Transactional(readOnly=true)
	public List<ZahtevZaGodisnji> findAllByIdKlinike(Long idKlinike) {
		return zahtevZaGodisnjiRepository.findAllByIdKlinike(idKlinike);
	}
	
	@Transactional(readOnly=true)
	public List<ZahtevZaGodisnji> findAllPoslatiByIdOsoblja(Long idOsoblja) {
		return zahtevZaGodisnjiRepository.findAllPoslatiByIdOsoblja(idOsoblja);
	}
	
	@Transactional(readOnly=true)
	public List<ZahtevZaGodisnji> findAllNeobradjeniZahtevi(Long idOsoblja) {
		return zahtevZaGodisnjiRepository.findAllNeobradjeniByIdOsoblja(idOsoblja);
	}

	@Transactional(readOnly=false)
	public ZahtevZaGodisnji add(ZahtevZaGodisnji zahtevToAdd){
		RadniKalendar kalendar = radniKalendarRepository.findByIdPessimisticWrite(zahtevToAdd.getRadniKalendar().getId());
		zahtevToAdd.setRadniKalendar(kalendar);
		return zahtevZaGodisnjiRepository.save(zahtevToAdd);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public ZahtevZaGodisnji approve(ZahtevZaGodisnji zahtevToUpdate) throws EntityNotFoundException, BusinessLogicException{
		//pessimistic write kako bi sprecio paralelno odobravanje istog razlicitih zahteva za odsustvo za istog lekara
		//dodavanje pregleda isto koristi pessimistic_write za dobavljanje lekara, tako da je i to reseno

		MedicinskoOsoblje osoblje = osobljeRepository.findByIdPessimisticWrite(zahtevToUpdate.getRadniKalendar().getMedicinskoOsoblje().getId());
		if(osoblje == null)
			throw new EntityNotFoundException("Medicinsko osoblje");

		for(ZahtevZaGodisnji z : osoblje.getRadniKalendar().getZahteviZaGodisnjiOdmor()){
			if(z.intersects(zahtevToUpdate) && z.isOdobreno()){
				throw new BusinessLogicException("Greška: Već imate odobren zahtev za godišnji odmor u vremenskom intervalu koji se preklapa sa unetim.");
			}
		}
		if(osoblje instanceof Lekar){
			Lekar lekar = (Lekar) osoblje;
			for(Pregled pregled : lekar.getPregledi()){
				if(pregled.intersects(zahtevToUpdate))
					throw new BusinessLogicException("Greška: Ne možete odobriti zahtev za godišnji jer postoji pregled kod lekara u datom vremenskom intervalu.");
			}
			for(Pregled pregled : lekar.getDodatneOperacije()){
				if(pregled.intersects(zahtevToUpdate))
					throw new BusinessLogicException("Greška: Ne možete odobriti zahtev za godišnji jer postoji dodatna operacija kod lekara u datom vremenskom intervalu.");
			}
		}
		zahtevToUpdate.setRadniKalendar(osoblje.getRadniKalendar());
		zahtevToUpdate.setAdminObradio(true);
		zahtevToUpdate.setOdobreno(true);
		return zahtevZaGodisnjiRepository.save(zahtevToUpdate);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public ZahtevZaGodisnji reject(ZahtevZaGodisnji zahtevToUpdate) throws EntityNotFoundException{
		/*
		pri odbijanju zahteva ne moramo da vrsimo nikakve provere
		al opet treba da sprecimo paralelnu obradu istog zahteva
		*/
		ZahtevZaGodisnji zahtev = zahtevZaGodisnjiRepository.findById(zahtevToUpdate.getId()).orElse(null);
		if(zahtev == null)
			throw new EntityNotFoundException("Zahtev za odsustvo");
		zahtevToUpdate.setOdobreno(false);
		zahtevToUpdate.setAdminObradio(true);
		return zahtevZaGodisnjiRepository.save(zahtevToUpdate);
	}

	@Async
	public void sendMail(ZahtevZaGodisnji zahtev){
		if(zahtev.isOdobreno()){
			mailService.prihvatiZahtevZaGodisnji(zahtev);
		}else{
			mailService.odbiZahtevZaGodisnji(zahtev);
		}
	}

	@Transactional(readOnly=false)
	public ZahtevZaGodisnji obradiOsoblje(Long idZahtevaZaGodisnji) throws EntityNotFoundException{
		ZahtevZaGodisnji zahtev = zahtevZaGodisnjiRepository.findById(idZahtevaZaGodisnji).get();
		if(zahtev == null)
			throw new EntityNotFoundException("Zahtev za odsustvo.");
		zahtev.setOsobljeObradilo(true);
		return zahtevZaGodisnjiRepository.save(zahtev);
	}

}
