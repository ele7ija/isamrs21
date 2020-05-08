package isamrs.tim21.klinika.controller;

import java.util.List;

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

import isamrs.tim21.klinika.domain.MedicinskoOsoblje;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.services.OsobljeService;

@RestController
@RequestMapping(path="/medicinskaOsoba/{idKlinike}")
public class OsobljeKontroler {
			
	@Autowired
	private OsobljeService osobljeService;
	
	@GetMapping
	public ResponseEntity<List<MedicinskoOsoblje>> getAllOsoblje(@PathVariable("idKlinike") Long idKlinike){
		List<MedicinskoOsoblje> osoblje = osobljeService.findAllByIdKlinike(idKlinike);
		if(osoblje == null){
			return new ResponseEntity<List<MedicinskoOsoblje>>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<List<MedicinskoOsoblje>>(osoblje, HttpStatus.OK);
		}
	}
	
	@GetMapping(path="/{idOsoblja}")
	public ResponseEntity<MedicinskoOsoblje> getOsoblje(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idOsoblja") Long idOsoblja){
		MedicinskoOsoblje osoba = osobljeService.findByIdKlinikeAndById(idKlinike, idOsoblja);
		if(osoba == null){
			return new ResponseEntity<MedicinskoOsoblje>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<MedicinskoOsoblje>(osoba, HttpStatus.OK);
		}
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<MedicinskoOsoblje> addOsoblje(@PathVariable("idKlinike") Long idKlinike, @RequestBody MedicinskoOsoblje osobljeToAdd){
		MedicinskoOsoblje osoblje = osobljeService.add(idKlinike, osobljeToAdd);
		if(osoblje == null){
			return new ResponseEntity<MedicinskoOsoblje>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<MedicinskoOsoblje>(osoblje, HttpStatus.OK);
		}
	}
	
	@PutMapping(value="/specijalnosti/{idOsoblja}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<MedicinskoOsoblje>> addSpecijalnostOsoblja(@PathVariable("idKlinike") Long idKlinike, 
			@PathVariable("idOsoblja") Long idOsoblja, @RequestBody List<Long> idTipovaPregleda){
		ResponseEntity<CustomResponse<MedicinskoOsoblje>> retval = null;
		try{
			retval = osobljeService.updateSpecijalnosti(idKlinike, idOsoblja, idTipovaPregleda);
		}catch(Exception e){
			retval = new ResponseEntity<CustomResponse<MedicinskoOsoblje>>(new CustomResponse<MedicinskoOsoblje>(
					null, false, "Greska usled optimistickog zakljucavanja. Pokusajte ponovo."), HttpStatus.OK);
		}
		return retval;
	}
	
	@DeleteMapping(value="/specijalnosti/{idOsoblja}/{idTipaPregleda}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<MedicinskoOsoblje>> deleteSpecijalnostOsoblja(@PathVariable("idKlinike") Long idKlinike, 
			@PathVariable("idOsoblja") Long idOsoblja, @PathVariable("idTipaPregleda") Long idTipaPregleda){
		return osobljeService.deleteSpecijalnostOsoblja(idKlinike, idOsoblja, idTipaPregleda);
	}
	
	@DeleteMapping(value="/{idOsoblja}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<CustomResponse<Boolean>> deleteOsoblje(@PathVariable("idKlinike") Long idKlinike, @PathVariable("idOsoblja") Long idOsoblja){
		return osobljeService.deleteMain(idKlinike, idOsoblja);
	}
	
}
