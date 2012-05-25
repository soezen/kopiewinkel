/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package domain;

import domain.interfaces.Constrainable;

/**
 *
 * @author soezen
 */
public class PrijsFormule implements Constrainable {
    private Long id;
    private String formule;

    public PrijsFormule(String formule) {
        this.formule = formule;
    }

    public String getFormule() {
        return formule;
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
