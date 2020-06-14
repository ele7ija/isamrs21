package isamrs.tim21.klinika.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import isamrs.tim21.klinika.jsonSerialize.IdentitySerializable;

@Entity
@Table(name="radni_kalendar")
public class RadniKalendar implements IdentitySerializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch=FetchType.EAGER)
	@MapsId
	@JsonIgnoreProperties(value={"radniKalendar"}, allowSetters=true)
	private MedicinskoOsoblje medicinskoOsoblje;
	
	@OneToMany(mappedBy="radniKalendar", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<ZahtevZaGodisnji> zahteviZaGodisnjiOdmor;
	
	@Column(name="dnevno_radno_vreme", nullable=false)
	private int dnevnoRadnoVremeSati;

	public List<ZahtevZaGodisnji> getZahteviZaGodisnjiOdmor() {
		return zahteviZaGodisnjiOdmor;
	}

	public void setZahteviZaGodisnjiOdmor(List<ZahtevZaGodisnji> zahteviZaGodisnjiOdmor) {
		this.zahteviZaGodisnjiOdmor = zahteviZaGodisnjiOdmor;
	}

	public MedicinskoOsoblje getMedicinskoOsoblje() {
		return medicinskoOsoblje;
	}

	public void setMedicinskoOsoblje(MedicinskoOsoblje medicinskoOsoblje) {
		this.medicinskoOsoblje = medicinskoOsoblje;
	}

	public int getDnevnoRadnoVremeSati() {
		return dnevnoRadnoVremeSati;
	}

	public void setDnevnoRadnoVremeSati(int dnevnoRadnoVremeSati) {
		this.dnevnoRadnoVremeSati = dnevnoRadnoVremeSati;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
