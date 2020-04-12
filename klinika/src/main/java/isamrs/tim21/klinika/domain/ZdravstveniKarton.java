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

@Entity
@Table(name="zdravstveni_karton")
public class ZdravstveniKarton {
	@Id
	private Long id;
	
	@OneToOne(fetch=FetchType.EAGER)
	@MapsId //znaci da je id pacijenta u tabeli zdravstvenog kartona i primarni i strani kljuc
	private Pacijent pacijent;
	
	@OneToMany(mappedBy="zdravstveniKarton", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Poseta> posete;
	
	//ostale stvari, pregledi, bolesti...
}
