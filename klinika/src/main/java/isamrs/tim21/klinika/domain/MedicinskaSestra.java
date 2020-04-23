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

	
}
