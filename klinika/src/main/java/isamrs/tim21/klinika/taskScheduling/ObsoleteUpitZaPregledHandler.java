package isamrs.tim21.klinika.taskScheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import isamrs.tim21.klinika.services.UpitZaPregledeService;
import isamrs.tim21.klinika.domain.UpitZaPregled;

@Component
public class ObsoleteUpitZaPregledHandler {


  @Autowired
  UpitZaPregledeService upitZaPregledService;

	@Scheduled(fixedRate = 30000) //pokreni se svakih 30 sekundi
	public void handle() {
		List<UpitZaPregled> sviUpiti = upitZaPregledService.sviUpiti(); //upiti se vracaju u pessimistic force increment rezimu
	}
}