/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.Aanvraag;

/**
 *
 * @author soft
 */
public class AanvraagDB extends IdEntityDB<Aanvraag> {

    public AanvraagDB() {
        clazz = Aanvraag.class;
        type = DatabaseManager.EM_AANVRAAG;
    }
    
    
}
