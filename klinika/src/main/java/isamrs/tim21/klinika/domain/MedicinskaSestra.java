package isamrs.tim21.klinika.domain;

import java.util.Collection;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.springframework.security.core.GrantedAuthority;

@Entity
@DiscriminatorValue(value="MS")
public class MedicinskaSestra extends MedicinskoOsoblje{

	/**
	 * 
	 */
	private static final long serialVersionUID = -617917264867202685L;

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
