/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.OpdrachtType;

/**
 *
 * @author soezen
 */
public class OpdrachtTypeDB extends PriviligedEntityDB<OpdrachtType> {

    public OpdrachtTypeDB() {
        clazz = OpdrachtType.class;
        type = DatabaseManager.EM_OPDRACHT_TYPE;
    }

    
}
