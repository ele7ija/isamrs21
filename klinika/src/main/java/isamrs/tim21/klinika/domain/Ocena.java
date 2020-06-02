package isamrs.tim21.klinika.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import isamrs.tim21.klinika.jsonSerialize.IdentitySerializable;

@Entity
public class Ocena implements IdentitySerializable {
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"zdravstveniKarton"})
    private Pacijent pacijent;

    @ManyToOne(fetch = FetchType.EAGER)
    private Klinika klinika;

    @ManyToOne(fetch = FetchType.EAGER)
    private Lekar lekar;

    @Column(name = "datum")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datum;

    @Column(name = "vrednost")
    private Integer vrednost;

    public Ocena() {}

    public Pacijent getPacijent() {
        return this.pacijent;
    }

    public void setPacijent(Pacijent pacijent) {
        this.pacijent = pacijent;
    }

    public Klinika getKlinika() {
        return this.klinika;
    }

    public void setKlinika(Klinika klinika) {
        this.klinika = klinika;
    }

    public Lekar getLekar() {
        return this.lekar;
    }

    public void setLekar(Lekar lekar) {
        this.lekar = lekar;
    }

    public Date getDatum() {
        return this.datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public Integer getVrednost() {
        return this.vrednost;
    }

    public void setVrednost(Integer vrednost) {
        this.vrednost = vrednost;
    }

    @Override
    public Long getId() {
        return this.id;
    }

}