package domain;
// Generated 11-feb-2012 16:48:29 by Hibernate Tools 3.2.1.GA

import com.google.appengine.api.datastore.Key;
import java.util.List;
import javax.persistence.*;
import utils.ExpressionValidator;

/**
 * Conditie object.
 * 
 * Can be:
 * <ul>
 * <li>Single expression</li>
 * <li>Single expression + NOT operator</li>
 * <li>Combined expression + AND or OR operator</li>
 * </ul>
 */
@Entity
public class Conditie implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Key key;
    @Column(nullable=false)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="C_SEQUENCE")
    @SequenceGenerator(name="C_SEQUENCE")
    private Long id;
    private String naam;
    private String commentaar;
    private String expressie;
    @OneToMany(mappedBy = "conditie")
    private List<Prijs> prijzen;

    public Conditie() {
    }
    
    /**
     * 
     * @param naam
     * @param commentaar
     * @param expressie
     */
    public Conditie(String naam, String commentaar, String expressie) {
        this.naam = naam;
        this.commentaar = commentaar;
        this.expressie = expressie;
    }

    /**
     * Checks if this Conditie is met given the selected opties.
     * 
     * @param opties
     * @return 
     */
    public boolean isApplicable(List<Optie> opties) {
        return ExpressionValidator.validate(expressie, opties);
    }
    
    public Key getKey() {
        return key;
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

    public String getCommentaar() {
        return this.commentaar;
    }

    public void setCommentaar(String commentaar) {
        this.commentaar = commentaar;
    }

    public String getExpressie() {
        return this.expressie;
    }

    public void setExpressie(String expressie) {
        this.expressie = expressie;
    }

    public void setPrijzen(List<Prijs> prijzen) {
        this.prijzen = prijzen;
    }

    public List<Prijs> getPrijzen() {
        return prijzen;
    }
    
    public void addPrijs(Prijs prijs) {
        prijzen.add(prijs);
    }

    @Override
    public String toString() {
        return "CONDITIE [" + id + ", " + naam + ", " + expressie + "]";
    }
    
    
}
