package isamrs.tim21.klinika.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.Pacijent;
import isamrs.tim21.klinika.domain.Poseta;
import isamrs.tim21.klinika.domain.Pregled;
import isamrs.tim21.klinika.domain.UpitZaPregled;
import isamrs.tim21.klinika.domain.ZdravstveniKarton;
import isamrs.tim21.klinika.dto.PosetaDTO;
import isamrs.tim21.klinika.dto.PosetaDTO2;
import isamrs.tim21.klinika.dto.PosetaDTO3;
import isamrs.tim21.klinika.repository.KorisniciRepository;
import isamrs.tim21.klinika.repository.PosetaRepository;
import isamrs.tim21.klinika.repository.PregledRepository;
import isamrs.tim21.klinika.repository.ZdravstveniKartonRepository;
import isamrs.tim21.klinika.security.TokenUtils;

@Service
public class PosetaService {
	@Autowired 
	PosetaRepository posetaRepository;
	
	@Autowired 
	PregledRepository pregledRepository;
	
	@Autowired
	ZdravstveniKartonRepository zdravstveniKartonRepository;
	
	@Autowired
	KorisniciRepository korisniciRepository;
	
	@Autowired
	TokenUtils tokenUtils;	// Za generisanje username-a od tokena

	@Autowired
	ZdravstveniKartonService zdravstveniKartonService;

	@Autowired
	UpitZaPregledeService upitZaPregledeService;

	@Autowired
	PregledService pregledService;

	@Autowired
	ReceptService receptService;
	
	//@SuppressWarnings("rawtypes")
	public Poseta kreirajNovuPosetu(PosetaDTO dto, HttpServletRequest req) {
		// proveri da li postoji pregled sa tim id-jem
		if (!pregledRepository.existsById(dto.getPregledId())) {
			return null;
		}
		Pregled pregled = pregledRepository.findById(dto.getPregledId()).get();
		if (pregled.getPoseta() != null) {
			return null;
		}
		// dobavi korisnika
		String token = tokenUtils.getToken(req);
		String username = tokenUtils.getUsernameFromToken(token);
		Pacijent pacijent = (Pacijent) korisniciRepository.findByEmail(username);
		// dobavi zdravstveni karton, ako nema karton napravi
		ZdravstveniKarton zk;
		if (!zdravstveniKartonRepository.existsById(pacijent.getId())) {
			zk = new ZdravstveniKarton();
			zk.setPacijent(pacijent);
			pacijent.setZdravstveniKarton(zk);
			korisniciRepository.save(pacijent);
			zk = pacijent.getZdravstveniKarton();
		}
		else {
			zk = zdravstveniKartonRepository.findById(pacijent.getId()).get();
		}
		// kreiraj posetu na osnovu kartona, pregleda itd
		Poseta p = new Poseta();
		p.setBolest(null);
		p.setOpis(dto.getOpis());
		p.setPregled(pregled);
		p.setZdravstveniKarton(zk);
		pregled.setPoseta(p);
		posetaRepository.save(p);
		pregledRepository.save(pregled);
		if (zk.getPosete() == null) {
			zk.setPosete(new ArrayList<>());
		}
		zk.getPosete().add(p);
		zdravstveniKartonRepository.save(zk);
		return p;
	}
	
	public Poseta kreirajNovuPosetu(PosetaDTO2 dto) {
		// proveri da li postoji pregled sa tim id-jem
		if (!pregledRepository.existsById(dto.getPregledId())) {
			return null;
		}
		Pregled pregled = pregledRepository.findById(dto.getPregledId()).get();
		if (pregled.getPoseta() != null) {
			return null;
		}
		// dobavi korisnika
		Pacijent pacijent = (Pacijent) korisniciRepository.findByEmail(dto.getEmail());
		if (pacijent == null) {
			return null;
		}
		// dobavi zdravstveni karton, ako nema karton napravi
		ZdravstveniKarton zk;
		if (!zdravstveniKartonRepository.existsById(pacijent.getId())) {
			zk = new ZdravstveniKarton();
			zk.setPacijent(pacijent);
			pacijent.setZdravstveniKarton(zk);
			korisniciRepository.save(pacijent);
			zk=pacijent.getZdravstveniKarton();
		}
		else {
			zk = zdravstveniKartonRepository.findById(pacijent.getId()).get();
		}
		// kreiraj posetu na osnovu kartona, pregleda itd
		Poseta p = new Poseta();
		p.setBolest(null);
		p.setOpis(dto.getOpis());
		p.setPregled(pregled);
		p.setZdravstveniKarton(zk);
		pregled.setPoseta(p);
		// sacuvaj i vrati posetu
		posetaRepository.save(p);
		pregledRepository.save(pregled);
		if (zk.getPosete() == null) {
			zk.setPosete(new ArrayList<>());
		}
		zk.getPosete().add(p);
		return p;
	}
	
	public Poseta kreirajNovuPosetu(UpitZaPregled u) {
		// proveri da li postoji pregled sa tim id-jem
		Pregled pregled = null;
		if (u.getUnapredDefinisaniPregled() != null) {
			pregled = u.getUnapredDefinisaniPregled();
		}
		else {
			// KREIRANJE PREGLEDA - nece se nikad desiti?
		}
		
		if (null != pregled.getPoseta()) {
			return null;
		}
		// dobavi korisnika
		Pacijent pacijent = u.getPacijent();
		if (pacijent == null) {
			return null;
		}
		// dobavi zdravstveni karton, ako nema karton napravi
		ZdravstveniKarton zk;
		if (!zdravstveniKartonRepository.existsById(pacijent.getId())) {
			zk = new ZdravstveniKarton();
			zk.setPacijent(pacijent);
			pacijent.setZdravstveniKarton(zk);
			korisniciRepository.save(pacijent);
			zk=pacijent.getZdravstveniKarton();
		}
		else {
			zk = zdravstveniKartonRepository.findById(pacijent.getId()).get();
		}
		// kreiraj posetu na osnovu kartona, pregleda itd
		Poseta p = new Poseta();
		p.setBolest(null);
		p.setOpis(u.getOpis());
		p.setPregled(pregled);
		p.setZdravstveniKarton(zk);
		pregled.setPoseta(p);
		// sacuvaj i vrati posetu
		posetaRepository.save(p);
		pregledRepository.save(pregled);
		if (zk.getPosete() == null) {
			zk.setPosete(new ArrayList<>());
		}
		zk.getPosete().add(p);
		return p;
	}
	
	public List<Poseta> nadjiSvePosete(HttpServletRequest req){
		// dobavi korisnika
		// vrati posete
		String token = tokenUtils.getToken(req);
		String username = tokenUtils.getUsernameFromToken(token);
		Pacijent pacijent = (Pacijent) korisniciRepository.findByEmail(username);
		ZdravstveniKarton zk;
		if (!zdravstveniKartonRepository.existsById(pacijent.getId())) {
			zk = new ZdravstveniKarton();
			zk.setPacijent(pacijent);
			pacijent.setZdravstveniKarton(zk);
			korisniciRepository.save(pacijent);
			zk=pacijent.getZdravstveniKarton();
		}
		else {
			zk = zdravstveniKartonRepository.findById(pacijent.getId()).get();
		}
		if (zk.getPosete() == null) {
			zk.setPosete(new ArrayList<>());
		}
		return zk.getPosete();
	}

	public List<Poseta> nadjiNerealizovanePosete(HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String username = tokenUtils.getUsernameFromToken(token);
		Pacijent pacijent = (Pacijent) korisniciRepository.findByEmail(username);
		ZdravstveniKarton zk;
		if (!zdravstveniKartonRepository.existsById(pacijent.getId())) {
			zk = new ZdravstveniKarton();
			zk.setPacijent(pacijent);
			pacijent.setZdravstveniKarton(zk);
			korisniciRepository.save(pacijent);
			zk=pacijent.getZdravstveniKarton();
		}
		else {
			zk = zdravstveniKartonRepository.findById(pacijent.getId()).get();
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		List<Poseta> nerPosete = new ArrayList<>();
		if (zk.getPosete() == null) {
			zk.setPosete(new ArrayList<>());
		}
		for (Poseta p : zk.getPosete()) {
			if (p.getPregled().getPocetakPregleda().after(timestamp)) {
				nerPosete.add(p);
			}
		}
		return nerPosete;
	}

	public List<?> updatePoseta (PosetaDTO3 posetaDTO) {
		//ako je visina null, to znaci da je radjena samo izmena opisa pregleda na frontu
		//tada treba samo opis pregleda izmeniti  
		if( null == posetaDTO.getVisina()){
			Poseta poseta = posetaRepository.findById(posetaDTO.getPosetaId()).orElse(null);
			if( null == poseta) 
				return null;
			poseta.setOpis(posetaDTO.getOpis());
			posetaRepository.save(poseta);
			ZdravstveniKarton k = poseta.getZdravstveniKarton();

			//vraca listu u kojoj se nalaze i poseta i karton
			List lista = new ArrayList();
			lista.add(poseta);
			lista.add(k);
			return  lista;
		}
		else{
			//update poseta
			 Poseta poseta = posetaRepository.findById(posetaDTO.getPosetaId()).orElse(null);
			 if( null == poseta) 
			 	return null;
			poseta.setBolest(posetaDTO.getBolest());
			poseta.setLekovi(posetaDTO.getLekovi());
			poseta.setOpis(posetaDTO.getOpis());
			posetaRepository.save(poseta);
			//update zdravstveni karton
			ZdravstveniKarton k = poseta.getZdravstveniKarton();
			k.setDioptrija(posetaDTO.getDioptrija());
			k.setKrvnaGrupa(posetaDTO.getKrvnaGrupa());
			k.setVisina(posetaDTO.getVisina());
			k.setTezina(posetaDTO.getTezina());
			zdravstveniKartonRepository.save(k);
			//kreiraj recept
			receptService.saveRecept(poseta);

			//vraca listu u kojoj se nalaze i poseta i karton
			List lista = new ArrayList();
			lista.add(poseta);
			lista.add(k);
			return  lista;
		}



	}

	public Poseta izbrisiPosetu(Poseta p) {
		// pronadjiPosetu
		Poseta poseta = posetaRepository.findById(p.getId()).get();
		// izbrisi iz kartona
		zdravstveniKartonService.izbrisiPosetu(poseta);
		poseta.setZdravstveniKarton(null);
		// izbrisi upit
		upitZaPregledeService.izbrisiPosetu(poseta);
		// izbrisi iz pregleda
		pregledService.izbrisiPosetu(poseta);
		poseta.setPregled(null);

		posetaRepository.delete(poseta);
		return null;
	}
}
