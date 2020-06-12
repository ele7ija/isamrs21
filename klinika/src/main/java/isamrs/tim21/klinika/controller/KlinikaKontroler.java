package isamrs.tim21.klinika.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import isamrs.tim21.klinika.dto.KlinikaDTO;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.services.KlinikaService;

@RestController
@RequestMapping(path="/api/klinika")
public class KlinikaKontroler {
	
	@Autowired
	private KlinikaRepository klinikaRepository;
	
	@Autowired KlinikaService klinikaService;
	
	@GetMapping
	public ResponseEntity<List<Klinika>> getAllKlinike(){	
		List<Klinika> retval = klinikaRepository.findAll();
		return new ResponseEntity<List<Klinika>>(retval, HttpStatus.OK);
	}
	
	@GetMapping(value="/klinikaUlogovanogKorisnika")
	@PreAuthorize("hasAuthority('admin-klinike') or hasAuthority('lekar')")
	public ResponseEntity<Klinika> getKlinikaUlogovanogKorisnika(HttpServletRequest request){	
		Klinika retval = klinikaService.findKlinikaUlogovanog(request);
		if(retval == null){
			return new ResponseEntity<Klinika>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Klinika>(retval, HttpStatus.OK);
	}
	
	@GetMapping(value="/{idKlinike}")
	public ResponseEntity<Klinika> getklinika(@PathVariable("idKlinike") Long idKlinike){
		Klinika klinika =  klinikaRepository.findById(idKlinike).orElse(null); //ovo ce verovatno ici u aspekt
		if(klinika == null){
			return new ResponseEntity<Klinika>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<Klinika>(klinika, HttpStatus.OK);
		}
	}
	

	@PostMapping
	@PreAuthorize("hasAuthority('admin-klinickog-centra')")
	public ResponseEntity<Klinika> addKlinika(@RequestBody KlinikaDTO klinikaToadd){
		Klinika retval = klinikaService.addKlinika(klinikaToadd);
		return new ResponseEntity<Klinika>(retval, HttpStatus.OK);
	}

	@PutMapping("/{idKlinike}/adminCentra")
	@PreAuthorize("hasAuthority('admin-klinickog-centra')")
	public ResponseEntity<Klinika> updateKlinikaFromAdminCentra(
		@PathVariable("idKlinike") Long idKlinike, @RequestBody KlinikaDTO klinikaToUpdate){
		Klinika retval = klinikaService.updateKlinikaFromAdminCentra(idKlinike, klinikaToUpdate);
		return new ResponseEntity<>(retval, HttpStatus.OK);
	}
	
	@PutMapping(value="/{idKlinike}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<Klinika> updateKlinika(@PathVariable("idKlinike") Long idKlinike, @RequestBody Klinika klinikaToChange){
		//menjamo samo naziv i adresu
		klinikaToChange.setId(idKlinike);
		Klinika exKlinika = klinikaRepository.findById(idKlinike).orElse(null);
		if(exKlinika == null){
			return new ResponseEntity<Klinika>(HttpStatus.NOT_FOUND);
		}	
		
		klinikaToChange.setCenovnici(exKlinika.getCenovnici());
		klinikaToChange.setSale(exKlinika.getSale());
		klinikaToChange.setTipoviPregleda(exKlinika.getTipoviPregleda());
		klinikaToChange.setPregledi(exKlinika.getPregledi());
		klinikaToChange.setMedicinskoOsoblje(exKlinika.getMedicinskoOsoblje());
		klinikaToChange.setAdministratoriKlinike(exKlinika.getAdministratoriKlinike());
		
		Klinika retval = klinikaRepository.save(klinikaToChange);
		return new ResponseEntity<Klinika>(retval, HttpStatus.OK);
	}
	
	@DeleteMapping(value="/{idKlinike}")
	@PreAuthorize("hasAuthority('admin-klinike')")
	public ResponseEntity<Boolean> deleteKlinika(@PathVariable("idKlinike") Long idKlinike){
		klinikaRepository.deleteById(idKlinike);
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
}
