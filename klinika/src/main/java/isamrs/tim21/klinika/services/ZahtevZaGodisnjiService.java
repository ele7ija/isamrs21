package isamrs.tim21.klinika.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.MedicinskoOsoblje;
import isamrs.tim21.klinika.domain.Pregled;
import isamrs.tim21.klinika.domain.RadniKalendar;
import isamrs.tim21.klinika.domain.ZahtevZaGodisnji;
import isamrs.tim21.klinika.repository.RadniKalendarRepository;
import isamrs.tim21.klinika.repository.ZahtevZaGodisnjiRepository;

@Service
public class ZahtevZaGodisnjiService {

	@Autowired
	ZahtevZaGodisnjiRepository zahtevZaGodisnjiRepository;
	
	@Autowired
	RadniKalendarRepository radniKalendarRepository;
	
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

	@Transactional(readOnly=false, isolation=Isolation.SERIALIZABLE)
	public ZahtevZaGodisnji update(Long idZahtevaZaGodisnji, ZahtevZaGodisnji zahtevToUpdate) throws Exception {
		//serializable jer je potrebno spreciti neku drugu transakciju koju izvrsava da odobri zahtev za godisnji u istom vremenskom periodu
		//odnosno, moramo da sprecimo phantom read
		//medjutim, kako je dozvoljeno dodavati samo neodobrene zahteve za godisnji paralelno sa ovom metodom, REPEATABLE_READ je dovoljan nivo izolacije
		//ostavljamo serializable ipak zbog provere pregleda
		RadniKalendar kalendar = radniKalendarRepository.findById(zahtevToUpdate.getRadniKalendar().getId()).get();
		if(zahtevToUpdate.isOdobreno()){
			for(ZahtevZaGodisnji zahtev : kalendar.getZahteviZaGodisnjiOdmor()){
				if(zahtev.intersects(zahtevToUpdate) && zahtev.isOdobreno()){
					throw new Exception("Greška: Već imate odobren zahtev za godišnji odmor u vremenskom intervalu koji se preklapa sa unetim.");
				}
			}
			MedicinskoOsoblje osoblje = kalendar.getMedicinskoOsoblje();
			if(osoblje instanceof Lekar){
				Lekar lekar = (Lekar) osoblje;
				for(Pregled pregled : lekar.getPregledi()){
					if(pregled.intersects(zahtevToUpdate))
						throw new Exception("Greška: Već ne možete odobriti zahtev za godišnji jer postoji pregled kod lekara u datom vremenskom intervalu.");
				}
				for(Pregled pregled : lekar.getDodatneOperacije()){
					if(pregled.intersects(zahtevToUpdate))
						throw new Exception("Greška: Već ne možete odobriti zahtev za godišnji jer postoji dodatna operacija kod lekara u datom vremenskom intervalu.");
				}
			}
		}
		zahtevToUpdate.setRadniKalendar(kalendar);
		return zahtevZaGodisnjiRepository.save(zahtevToUpdate);
	}

	@Transactional(readOnly=false)
	public ZahtevZaGodisnji obradiOsoblje(Long idZahtevaZaGodisnji) {
		ZahtevZaGodisnji zahtev = zahtevZaGodisnjiRepository.findByIdPessimisticWrite(idZahtevaZaGodisnji); //pessimistic write nad zahtevom za godisnji koji se menja
		zahtev.setOsobljeObradilo(true);
		return zahtevZaGodisnjiRepository.save(zahtev);
	}

}
