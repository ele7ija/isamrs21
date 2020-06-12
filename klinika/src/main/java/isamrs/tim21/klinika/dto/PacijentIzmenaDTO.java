package isamrs.tim21.klinika.dto;

import isamrs.tim21.klinika.domain.Pacijent;

public class PacijentIzmenaDTO {
    Pacijent pacijent;
    String poslednjaSifra;

    public PacijentIzmenaDTO(){}

    public PacijentIzmenaDTO(Pacijent p, String s) {
        this.pacijent = p;
        this.poslednjaSifra = s;
    }

    public Pacijent getPacijent() {
        return this.pacijent;
    }

    public void setPacijent(Pacijent pacijent) {
        this.pacijent = pacijent;
    }

    public String getPoslednjaSifra() {
        return this.poslednjaSifra;
    }

    public void setPoslednjaSifra(String poslednjaSifra) {
        this.poslednjaSifra = poslednjaSifra;
    }


}