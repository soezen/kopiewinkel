/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.SchooljaarGroep;

/**
 *
 * @author Soezen
 */
public class SchooljaarGroepDB extends EntityDB<SchooljaarGroep> {

    public SchooljaarGroepDB() {
        clazz = SchooljaarGroep.class;
        type = DatabaseManager.EM_SCHOOLJAAR_GROEP;
    }
    
    
}
