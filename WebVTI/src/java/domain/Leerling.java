package domain;
// Generated 11-feb-2012 16:48:29 by Hibernate Tools 3.2.1.GA

import com.google.appengine.api.datastore.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;


/**
 * Leerling
 */
@Entity
public class Leerling implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Key key;
    @Basic(optional=false)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="L_SEQUENCE")
    @SequenceGenerator(name="L_SEQUENCE")
    private Long id;
    private String naam;
    @Temporal(TemporalType.DATE)
    private Date startDatum;
    @Temporal(TemporalType.DATE)
    @Column(nullable=true)
    private Date eindDatum;
    @ManyToOne
    private SchooljaarGroep currentGroep;
    private List<Key> groepen = new ArrayList<Key>(0);

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

    public Key getKey() {
        return key;
    }
 
    public void setCurrentGroep(SchooljaarGroep groep) {
        groepen.add(groep.getKey());
        currentGroep = groep;
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

    public SchooljaarGroep getCurrentGroep() {
        return currentGroep;
    }
    
    public void setNaam(String naam) {
        this.naam = naam;
    }

    public List<Key> getGroepen() {
        return groepen;
    }

    public Date getEindDatum() {
        return eindDatum;
    }

    public void setGroepen(List<Key> groepen) {
        this.groepen = groepen;
    }
    
    public void addGroep(SchooljaarGroep groep) {
        
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

    @Override
    public String toString() {
        return "LEERLING [" + id + ", " + naam + ", " + startDatum + "]";
    }

}
