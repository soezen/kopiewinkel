/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.PrijsFormule;

/**
 *
 * @author Soezen
 */
public class PrijsFormuleDB extends IdEntityDB<PrijsFormule> {

    public PrijsFormuleDB() {
        clazz = PrijsFormule.class;
        type = DatabaseManager.EM_PRIJS_FORMULE;
    }
    
}
