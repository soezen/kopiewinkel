/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.Leerling;

/**
 *
 * @author Soezen
 */
public class LeerlingDB extends IdEntityDB<Leerling> {

    public LeerlingDB() {
        clazz = Leerling.class;
        type = DatabaseManager.EM_DOELGROEP;
    }
    
}
