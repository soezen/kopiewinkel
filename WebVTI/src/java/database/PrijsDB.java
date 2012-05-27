/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.Prijs;

/**
 *
 * @author Soezen
 */
public class PrijsDB extends IdEntityDB<Prijs> {

    public PrijsDB() {
        clazz = Prijs.class;
        type = DatabaseManager.EM_PRIJS;
    }
    
}
