package domain;
// Generated 11-feb-2012 16:48:29 by Hibernate Tools 3.2.1.GA

import com.google.appengine.api.datastore.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;


/**
 * Doelgroep generated by hbm2java
 */
public class Leerling implements java.io.Serializable {
    private Key key;
    private Long id;
    private String naam;
    private Date startDatum;
    private Date eindDatum;
    private List<SchooljaarGroep> doelgroepen = new ArrayList<SchooljaarGroep>(0);

    public Leerling() {
    }

    public Leerling(String naam, Date startDatum) {
        this.naam = naam;
        this.startDatum = startDatum;
    }

    public Leerling(String naam, Date startDatum, Date eindDatum) {
        this.naam = naam;
        this.startDatum = startDatum;
        this.eindDatum = eindDatum;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaam() {
        return this.naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public List<SchooljaarGroep> getDoelgroepen() {
        return doelgroepen;
    }

    public Date getEindDatum() {
        return eindDatum;
    }

    public void setDoelgroepen(List<SchooljaarGroep> doelgroepen) {
        this.doelgroepen = doelgroepen;
    }
    
    public void addDoelgroep(Doelgroep doelgroep, int jaar, String groep) {
        //this.doelgroepen.add(new SchooljaarGroep(this, doelgroep, groep, jaar));
    }

    public Date getStartDatum() {
        return startDatum;
    }

    public void setEindDatum(Date eindDatum) {
        this.eindDatum = eindDatum;
    }

    public void setStartDatum(Date startDatum) {
        this.startDatum = startDatum;
    }

    
}
