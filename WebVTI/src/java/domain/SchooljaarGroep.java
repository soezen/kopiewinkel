package domain;
// Generated 11-feb-2012 16:48:29 by Hibernate Tools 3.2.1.GA

import com.google.appengine.api.datastore.Key;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;



/**
 * Schooljaar groep stelt een groep van een doelgroep voor van een bepaald schooljaar.
 * Deze groep bevat verschillende leerlingen en leerlingen kunnen deel uit maken van verschillende groepen, maar nooit tegelijkertijd.
 */
@Entity
public class SchooljaarGroep implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Key key;
    @ManyToOne
    private Doelgroep doelgroep;
    private String groep;
    private int schooljaar;
    @Basic
    private List<Leerling> leerlingen = new ArrayList<Leerling>();
    
    public SchooljaarGroep() {
    }

    public SchooljaarGroep(Doelgroep doelgroep, String groep, int schooljaar) {
        this.doelgroep = doelgroep;
        this.groep = groep;
        this.schooljaar = schooljaar;
    }

    public Key getKey() {
        return key;
    }

    public Doelgroep getDoelgroep() {
        return doelgroep;
    }

    public String getGroep() {
        return groep;
    }

    public int getSchooljaar() {
        return schooljaar;
    }

    public List<Leerling> getLeerlingen() {
        return leerlingen;
    }

    public void setDoelgroep(Doelgroep doelgroep) {
        this.doelgroep = doelgroep;
    }

    public void setGroep(String groep) {
        this.groep = groep;
    }

    public void setLeerling(List<Leerling> leerlingen) {
        this.leerlingen = leerlingen;
    }
    
    public void addLeerling(Leerling leerling) {
        leerlingen.add(leerling);
    }

    public void setSchooljaar(int schooljaar) {
        this.schooljaar = schooljaar;
    }

    @Override
    public String toString() {
        return "GROEP [" + schooljaar + ", " + doelgroep + " " + groep + ", " + leerlingen.size() + "]";
    }

    
}
