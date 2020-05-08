package isamrs.tim21.klinika.domain;

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import isamrs.tim21.klinika.dto.AdminKlinikeDTO;
import isamrs.tim21.klinika.jsonSerialize.IdentitySerializer;

@Entity
@DiscriminatorValue(value="AK")
public class AdministratorKlinike extends Korisnik{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6508036954142630963L;

	@JoinColumn(name="klinika_admina_id")
	@ManyToOne(fetch=FetchType.EAGER)
	@JsonSerialize(using=IdentitySerializer.class)
	private Klinika klinikaAdmina;

	public AdministratorKlinike(){}

	public AdministratorKlinike(AdminKlinikeDTO adminKlinikeDTO, Klinika klinika){
		super(adminKlinikeDTO.email, adminKlinikeDTO.sifra, adminKlinikeDTO.ime, adminKlinikeDTO.prezime);
		this.klinikaAdmina = klinika;
	}
	public Klinika getKlinikaAdmina() {
		return klinikaAdmina;
	}

	public void setKlinikaAdmina(Klinika klinikaAdmina) {
		this.klinikaAdmina = klinikaAdmina;
	}
	
}
