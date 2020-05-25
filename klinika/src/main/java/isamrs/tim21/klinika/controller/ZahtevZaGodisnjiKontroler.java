package isamrs.tim21.klinika.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim21.klinika.domain.ZahtevZaGodisnji;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.services.ZahtevZaGodisnjiService;


@RestController
@RequestMapping(path="/zahtevZaGodisnji")
public class ZahtevZaGodisnjiKontroler {

	@Autowired
	ZahtevZaGodisnjiService zahtevZaGodisnjiService;
	
	@GetMapping(value="/{idKlinike}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<List<ZahtevZaGodisnji>> getAllZahtevZaGodisnji(@PathVariable("idKlinike") Long idKlinike){
		List<ZahtevZaGodisnji> zahtevi = zahtevZaGodisnjiService.findAllByIdKlinike(idKlinike);
		if(zahtevi == null){
			return new ResponseEntity<List<ZahtevZaGodisnji>>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<List<ZahtevZaGodisnji>>(zahtevi, HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/poslati/{idOsoblja}")
	@PreAuthorize("hasAuthority('lekar') or hasAuthority('medicinska-sestra')")
	public ResponseEntity<List<ZahtevZaGodisnji>> getPoslatiZahtevi(@PathVariable("idOsoblja") Long idOsoblja){
		List<ZahtevZaGodisnji> zahtevi = zahtevZaGodisnjiService.findAllPoslatiByIdOsoblja(idOsoblja);
		if(zahtevi == null){
			return new ResponseEntity<List<ZahtevZaGodisnji>>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<List<ZahtevZaGodisnji>>(zahtevi, HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/neobradjeni/{idOsoblja}")
	@PreAuthorize("hasAuthority('lekar') or hasAuthority('medicinska-sestra')")
	public ResponseEntity<List<ZahtevZaGodisnji>> getNeodobreniZahtevi(@PathVariable("idOsoblja") Long idOsoblja){
		List<ZahtevZaGodisnji> zahtevi = zahtevZaGodisnjiService.findAllNeobradjeniZahtevi(idOsoblja);
		if(zahtevi == null){
			return new ResponseEntity<List<ZahtevZaGodisnji>>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<List<ZahtevZaGodisnji>>(zahtevi, HttpStatus.OK);
		}
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('lekar') or hasAuthority('medicinska-sestra')")
	public ResponseEntity<ZahtevZaGodisnji> addZahtevZaGodisnji(@RequestBody ZahtevZaGodisnji zahtevToAdd){
		ZahtevZaGodisnji zahtev = zahtevZaGodisnjiService.add(zahtevToAdd);
		if(zahtev == null){
			return new ResponseEntity<ZahtevZaGodisnji>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<ZahtevZaGodisnji>(zahtev, HttpStatus.OK);
		}
	}
	
	@PutMapping(value="/{idZahtevaZaGodisnji}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<ZahtevZaGodisnji>> updateZahtevZaGodisnji(@PathVariable("idZahtevaZaGodisnji") Long idZahtevaZaGodisnji, @RequestBody ZahtevZaGodisnji zahtevToUpdate){
		ZahtevZaGodisnji zahtev = null;
		try{
			zahtev = zahtevZaGodisnjiService.update(idZahtevaZaGodisnji, zahtevToUpdate);
		}catch(Exception e){
			return new ResponseEntity<CustomResponse<ZahtevZaGodisnji>>(
				new CustomResponse<ZahtevZaGodisnji>(null, false, e.getMessage()),
				HttpStatus.NOT_FOUND
			);
		}
		zahtevZaGodisnjiService.sendMail(zahtev);
		return new ResponseEntity<CustomResponse<ZahtevZaGodisnji>>(
			new CustomResponse<ZahtevZaGodisnji>(zahtev, true, "Zahtev je uspešno obrađen."),
			HttpStatus.OK
		);
	}
	
	@PutMapping(value="obradiOsoblje/{idZahtevaZaGodisnji}")
	@PreAuthorize("hasAuthority('lekar') or hasAuthority('medicinska-sestra')")
	public ResponseEntity<ZahtevZaGodisnji>obradiOsoblje(@PathVariable("idZahtevaZaGodisnji") Long idZahtevaZaGodisnji){
		ZahtevZaGodisnji zahtev = zahtevZaGodisnjiService.obradiOsoblje(idZahtevaZaGodisnji);
		return new ResponseEntity<ZahtevZaGodisnji>(
			zahtev,
			HttpStatus.OK
		);
	}
}
