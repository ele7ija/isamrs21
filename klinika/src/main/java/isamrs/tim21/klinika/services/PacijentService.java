package isamrs.tim21.klinika.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.Authority;
import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.Pacijent;
import isamrs.tim21.klinika.domain.Pregled;
import isamrs.tim21.klinika.dto.CustomResponse;
import isamrs.tim21.klinika.dto.PacijentDTO;
import isamrs.tim21.klinika.dto.PacijentIzmenaDTO;
import isamrs.tim21.klinika.repository.OsobljeRepository;
import isamrs.tim21.klinika.repository.PacijentRepository;

@Service
public class PacijentService {
	@Autowired
	private PacijentRepository pacijentRepository;
	
	@Autowired
	private OsobljeRepository osobljeRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AutentifikacijaService autentifikacijaServis;

	@Autowired
	public CustomUserDetailsService userDetailsService;
	
	public Pacijent findByEmail(String email) throws UsernameNotFoundException {
		Pacijent p = pacijentRepository.findByEmail(email);
		return p;
	}

	public Pacijent save(PacijentDTO pacijentDTO) {
		Pacijent p = new Pacijent(pacijentDTO);
		// pre nego sto postavimo lozinku u atribut hesiramo je
		p.setSifra(passwordEncoder.encode(pacijentDTO.getSifra()));
		p.setEnabled(false);
		
		Authority auth = autentifikacijaServis.findByName("pacijent");
		if (auth == null) {
			throw new RuntimeException("Pacijent Authority not found");
		}
		// u primeru se registruju samo obicni korisnici i u skladu sa tim im se i dodeljuje samo rola USER
		p.getAuthorities().add(auth);
		
		p = pacijentRepository.save(p);
		return p;
	}

	@Transactional(readOnly=true)
	public List<Pacijent> findAll() {
		return pacijentRepository.findAll();
	}

	@Transactional(readOnly=true, isolation=Isolation.REPEATABLE_READ)
	public Boolean pravoPristupa(Long idPacijenta, Long idLekara){
		/*
		 * Repeatable read zato sto moze da se desi da admin klinike izvrsi brisanje nekog pregleda u toku ove transakcije
		 * i da samim tim lekar vise nema pravo pristupa zdravstvenom kartonu pacijenta
		 * ovaj isolation type se koristi jer PessimisticLockScope i kaskadno zakljucavanje nije podrzano u hibernate-u
		 */
		Lekar lekar = (Lekar) osobljeRepository.findByIdPessimisticReadLockExtended(idLekara);
		for(Pregled pregled : lekar.getPregledi()){
			if(pregled.getPoseta() == null)
				continue;
			if(pregled.getPoseta().getZdravstveniKarton().getPacijent().getId() == idPacijenta)
				return true;
		}
		return false;
	}

	public CustomResponse<Pacijent> izmeni(PacijentIzmenaDTO p) {
		// sifra
		Pacijent retval = (Pacijent) pacijentRepository.findByEmail(p.getPacijent().getEmail());
		// retval.setIme(sestra.getSestra().getIme());
		// retval.setPrezime(sestra.getSestra().getPrezime());
		if(!p.getPacijent().getSifra().equals(retval.getSifra())){
			retval.setPoslednjaPromenaSifre(new Date());
			retval = (Pacijent) userDetailsService.findUserAndChangePassword(p.getPacijent().getId(), p.getPoslednjaSifra(), p.getPacijent().getSifra());
		}
		retval.setAdresa(p.getPacijent().getAdresa());
		retval.setGrad(p.getPacijent().getGrad());
		retval.setDrzava(p.getPacijent().getDrzava());
		retval.setBrojTelefona(p.getPacijent().getBrojTelefona());
		retval.setIme(p.getPacijent().getIme());
		retval.setPrezime(p.getPacijent().getPrezime());

		retval = pacijentRepository.save(retval);
		return new CustomResponse<Pacijent>(retval, true, "OK.");
	}
	
}
