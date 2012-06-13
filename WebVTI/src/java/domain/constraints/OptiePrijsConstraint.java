/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.constraints;

import com.google.appengine.api.datastore.Key;
import domain.Optie;
import domain.Prijs;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author Soezen
 */
@Entity
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public class OptiePrijsConstraint extends PrijsConstraint {

    public OptiePrijsConstraint() {
    }

    /**
     * Create a constraint which specifies the price for a selected option.
     * <code>standaard</code> specifies whether the conditions of the price have to be met or not.
     * 
     * @param optie
     * @param prijs
     * @param standaard if <code>true</code> the conditions of the price are ignored.
     */
    public OptiePrijsConstraint(Optie optie, Prijs prijs, boolean standaard) {
        super(optie, prijs, standaard);
        this.standaard = standaard;
    }
    
    public Long getOptieId() {
        return getConstrainer();
    }
    
    public void setOptie(Optie optie) {
        setConstrainer(optie);
    }
}
