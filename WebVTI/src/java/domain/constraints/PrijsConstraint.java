/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.constraints;

import com.google.appengine.api.datastore.Key;
import domain.Prijs;
import domain.interfaces.Constrainer;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author Soezen
 */
@Entity
@MappedSuperclass
public abstract class PrijsConstraint extends Constraint {
    protected boolean standaard;

    public PrijsConstraint() {
    }

    public PrijsConstraint(Constrainer constrainer, Prijs constrained, boolean standaard) {
        super(constrainer, constrained);
        this.standaard = standaard;
    }
    
    public boolean isStandaard() {
        return standaard;
    }

    public void setStandaard(boolean standaard) {
        this.standaard = standaard;
    }
    
    public Long getPrijsId() {
        return getConstrained();
    }
    
    public void setPrijsKey(Prijs prijs) {
        setConstrained(prijs);
    }

    @Override
    public String toString() {
        return "CONSTRAINT.PRIJS [" + getConstrainer() + ", " + getConstrained() + ", " + standaard + "]";
    }
    
    
}
