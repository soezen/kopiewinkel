/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.constraints;

import domain.Optie;
import domain.interfaces.Constrainable;
import domain.interfaces.Constrainer;
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
    protected boolean constrainedOptie;
    protected boolean constrainerOptie;

    public ConnectionConstraint() {
    }

    public ConnectionConstraint(Constrainer constrainer, Constrainable constrained, boolean wederkerig, boolean verplicht) {
        super(constrainer, constrained);
        this.constrainerOptie = (constrainer instanceof Optie) ? true : false;
        this.constrainedOptie = (constrained instanceof Optie) ? true : false;
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

    public boolean isConstrainedOptie() {
        return constrainedOptie;
    }

    public boolean isConstrainerOptie() {
        return constrainerOptie;
    }

    @Override
    public void setConstrained(Constrainable constrained) {
        super.setConstrained(constrained);
        constrainedOptie = (constrained instanceof Optie) ? true : false;
    }

    @Override
    public void setConstrainer(Constrainer constrainer) {
        super.setConstrainer(constrainer);
        constrainerOptie = (constrainer instanceof Optie) ? true : false;
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
