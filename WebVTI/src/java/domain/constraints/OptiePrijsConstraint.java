/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.constraints;

import com.google.appengine.api.datastore.Key;
import domain.Optie;
import domain.Prijs;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author Soezen
 */
@Entity
@DiscriminatorValue("OPTIEPRIJS")
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
        super(optie.getKey(), prijs, standaard);
    }
    
    public Key getOptieKey() {
        return getConstrainer();
    }
    
    public void setOptie(Optie optie) {
        setConstrainer(optie.getKey());
    }
}
