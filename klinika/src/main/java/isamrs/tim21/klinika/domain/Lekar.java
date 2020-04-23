package isamrs.tim21.klinika.domain;

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.jsonSerialize.IdentityListSerializer;


@Entity
@DiscriminatorValue(value="LE")
public class Lekar extends MedicinskoOsoblje{
	
	@ManyToMany(mappedBy="lekari")
	@JsonSerialize(using=IdentityListSerializer.class)
	private List<TipPregleda> tipovi_pregleda;
	
	@OneToMany(mappedBy="lekar", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JsonSerialize(using=IdentityListSerializer.class)
	private List<Pregled> pregledi;
	
	public List<TipPregleda> getTipovi_pregleda() {
		return tipovi_pregleda;
	}

	public void setTipovi_pregleda(List<TipPregleda> tipovi_pregleda) {
		this.tipovi_pregleda = tipovi_pregleda;
	}

	public List<Pregled> getPregledi() {
		return pregledi;
	}

	public void setPregledi(List<Pregled> pregledi) {
		this.pregledi = pregledi;
	}
	
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
