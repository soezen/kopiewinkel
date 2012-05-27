package domain;
// Generated 11-feb-2012 16:48:29 by Hibernate Tools 3.2.1.GA

import com.google.appengine.api.datastore.Key;
import domain.interfaces.Constrained;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 * OpdrachtType:
 * 
 * bepaald wat er allemaal nodig is om een opdracht aanvraag aan te maken.
 * Gebruikers en GebruikerTypes moet gerechtigd zijn om het opdrachtType te kunnen gebruiken.
 */
@Entity
public class OpdrachtType implements java.io.Serializable, Constrained {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Key key;
    @Basic(optional=false)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="P_SEQUENCE")
    @SequenceGenerator(name="P_SEQUENCE")
    private Long id;
    @ManyToOne
    private PrijsKlasse prijsKlasse;
    private String naam;
    private String omschrijving;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date van;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(nullable=true)
    private Date tot;
    private List<OpdrachtTypeInput> inputVelden = new ArrayList<OpdrachtTypeInput>(0);

    public OpdrachtType() {
    }

    public OpdrachtType(PrijsKlasse prijsKlasse, String naam, String omschrijving, Date van) {
        this.prijsKlasse = prijsKlasse;
        this.naam = naam;
        this.van = van;
        this.omschrijving = omschrijving;
    }

    public OpdrachtType(PrijsKlasse prijsKlasse, String naam, String omschrijving, 
            Date van, Date tot, List<OpdrachtTypeInput> inputVelden) {
        this.prijsKlasse = prijsKlasse;
        this.naam = naam;
        this.van = van;
        this.tot = tot;
        this.inputVelden = inputVelden;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Key getKey() {
        return key;
    }

    public List<OpdrachtTypeInput> getInputVelden() {
        return inputVelden;
    }

    public void addInputVeld(OpdrachtTypeInput input) {
        inputVelden.add(input);
    }
    
    public PrijsKlasse getPrijsKlasse() {
        return this.prijsKlasse;
    }

    public void setPrijsKlasse(PrijsKlasse prijsKlasse) {
        this.prijsKlasse = prijsKlasse;
    }

    public String getNaam() {
        return this.naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public Date getVan() {
        return this.van;
    }

    public void setVan(Date van) {
        this.van = van;
    }

    public Date getTot() {
        return this.tot;
    }

    public void setTot(Date tot) {
        this.tot = tot;
    }

    public void setInputVelden(List<OpdrachtTypeInput> inputVelden) {
        this.inputVelden = inputVelden;
    }

    @Override
    public String toString() {
        return "OPDRACHTTYPE [" + id + ", " + naam + ", " + prijsKlasse + ", " + van + ", " + inputVelden.size() + "]";
    }
}
