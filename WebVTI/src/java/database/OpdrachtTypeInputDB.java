/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.OpdrachtTypeInput;

/**
 *
 * @author Soezen
 */
public class OpdrachtTypeInputDB extends EntityDB<OpdrachtTypeInput> {

    public OpdrachtTypeInputDB() {
        clazz = OpdrachtTypeInput.class;
        type = DatabaseManager.EM_PRIJS_KLASSE;
    }
    
    
}
