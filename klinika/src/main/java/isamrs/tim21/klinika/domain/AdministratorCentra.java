package isamrs.tim21.klinika.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.dto.AdminCentraDTO;
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

	public List<ZahtevZaRegistraciju> getZahtevi(){
		return zahtevi;
	}

	public AdministratorCentra() {}

	public AdministratorCentra(AdminCentraDTO adminCentraDTO){
		super(adminCentraDTO.getEmail(), adminCentraDTO.getSifra(), adminCentraDTO.getIme(),adminCentraDTO.getPrezime());
	}

	
}
