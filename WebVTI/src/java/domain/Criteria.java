package domain;
// Generated 18-feb-2012 18:51:31 by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * Criteria generated by hbm2java
 */
public class Criteria  implements java.io.Serializable {


     private Integer id;
     private Gebruiker eigenaar;
     private String naam;
     private String tabel;
     private Set<Doelgroep> doelgroepen = new HashSet<Doelgroep>(0);
     private Set<Prijs> prijzen = new HashSet<Prijs>(0);
     private Set<Constraint> constraints = new HashSet<Constraint>(0);
     private Set<Aanvraag> aanvragen = new HashSet<Aanvraag>(0);
     private Set<Optie> opties = new HashSet<Optie>(0);
     private Set<Gebruiker> gebruikers = new HashSet<Gebruiker>(0);
     private Set<OptieType> optieTypes = new HashSet<OptieType>(0);
     private Set<OpdrachtType> opdrachtTypes = new HashSet<OpdrachtType>(0);
     private Set<Opdracht> opdrachten = new HashSet<Opdracht>(0);

    public Criteria() {
    }

	
    public Criteria(Gebruiker eigenaar, String naam, String tabel) {
        this.eigenaar = eigenaar;
        this.naam = naam;
        this.tabel = tabel;
    }
    public Criteria(Gebruiker eigenaar, String naam, String tabel, Set<Doelgroep> doelgroepen, Set<Gebruiker> gebruikers, Set<Prijs> prijzen, Set<Constraint> constraints, Set<Aanvraag> aanvragen, Set<Optie> opties, Set<OptieType> optieTypes, Set<OpdrachtType> opdrachtTypes, Set<Opdracht> opdrachten) {
       this.eigenaar = eigenaar;
       this.naam = naam;
       this.tabel = tabel;
       this.doelgroepen = doelgroepen;
       this.gebruikers = gebruikers;
       this.prijzen = prijzen;
       this.constraints = constraints;
       this.aanvragen = aanvragen;
       this.opties = opties;
       this.optieTypes = optieTypes;
       this.opdrachtTypes = opdrachtTypes;
       this.opdrachten = opdrachten;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Gebruiker getEigenaar() {
        return this.eigenaar;
    }
    
    public void setEigenaar(Gebruiker eigenaar) {
        this.eigenaar = eigenaar;
    }
    public String getNaam() {
        return this.naam;
    }
    
    public void setNaam(String naam) {
        this.naam = naam;
    }
    public String getTabel() {
        return this.tabel;
    }
    
    public void setTabel(String tabel) {
        this.tabel = tabel;
    }
    public Set<Doelgroep> getDoelgroepen() {
        return this.doelgroepen;
    }
    
    public void setDoelgroepen(Set<Doelgroep> doelgroepen) {
        this.doelgroepen = doelgroepen;
    }
    public Set<Gebruiker> getGebruikers() {
        return this.gebruikers;
    }
    
    public void setGebruikers(Set<Gebruiker> gebruikers) {
        this.gebruikers = gebruikers;
    }
    public Set<Prijs> getPrijzen() {
        return this.prijzen;
    }
    
    public void setPrijzen(Set<Prijs> prijzen) {
        this.prijzen = prijzen;
    }
    public Set<Constraint> getConstraints() {
        return this.constraints;
    }
    
    public void setConstraints(Set<Constraint> constraints) {
        this.constraints = constraints;
    }
    public Set<Aanvraag> getAanvragen() {
        return this.aanvragen;
    }
    
    public void setAanvragen(Set<Aanvraag> aanvragen) {
        this.aanvragen = aanvragen;
    }
    public Set<Optie> getOpties() {
        return this.opties;
    }
    
    public void setOpties(Set<Optie> opties) {
        this.opties = opties;
    }
    public Set<OptieType> getOptieTypes() {
        return this.optieTypes;
    }
    
    public void setOptieTypes(Set<OptieType> optieTypes) {
        this.optieTypes = optieTypes;
    }
    public Set<OpdrachtType> getOpdrachtTypes() {
        return this.opdrachtTypes;
    }
    
    public void setOpdrachtTypes(Set<OpdrachtType> opdrachtTypes) {
        this.opdrachtTypes = opdrachtTypes;
    }
    public Set<Opdracht> getOpdrachten() {
        return this.opdrachten;
    }
    
    public void setOpdrachten(Set<Opdracht> opdrachten) {
        this.opdrachten = opdrachten;
    }




}

