/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.constraints;

import com.google.appengine.api.datastore.Key;
import domain.Prijs;
import domain.PrijsFormule;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author Soezen
 */
@Entity
@DiscriminatorValue(value="FORMULE")
public class FormuleConstraint extends Constraint {

    public FormuleConstraint() {
    }

    public FormuleConstraint(Prijs prijs, PrijsFormule formule) {
        super(prijs, formule);
    }
    
    public Long getPrijsId() {
        return constrainer;
    }
    
    public Long getFormuleId() {
        return constrained;
    }
    
    public void setPrijs(Prijs prijs) {
        setConstrainer(prijs);
    }
    
    public void setFormule(PrijsFormule formule) {
        setConstrained(formule);
    }

    @Override
    public String toString() {
        return "CONSTRAINT.FORMULE [" + constrainer + ", " + constrained + "]";
    }
}
