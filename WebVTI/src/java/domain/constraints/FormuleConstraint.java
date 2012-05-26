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
        super(prijs.getKey(), formule.getKey());
    }
    
    public Key getPrijsKey() {
        return constrainer;
    }
    
    public Key getFormuleKey() {
        return constrained;
    }
    
    public void setPrijs(Key prijs) {
        setConstrainer(prijs);
    }
    
    public void setFormule(Key formule) {
        setConstrained(formule);
    }

    @Override
    public String toString() {
        return "CONSTRAINT.FORMULE [" + constrainer + ", " + constrained + "]";
    }
}
