package domain;
// Generated 11-feb-2012 16:48:29 by Hibernate Tools 3.2.1.GA

import com.google.appengine.api.datastore.Key;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * PrijsKlasse generated by hbm2java
 */
@Entity
public class PrijsKlasse implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CS_SEQUENCE")
    @SequenceGenerator(name = "CS_SEQUENCE")
    protected Long id;
    private String naam;
    @Basic
    private List<Key> prijzen = new ArrayList<Key>(0);
    private List<OpdrachtType> opdrachtTypes = new ArrayList<OpdrachtType>(0);

    public PrijsKlasse() {
    }

    public PrijsKlasse(String naam) {
        this.naam = naam;
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

    public List<Key> getPrijzen() {
        return this.prijzen;
    }

    public void addPrijs(Prijs prijs) {
        prijzen.add(prijs.getKey());
    }
    
    public void setPrijzen(List<Key> prijzen) {
        this.prijzen = prijzen;
    }

    public List<OpdrachtType> getOpdrachtTypes() {
        return this.opdrachtTypes;
    }

    public void setOpdrachtTypes(List<OpdrachtType> opdrachtTypes) {
        this.opdrachtTypes = opdrachtTypes;
    }

    @Override
    public String toString() {
        return "PRIJSKLASSE [" + id + ", " + naam + "]";
    }
    
    
}
