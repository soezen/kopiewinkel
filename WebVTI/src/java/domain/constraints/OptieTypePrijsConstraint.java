/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.constraints;

import com.google.appengine.api.datastore.Key;
import domain.OptieType;
import domain.Prijs;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author Soezen
 */
@Entity
@DiscriminatorValue("OPTIETYPEPRIJS")
public class OptieTypePrijsConstraint extends PrijsConstraint {

    public OptieTypePrijsConstraint() {
    }

    public OptieTypePrijsConstraint(OptieType constrainer, Prijs constrained, boolean standaard) {
        super(constrainer, constrained, standaard);
    }
    
    public Long getOptieTypeId() {
        return getConstrainer();
    }
    
    public void setOptieType(OptieType optieType) {
        setConstrainer(optieType);
    }
}
