package isamrs.tim21.klinika.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;



@Entity
@DiscriminatorValue(value="MS")
public class MedicinskaSestra extends MedicinskoOsoblje{

	/**
	 * 
	 */
	private static final long serialVersionUID = -617917264867202685L;

	
}
