package isamrs.tim21.klinika.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import isamrs.tim21.klinika.jsonSerialize.IdentitySerializable;

@Entity
@Table(name="zdravstveni_karton")
public class ZdravstveniKarton implements IdentitySerializable{
	@Id
	private Long id;
	
	@OneToOne(fetch=FetchType.EAGER)
	@MapsId //znaci da je id pacijenta u tabeli zdravstvenog kartona i primarni i strani kljuc
	private Pacijent pacijent;
	
	@OneToMany(mappedBy="zdravstveniKarton", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Poseta> posete;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Pacijent getPacijent() {
		return pacijent;
	}

	public void setPacijent(Pacijent pacijent) {
		this.pacijent = pacijent;
	}

	public List<Poseta> getPosete() {
		return posete;
	}

	public void setPosete(List<Poseta> posete) {
		this.posete = posete;
	}
	
	//ostale stvari, pregledi, bolesti...
	
	
}
