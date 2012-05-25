/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.Melding;

/**
 *
 * @author soft
 */
public class MeldingDB extends PriviligedEntityDB<Melding> {

    public MeldingDB() {
        clazz = Melding.class;
        type = DatabaseManager.EM_MELDING;
    }
    
    
}
