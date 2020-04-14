package isamrs.tim21.klinika.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue(value="AC")
public class AdministratorCentra extends Korisnik{
	
	@OneToMany(cascade=CascadeType.ALL)
	private Set<ZahtevZaRegistraciju> zahtevi;
}
