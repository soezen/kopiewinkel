package domain;
// Generated 18-feb-2012 18:51:31 by Hibernate Tools 3.2.1.GA

import com.google.appengine.api.datastore.Key;
import domain.interfaces.Constrained;
import javax.persistence.*;

/**
 * MenuItem generated by hbm2java
 */
@Entity
public class MenuItem implements java.io.Serializable, Constrained {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "keyGenerator")
    private Key key;
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MI_SEQUENCE")
    @SequenceGenerator(name = "MI_SEQUENCE")
    private Long id;
    private String naam;
    private String link;
    private boolean extern;
    private int volgorde;

    public MenuItem() {
    }

    public MenuItem(String naam, String link, boolean extern, int volgorde) {
        this.naam = naam;
        this.link = link;
        this.extern = extern;
        this.volgorde = volgorde;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getNaam() {
        return this.naam;
    }

    public Key getKey() {
        return key;
    }
    
    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isExtern() {
        return this.extern;
    }

    public void setExtern(boolean extern) {
        this.extern = extern;
    }

    public int getVolgorde() {
        return this.volgorde;
    }

    public void setVolgorde(int volgorde) {
        this.volgorde = volgorde;
    }

    @Override
    public String toString() {
        return "MENUITEM [" + id + ", " + naam + ", " + link + ", " + extern + ", " + volgorde + "]";
    }
    
    
}
