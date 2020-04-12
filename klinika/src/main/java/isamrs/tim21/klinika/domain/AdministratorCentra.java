package isamrs.tim21.klinika.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue(value="AC")
public class AdministratorCentra extends Korisnik{
	
}
