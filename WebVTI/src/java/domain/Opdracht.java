package domain;
// Generated 11-feb-2012 16:48:29 by Hibernate Tools 3.2.1.GA

import com.google.appengine.api.datastore.Key;
import domain.enums.OpdrachtStatus;
import domain.interfaces.Constrained;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 * Opdracht generated by hbm2java
 */
@Entity
public class Opdracht implements java.io.Serializable, Constrained {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Key key;
    @Basic(optional=false)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="OD_SEQUENCE")
    @SequenceGenerator(name="OD_SEQUENCE")
    private Long id;
    private Key opdrachtgever;
    private Key uitvoerder;
    private Key opdrachtType;
    private String bestand;
    private int aantal;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date aanmaakDatum;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(nullable=true)
    private Date printDatum;
    private OpdrachtStatus status;
    private String commentaar;
    @OneToMany(mappedBy="opdracht")
    @Basic(fetch= FetchType.EAGER)
    private List<InputWaarde> inputWaarden = new ArrayList<InputWaarde>(0);
    @Basic
    private List<Long> doelgroepen = new ArrayList<Long>(0);
    @Basic
    private List<Long> opties = new ArrayList<Long>(0);

    public Opdracht() {
    }

    public Opdracht(Gebruiker opdrachtgever, OpdrachtType opdrachtType, String bestand, int aantal, Date aanmaakDatum, OpdrachtStatus status) {
        this.opdrachtgever = opdrachtgever.getKey();
        this.opdrachtType = opdrachtType.getKey();
        this.bestand = bestand;
        this.aantal = aantal;
        this.aanmaakDatum = aanmaakDatum;
        this.status = status;
    }

    public Key getKey() {
        return key;
    }
    
    public InputWaarde addInputWaarde(InputVeld veld, String waarde) {
        InputWaarde present = getInputWaardeFor(veld);
        if (present == null) {
            InputWaarde input = new InputWaarde(veld, this, waarde);
            inputWaarden.add(input);
            present = input;
        } else {
            present.setWaarde(waarde);
        }
        return present;
    }
    
    public void addInputWaarde(InputWaarde inputWaarde) {
        inputWaarden.add(inputWaarde);
    }

    public void addDoelgroep(Doelgroep doelgroep) {
        doelgroepen.add(doelgroep.getId());
    }

    public void addOptie(Optie optie) {
        opties.add(optie.getId());
    }

    public InputWaarde getInputWaardeFor(InputVeld veld) {
        for (InputWaarde waarde : inputWaarden) {
            if (waarde.getInputVeld().equals(veld)) {
                return waarde;
            }
        }
        return null;
    }
    
    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Key getOpdrachtgever() {
        return this.opdrachtgever;
    }

    public void setOpdrachtgever(Gebruiker opdrachtgever) {
        this.opdrachtgever = opdrachtgever.getKey();
    }

    public Key getUitvoerder() {
        return uitvoerder;
    }

    public void setUitvoerder(Gebruiker uitvoerder) {
        this.uitvoerder = uitvoerder.getKey();
    }

    public Key getOpdrachtType() {
        return this.opdrachtType;
    }

    public void setOpdrachtType(OpdrachtType opdrachtType) {
        this.opdrachtType = opdrachtType.getKey();
    }

    public String getBestand() {
        return this.bestand;
    }

    public void setBestand(String bestand) {
        this.bestand = bestand;
    }

    public int getAantal() {
        return this.aantal;
    }

    public void setAantal(int aantal) {
        this.aantal = aantal;
    }

    public Date getAanmaakDatum() {
        return this.aanmaakDatum;
    }

    public void setAanmaakDatum(Date aanmaakDatum) {
        this.aanmaakDatum = aanmaakDatum;
    }

    public Date getPrintDatum() {
        return this.printDatum;
    }

    public void setPrintDatum(Date printDatum) {
        this.printDatum = printDatum;
    }

    public OpdrachtStatus getStatus() {
        return this.status;
    }

    public void setStatus(OpdrachtStatus status) {
        this.status = status;
    }

    public String getCommentaar() {
        return this.commentaar;
    }

    public void setCommentaar(String commentaar) {
        this.commentaar = commentaar;
    }
    
    public void appendCommentaar(String commentaar) {
        this.commentaar += "<br />" + commentaar;
    }

    public List<InputWaarde> getInputWaarden() {
        return this.inputWaarden;
    }

    public void setInputWaarden(List<InputWaarde> inputWaarden) {
        this.inputWaarden = inputWaarden;
    }
    
    public List<Long> getDoelgroepen() {
        return this.doelgroepen;
    }

    public void setDoelgroepen(List<Long> doelgroepen) {
        this.doelgroepen = doelgroepen;
    }
    
    public List<Long> getOpties() {
        return this.opties;
    }

    public void setOpties(List<Long> opties) {
        this.opties = opties;
    }
    
    @Override
    public String toString() {
        return "OPDRACHT [" + id + ", " + opdrachtgever + ", " + opdrachtType + ", " + bestand + ", " + aantal + ", " + status + ", " + opties.size() + ", " + doelgroepen.size() + ", " + inputWaarden.size() + "]";
    }

    
}
