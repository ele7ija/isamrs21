package isamrs.tim21.klinika.controller;

import java.util.Date;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.MedicinskaSestra;
import isamrs.tim21.klinika.domain.MedicinskoOsoblje;
import isamrs.tim21.klinika.domain.Pregled;
import isamrs.tim21.klinika.domain.TipPregleda;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.OsobljeRepository;
import isamrs.tim21.klinika.services.OsobljeService;

@RestController
@RequestMapping(path="/medicinskaOsoba/{idKlinike}")
public class OsobljeKontroler {
	@Autowired
	private KlinikaRepository klinikaRepository;
	
	@Autowired
	private OsobljeRepository osobljeRepository;
	
	@Autowired
	private OsobljeService osobljeService;
	
	@GetMapping
	public ResponseEntity<List<MedicinskoOsoblje>> getAllOsoblje(@PathVariable("idKlinike") Long idKlinike){
		Klinika klinika = klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return new ResponseEntity<List<MedicinskoOsoblje>>(HttpStatus.NOT_FOUND);
		}
		else{
			List<MedicinskoOsoblje> osoblje = osobljeRepository.findAllByIdKlinike(idKlinike);
			return new ResponseEntity<List<MedicinskoOsoblje>>(osoblje, HttpStatus.OK);
		}
	}
	
	@GetMapping(path="/{idOsoblja}")
	public ResponseEntity<MedicinskoOsoblje> getOsoblje(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idOsoblja") Long idOsoblja){
		Klinika klinika = klinikaRepository.findById(idKlinike).orElse(null);
		if(klinika == null){
			return new ResponseEntity<MedicinskoOsoblje>(HttpStatus.NOT_FOUND);
		}else{
			MedicinskoOsoblje medicinskaOsoba = osobljeRepository.findByIdKlinikeAndById(idKlinike, idOsoblja);
			if(medicinskaOsoba == null){
				return new ResponseEntity<MedicinskoOsoblje>(HttpStatus.NOT_FOUND);
			}else{
				return new ResponseEntity<MedicinskoOsoblje>(medicinskaOsoba, HttpStatus.OK);
			}
		}
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<MedicinskoOsoblje> addOsoblje(@PathVariable("idKlinike") Long idKlinike, @RequestBody MedicinskoOsoblje osobljeToAdd){
		System.out.println(osobljeToAdd.getEmail() + " " + osobljeToAdd.getIme() + " " + osobljeToAdd.getPrezime());
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<MedicinskoOsoblje>(HttpStatus.NOT_FOUND);
		}else{
			//POSTAVI JOS JEDNOM SVE PARAMETRE NA BEKU
			osobljeToAdd.setId(null);
			osobljeToAdd.setKlinika(klinika);
			return new ResponseEntity<MedicinskoOsoblje>(osobljeService.save(osobljeToAdd), HttpStatus.OK);
		}
	}
	
	@PutMapping(value="/{idOsoblja}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<MedicinskoOsoblje> updateOsoblje(@PathVariable("idKlinike") Long idKlinike, 
			@PathVariable("idOsoblja") Long idOsoblja, @RequestBody MedicinskoOsoblje osobljeToChange){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<MedicinskoOsoblje>(HttpStatus.NOT_FOUND);
		}else{
			//POSTAVI JOS JEDNOM SVE PARAMETRE NA BEKU
			osobljeToChange.setId(idOsoblja);
			osobljeToChange.setKlinika(klinika);
			if(! osobljeRepository.findById(idOsoblja).isPresent()){
				return new ResponseEntity<MedicinskoOsoblje>(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<MedicinskoOsoblje>(osobljeService.save(osobljeToChange), HttpStatus.OK);	
			
		}
	}
	
	@PutMapping(value="/specijalnosti/{idOsoblja}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<MedicinskoOsoblje> addSpecijalnostOsoblja(@PathVariable("idKlinike") Long idKlinike, 
			@PathVariable("idOsoblja") Long idOsoblja, @RequestBody List<Long> idTipovaPregleda){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<MedicinskoOsoblje>(HttpStatus.NOT_FOUND);
		}else{
			return osobljeService.addSpecijalnostOsoblja(idOsoblja, idTipovaPregleda);	
			
		}
	}
	
	@DeleteMapping(value="/specijalnosti/{idOsoblja}/{idTipaPregleda}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<MedicinskoOsoblje>> deleteSpecijalnostOsoblja(@PathVariable("idKlinike") Long idKlinike, 
			@PathVariable("idOsoblja") Long idOsoblja, @PathVariable("idTipaPregleda") Long idTipaPregleda){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<CustomResponse<MedicinskoOsoblje>>(new CustomResponse<MedicinskoOsoblje>(null, false, "Greska. Klinika ne postoji"), HttpStatus.NOT_FOUND);
		}else{
			CustomResponse<MedicinskoOsoblje> customResponse = osobljeService.deleteSpecijalnostOsoblja(idOsoblja, idTipaPregleda);
			//HttpStatus status = customResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
			return new ResponseEntity<CustomResponse<MedicinskoOsoblje>>(customResponse, HttpStatus.OK);
		}
	}
	
	@DeleteMapping(value="/{idOsoblja}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<Boolean>> deleteOsoblje(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idOsoblja") Long idOsoblja){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<CustomResponse<Boolean>>(new CustomResponse<Boolean>(false, false, "Greska. Klinika ne postoji"), HttpStatus.NOT_FOUND);
		}else{
			CustomResponse<Boolean> customResponse = osobljeService.delete(idKlinike, idOsoblja);
			//HttpStatus status = customResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
			return new ResponseEntity<CustomResponse<Boolean>>(customResponse, HttpStatus.OK);
		}
	}
	
}
