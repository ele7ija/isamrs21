package isamrs.tim21.klinika.domain;

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.jsonSerialize.IdentitySerializer;

@Entity
@DiscriminatorValue(value="AK")
public class AdministratorKlinike extends Korisnik{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6508036954142630963L;

	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonSerialize(using=IdentitySerializer.class)
	private Klinika klinika;

	public Klinika getKlinika() {
		return klinika;
	}

	public void setKlinika(Klinika klinika) {
		this.klinika = klinika;
	}
	
}
