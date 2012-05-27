/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.constraints;

import com.google.appengine.api.datastore.Key;
import domain.Prijs;
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

    public PrijsConstraint(Key constrainer, Prijs constrained, boolean standaard) {
        super(constrainer, constrained.getKey());
        System.out.println("standaard: " + standaard);
        this.standaard = standaard;
    }
    
    public boolean isStandaard() {
        return standaard;
    }

    public void setStandaard(boolean standaard) {
        this.standaard = standaard;
    }
    
    public Key getPrijsKey() {
        return getConstrained();
    }
    
    public void setPrijsKey(Key prijs) {
        setConstrained(prijs);
    }

    @Override
    public String toString() {
        return "CONSTRAINT.PRIJS [" + getConstrainer() + ", " + getConstrained() + ", " + standaard + "]";
    }
    
    
}
