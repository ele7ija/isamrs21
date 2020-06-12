package isamrs.tim21.klinika.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import isamrs.tim21.klinika.domain.Klinika;
import isamrs.tim21.klinika.domain.Lekar;
import isamrs.tim21.klinika.domain.MedicinskoOsoblje;
import isamrs.tim21.klinika.domain.Ocena;
import isamrs.tim21.klinika.domain.Pacijent;
import isamrs.tim21.klinika.repository.KlinikaRepository;
import isamrs.tim21.klinika.repository.KorisniciRepository;
import isamrs.tim21.klinika.repository.OcenaRepository;
import isamrs.tim21.klinika.repository.PacijentRepository;
import isamrs.tim21.klinika.repository.OsobljeRepository;

@Service
public class OcenaService {
    @Autowired
    private KlinikaRepository klinikaRepository;

    @Autowired
    private KorisniciRepository korisniciRepository;

    @Autowired
    private OsobljeRepository osobljeRepository;

    @Autowired
    private PacijentRepository pacijentRepository;

    @Autowired
    private OcenaRepository ocenaRepository;

	public List<Ocena> pronadjiOceneKlinike(Long idKlinike) {
        Klinika k = klinikaRepository.findById(idKlinike).get();
        return k.getOcene();
    }

	public List<Ocena> pronadjiOceneLekara(Long idLekara) {
        Lekar l = (Lekar) korisniciRepository.findById(idLekara).get();
        return l.getOcene();
    }
    
    @Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED)
    public Ocena kreirajOcenu(Ocena o) {
        Pacijent p = pacijentRepository.findByIdPacijentaPessimisticWrite(o.getPacijent().getId());
        if (p.getOcene() == null) {
            p.setOcene(new ArrayList<Ocena>());
        }
        o.setPacijent(pacijentRepository.findById(o.getPacijent().getId()).get());
        if (o.getKlinika() != null) {
            Klinika k = klinikaRepository.findByIdPessimisticWrite(o.getKlinika().getId());
            for (Ocena oc : p.getOcene()) {
                if (oc.getKlinika() != null) {
                    if (oc.getKlinika().getId()==k.getId()) {
                        p.getOcene().remove(oc);
                        ocenaRepository.delete(oc);
                        p.getOcene().add(o);
                        break;
                    }
                }
            }
            o.setKlinika(k);
            o.getKlinika().getOcene().add(o);
        }
        else{
            Lekar le = (Lekar) korisniciRepository.findByIdPessimisticWrite(o.getLekar().getId());
            // izbrisi pacijentovu ocenu za tog lekara
            for (Ocena oc : p.getOcene()) {
                if (oc.getLekar() != null) {
                    if (oc.getLekar().getId()==le.getId()) {
                        p.getOcene().remove(oc);
                        ocenaRepository.delete(oc);
                        p.getOcene().add(o);
                        break;
                    }
                }
            }
            o.setLekar(le);
            o.getLekar().getOcene().add(o);
        }
        return ocenaRepository.save(o);
	}

	public List<Ocena> pronadjiOcenePacijenta(String emailPacijenta) {
        Pacijent p = pacijentRepository.findByEmail(emailPacijenta);
        return p.getOcene();
	}
}