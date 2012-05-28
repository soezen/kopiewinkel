/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.Gebruiker;
import domain.InputVeld;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author soft
 */
public class InputVeldDB extends PriviligedEntityDB<InputVeld> {

    public InputVeldDB() {
        clazz = InputVeld.class;
        type = DatabaseManager.EM_PRIJS_KLASSE;
    }
    
    public InputVeld getWithName(String naam) {
        EntityManager manager = DatabaseManager.getEntityManager(type);
//        try {
            String stmt = "select iv from " + clazz.getSimpleName() + " iv where iv.naam = :naam";
            Query query = manager.createQuery(stmt);
            query.setParameter("naam", naam);

            System.out.println("QUERY: " + stmt);
            
            return (InputVeld) query.getSingleResult();
//        } finally {
//            manager.close();
//        }
    }
    
}
