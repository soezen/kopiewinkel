package domain;
// Generated 11-feb-2012 16:48:29 by Hibernate Tools 3.2.1.GA

import com.google.appengine.api.datastore.Key;
import domain.enums.Operator;
import javax.persistence.*;

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
    @OneToOne
    private Conditie linker;
    private String naam;
    private String commentaar;
    private Operator operator;
    private String expressie;

    public Conditie() {
    }
    
    /**
     * Create a Conditie with one sub conditie and an expressie.
     * Operator cannot be NOT, it has to be AND or OR.
     * 
     * @param naam
     * @param commentaar
     * @param expressie
     * @param operator
     * @param linker 
     */
    public Conditie(String naam, String commentaar, Conditie linker, Operator operator, String expressie) {
        this.naam = naam;
        this.commentaar = commentaar;
        this.linker = linker;
        this.operator = operator;
        this.expressie = expressie;
    }
    
    /**
     * Create a Conditie with only an expressie
     * 
     * @param naam
     * @param commentaar
     * @param expressie 
     * @param not Whether the expressie has to be true or not
     */
    public Conditie(String naam, String commentaar, String expressie, boolean not) {
        this.naam = naam;
        this.commentaar = commentaar;
        if (not) {
            operator = Operator.NOT;
        }
        this.expressie = expressie;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conditie getSubConditie() {
        return this.linker;
    }

    public void setSubConditie(Conditie subConditie) {
        this.linker = subConditie;
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

    public Operator getOperator() {
        return this.operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getExpressie() {
        return this.expressie;
    }

    public void setExpressie(String expressie) {
        this.expressie = expressie;
    }

    @Override
    public String toString() {
        return "CONDITIE [" + id + ", " + naam + ", " + linker + ", " + operator + ", " + expressie + "]";
    }
    
    
}
