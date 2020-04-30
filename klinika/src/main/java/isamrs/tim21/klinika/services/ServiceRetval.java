package isamrs.tim21.klinika.services;

/**
 * Genericka klasa za vracanje podataka od strane servisa.
 * Ukoliko kod ima pozitivno znacenje (operacija je uspela) tada 
 * se u payloadu nalazi adekvatan objekat.
 * Ukoliko kod ima negativno znacenje (operacija nije uspela) tada 
 * se u payloadu nalazi komentar .
 * */
public class ServiceRetval<T> {
	private T payload;
	private int code;
	
	public ServiceRetval(T pay, int c) {
		payload = pay;
		code = c;
	}
}
