package isamrs.tim21.klinika.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isamrs.tim21.klinika.domain.Poseta;
import isamrs.tim21.klinika.domain.ZdravstveniKarton;
import isamrs.tim21.klinika.repository.ZdravstveniKartonRepository;

@Service
public class ZdravstveniKartonService {
    @Autowired
	ZdravstveniKartonRepository zdravstveniKartonRepository;

	public void izbrisiPosetu(Poseta poseta) {
        ZdravstveniKarton zk = zdravstveniKartonRepository.findById(poseta.getZdravstveniKarton().getId()).get();
        for (Poseta p : zk.getPosete()) {
            if (p.getId() == poseta.getId()) {
                zk.getPosete().remove(p);
                break;
            }
        }
        zdravstveniKartonRepository.save(zk);
	}
}