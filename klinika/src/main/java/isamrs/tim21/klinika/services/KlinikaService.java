package isamrs.tim21.klinika.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.AdministratorKlinike;
import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.Korisnik;
import isamrs.tim21.klinika.domain.Lokacija;
import isamrs.tim21.klinika.domain.MedicinskoOsoblje;
import isamrs.tim21.klinika.dto.KlinikaDTO;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.LokacijaRepository;
import isamrs.tim21.klinika.security.TokenUtils;

@Service

public class KlinikaService {
	
	@Autowired
	private TokenUtils tokenUtils;
	
	@Autowired
	private KlinikaRepository klinikaRepository;

	@Autowired
	private LokacijaRepository lokacijaRepository;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Transactional(readOnly=true)
	private List<Klinika> findAll() {
		return klinikaRepository.findAll();
	}

	@Transactional(readOnly=true)
	public Klinika findKlinikaUlogovanog(HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = this.tokenUtils.getUsernameFromToken(token);
		Korisnik user = (Korisnik) this.userDetailsService.loadUserByUsername(email);
		if(user instanceof AdministratorKlinike){
			AdministratorKlinike admin = (AdministratorKlinike) user;
			return klinikaRepository.findById(admin.getKlinikaAdmina().getId()).orElse(null);
		}else{
			//lekar ili medicinska sestra
			MedicinskoOsoblje osoblje = (MedicinskoOsoblje) user;
			return klinikaRepository.findById(osoblje.getKlinika().getId()).orElse(null);
		}
	}

	@Transactional(readOnly=false)
	public Klinika addKlinika(KlinikaDTO klinikaDTO){
		Klinika klinika = new Klinika(klinikaDTO);
		Lokacija lokacija = lokacijaRepository.save(klinika.getLokacija());
		klinika.setLokacija(lokacija);
		klinikaRepository.save(klinika);
		return klinika;
	}

	@Transactional(readOnly=false)
	public Klinika updateKlinikaFromAdminCentra(Long idKlinike, KlinikaDTO klinikaDTO){
		Klinika klinika = klinikaRepository.findById(idKlinike).orElse(null);
		klinika.setNaziv(klinikaDTO.getNaziv());
		klinika.getLokacija().setAdresa(klinikaDTO.getAdresa());
		klinika.getLokacija().setGrad(klinikaDTO.getGrad());
		klinika.getLokacija().setDrzava(klinikaDTO.getDrzava());
		klinika.setOpis(klinikaDTO.getOpis());
		return klinika;
	}
}
