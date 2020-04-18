package isamrs.tim21.klinika.domain;

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.jsonSerialize.IdentityListSerializer;

@Entity
@DiscriminatorValue(value="AC")
public class AdministratorCentra extends Korisnik{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7205443171988738622L;
	@OneToMany(cascade=CascadeType.ALL)
	@JsonSerialize(using=IdentityListSerializer.class)
	private List<ZahtevZaRegistraciju> zahtevi;
	
	@Override
	public List<Authority> getAuthorities() {
		return getAuthorities();
	}

	@Override
	public String getPassword() {
		return getPassword();
	}

	@Override
	public String getUsername() {
		return getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
