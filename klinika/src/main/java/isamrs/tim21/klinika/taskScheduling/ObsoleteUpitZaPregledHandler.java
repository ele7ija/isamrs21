package isamrs.tim21.klinika.taskScheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import isamrs.tim21.klinika.services.UpitZaPregledeService;
import isamrs.tim21.klinika.services.SalaService;
import isamrs.tim21.klinika.domain.UpitZaPregled;
import isamrs.tim21.klinika.domain.Sala;
import java.util.List;
import java.util.Date;

@Component
public class ObsoleteUpitZaPregledHandler {


  @Autowired
	UpitZaPregledeService upitZaPregledService;

	@Autowired
	SalaService salaService;
	
	static final Long GAP = 30 * 1000L; //samo za testiranje

	//@Scheduled(cron="0 0 0 * * ?") ovo cemo zapravo koristiti, jer to ce pokrenuti ovu metodu svaki put u 00:00
	@Scheduled(fixedRate = 30000) //pokreni se svakih 30 sekundi, samo za testiranje
	public void handle() {
		List<UpitZaPregled> sviUpiti = upitZaPregledService.sviUpiti(); //upiti se vracaju u pessimistic force increment rezimu
		for(UpitZaPregled upit: sviUpiti){
			Date now = new Date();
			if(Math.abs(now.getTime() - upit.getDatumKreiranjaUpita().getTime()) > GAP){ //ovaj if uslov samo za testiranje
				if(upit.getUnapredDefinisaniPregled() != null && !upit.getAdminObradio()){
					//upit za unapred definisani pregled
					//ove upite po defaultu prihvatas ako ih admin ne obradi u datom vremenskom intervalu
					upit.setAdminObradio(true);
					upit.setOdobren(true);
					upitZaPregledService.save(upit);
				}else{
					continue;
					//TODO
					//upit za pregled nije unapred definisani
					/*for(Sala sala : salaService.getAllSale(upit.getLekar().getKlinika().getId())){
						upit.setSala(sala);
					}*/
				}
			}
		}
	}
}