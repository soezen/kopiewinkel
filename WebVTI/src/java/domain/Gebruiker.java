package domain;
// Generated 11-feb-2012 16:48:29 by Hibernate Tools 3.2.1.GA

import com.google.appengine.api.datastore.Key;
import domain.interfaces.Constrained;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 * Gebruiker van de applicatie.
 * 
 * Heeft rechten op bepaalde gegevens en acties. Dit wordt bepaald per gebruiker of per gebruiker type.
 * 
 */
@Entity
public class Gebruiker implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Key key;
    @Basic(optional=false)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="G_SEQUENCE")
    @SequenceGenerator(name="G_SEQUENCE")
    private Long id;
    @ManyToOne(cascade={CascadeType.REFRESH}, optional=false)
    private GebruikerType gebruikerType;
    @Basic(optional=false)
    private String naam;
    private String wachtwoord;
    private String email;
    @Basic(optional=false)
    private boolean actief;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date laatstIngelogd;
    @Temporal(TemporalType.DATE)
    private Date creationDate;
//    private Set<LayoutProperty> layoutProperties = new HashSet<LayoutProperty>(0);
//    @OneToMany
//    private List<Doelgroep> doelgroepen = new ArrayList<Doelgroep>(0);
//    private Set<Opdracht> opdrachten = new HashSet<Opdracht>(0);
//    private Set<Criteria> criteria = new HashSet<Criteria>(0);
//    private Set<Criteria> startpagina = new HashSet<Criteria>(0);
//    @OneToMany(mappedBy = "gebruiker")
    private List<Melding> meldingen = new ArrayList<Melding>(0);
    private List<Long> rechten = new ArrayList<Long>(0);
    @OneToMany(mappedBy = "eigenaar")
    private List<Aanvraag> aanvragen;
    @Basic
    private List<Long> toewijzingen = new ArrayList<Long>();

    public Gebruiker() {
    }
    
    public Gebruiker(GebruikerType gebruikerType, String naam) {
        this.gebruikerType = gebruikerType;
        this.naam = naam;
        this.creationDate = new Date(System.currentTimeMillis());
        this.actief = true;
        this.laatstIngelogd = creationDate;
    }

    public Long getId() {
        return this.id;
    }

    public Key getKey() {
        return key;
    }

    public Date getCreationDate() {
        return creationDate;
    }
    
    

    public void setId(Long id) {
        this.id = id;
    }

    public GebruikerType getGebruikerType() {
        return this.gebruikerType;
    }

    public final void setGebruikerType(GebruikerType gebruikerType) {
        this.gebruikerType = gebruikerType;
    }

    public String getNaam() {
        return this.naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getWachtwoord() {
        return this.wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActief() {
        return this.actief;
    }

    public void setActief(boolean actief) {
        this.actief = actief;
    }

    public void setRechten(List<Long> rechten) {
        this.rechten = rechten;
    }

    public List<Long> getRechten() {
        return rechten;
    }

    public void addRecht(Constrained recht) {
        rechten.add(recht.getId());
    }

    public void setToewijzingen(List<Long> toewijzingen) {
        this.toewijzingen = toewijzingen;
    }

    public List<Long> getToewijzingen() {
        return toewijzingen;
    }
    
    public void addToewijzing(Doelgroep doelgroep) {
        toewijzingen.add(doelgroep.getId());
    }
    
//    public Set<LayoutProperty> getLayoutProperties() {
//        return this.layoutProperties;
//    }
//
//    public void setLayoutProperties(Set<LayoutProperty> layoutProperties) {
//        this.layoutProperties = layoutProperties;
//    }
////
//    public List<Doelgroep> getDoelgroepen() {
//        return this.doelgroepen;
//    }
//
//    public void setDoelgroepen(List<Doelgroep> doelgroepen) {
//        this.doelgroepen = doelgroepen;
//    }

//    public Set<Opdracht> getOpdrachten() {
//        return this.opdrachten;
//    }
//
//    public void setOpdrachten(Set<Opdracht> opdrachten) {
//        this.opdrachten = opdrachten;
//    }
//
    public List<Aanvraag> getAanvragen() {
        return aanvragen;
    }

//    public Set<Criteria> getCriteria() {
//        return criteria;
//    }

    public Date getLaatstIngelogd() {
        return laatstIngelogd;
    }

//    public List<Melding> getMeldingen() {
//        return meldingen;
//    }

//    public Set<Criteria> getStartpagina() {
//        return startpagina;
//    }

    public void setAanvragen(List<Aanvraag> aanvragen) {
        this.aanvragen = aanvragen;
    }
    
    public void addAanvraag(Aanvraag aanvraag) {
        aanvragen.add(aanvraag);
    }
    
//
//    public void setCriteria(Set<Criteria> criteria) {
//        this.criteria = criteria;
//    }

    public void setLaatstIngelogd(Date laatstIngelogd) {
        this.laatstIngelogd = laatstIngelogd;
    }
//
//    public void setMeldingen(List<Melding> meldingen) {
//        this.meldingen = meldingen;
//    }
//    
//    public void addMelding(Melding melding) {
//        meldingen.add(melding);
//    }

//    public void setStartpagina(Set<Criteria> startpagina) {
//        this.startpagina = startpagina;
//    }

    @Override
    public String toString() {
        return "GEBRUIKER [" + id + ", " + naam + ", " + creationDate + ", " + gebruikerType + ", " + toewijzingen.size() + "]";
    }


    public boolean isAllowed(Constrained constrained) {
        return getGebruikerType().isStandaard()
                ^ (getRechten().contains(constrained.getId())
                || getGebruikerType().getRechten().contains(constrained.getId()));
    }
    
}
