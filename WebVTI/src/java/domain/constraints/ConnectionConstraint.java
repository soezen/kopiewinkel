/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.constraints;

import com.google.appengine.api.datastore.Key;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author Soezen
 */
@Entity
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public class ConnectionConstraint extends Constraint {
    protected boolean wederkerig;
    protected boolean verplicht;

    public ConnectionConstraint() {
    }

    public ConnectionConstraint(Key constrainer, Key constrained, boolean wederkerig, boolean verplicht) {
        super(constrainer, constrained);
        this.wederkerig = wederkerig;
        this.verplicht = verplicht;
    }

    public boolean isWederkerig() {
        return wederkerig;
    }

    public void setWederkerig(boolean wederkerig) {
        this.wederkerig = wederkerig;
    }

    public boolean isVerplicht() {
        return verplicht;
    }

    public void setVerplicht(boolean verplicht) {
        this.verplicht = verplicht;
    }

    @Override
    public String toString() {
        String type = "VERBIEDT";
        if (verplicht) {
            type = "VERPLICHT";
        }
        return "CONSTRAINT." + type + " [" + id + ", " + constrainer + ", " + constrained + ", " + wederkerig + "]";
    }
}
