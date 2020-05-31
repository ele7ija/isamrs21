package isamrs.tim21.klinika;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KlinikaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KlinikaApplication.class, args);
	}

}
