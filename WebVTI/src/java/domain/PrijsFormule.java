/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import com.google.appengine.api.datastore.Key;
import domain.interfaces.Constrainable;
import javax.persistence.*;

/**
 * implements Constrainable because it can be constrained by a Constrainer, so it can be passed to Constraint as an argument.
 * 
 * @author soezen
 */
@Entity
public class PrijsFormule implements Constrainable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_SEQUENCE")
    @SequenceGenerator(name = "PF_SEQUENCE")
    protected Long id;
    private String formule;

    public PrijsFormule() {
    }

    public PrijsFormule(String formule) {
        this.formule = formule;
    }

    public String getFormule() {
        return formule;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public void setFormule(String formule) {
        this.formule = formule;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }
}
