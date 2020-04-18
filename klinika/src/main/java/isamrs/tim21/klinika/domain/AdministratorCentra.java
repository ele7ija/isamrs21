package isamrs.tim21.klinika.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.jsonSerialize.IdentityListSerializer;

@Entity
@DiscriminatorValue(value="AC")
public class AdministratorCentra extends Korisnik{
	
	@OneToMany(cascade=CascadeType.ALL)
	@JsonSerialize(using=IdentityListSerializer.class)
	private List<ZahtevZaRegistraciju> zahtevi;
}
