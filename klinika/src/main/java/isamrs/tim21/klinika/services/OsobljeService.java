package isamrs.tim21.klinika.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.Authority;
import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.MedicinskoOsoblje;
import isamrs.tim21.klinika.repository.AuthorityRepository;
import isamrs.tim21.klinika.repository.OsobljeRepository;
import isamrs.tim21.klinika.repository.RadniKalendarRepository;

@Service
public class OsobljeService {
	@Autowired
	public PasswordEncoder passwordEncoder;
	
	@Autowired
	public OsobljeRepository osobljeRepository;
	
	@Autowired
	public AuthorityRepository authorityRepository;

	@Autowired
	public RadniKalendarRepository radniKalendarRepository;
	
	public MedicinskoOsoblje save(MedicinskoOsoblje osobljeToSave){
		osobljeToSave.setSifra(passwordEncoder.encode(osobljeToSave.getSifra()));
		osobljeToSave.setEnabled(true);
		Authority authority;
		if(osobljeToSave instanceof Lekar){
			authority = authorityRepository.findByName("lekar");
			Lekar l = (Lekar) osobljeToSave;
			System.out.println(l.getTipovi_pregleda().get(0).getId());
		}else{
			authority = authorityRepository.findByName("medicinska-sestra");
		}
		osobljeToSave.getAuthorities().add(authority);
		
		osobljeToSave.getRadniKalendar().setMedicinskoOsoblje(osobljeToSave);
		//radniKalendarRepository.save(osobljeToSave.getRadniKalendar());
		return osobljeRepository.save(osobljeToSave);
	}
}
