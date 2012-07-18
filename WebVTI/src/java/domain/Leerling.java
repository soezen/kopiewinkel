package domain;
// Generated 11-feb-2012 16:48:29 by Hibernate Tools 3.2.1.GA

import com.google.appengine.api.datastore.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import utils.GlobalValues;


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
    @ManyToOne(cascade= CascadeType.REMOVE)
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
    
    // TODO make it so that SchooljaarGroep has start and end date per student
    public void addGroep(SchooljaarGroep groep) {
        if (GlobalValues.HUIDIG_SCHOOLJAAR == groep.getSchooljaar()) {
            // groep is currentGroep if it is for the current school year
            if (currentGroep != null) {
                groepen.remove(currentGroep.getKey());
            }
            currentGroep = groep;           
        } else if (currentGroep == null) {
            // groep is current groep if it is not for the current school year when there is no current groep set yet
            currentGroep = groep;
        } else if (currentGroep.getSchooljaar() <= groep.getSchooljaar() && currentGroep.getSchooljaar() < GlobalValues.HUIDIG_SCHOOLJAAR) {
            // groep is current groep if it is not for the current school year 
            // and when there is already a current groep set 
            // when the current groep is of a year earlier than the new groep
            // but only if the current groep is before the current school year
            currentGroep = groep;
        }
        groepen.add(groep.getKey());
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

    public boolean isActiveOn(Date date) {
        boolean active = false;
        if (date.after(startDatum)) {
            if (eindDatum != null) {
                return date.before(eindDatum);
            } else {
                active = true;
            }
        }
        return active;
    }
    
    @Override
    public String toString() {
        return "LEERLING [" + id + ", " + naam + ", " + startDatum + "]";
    }

}
