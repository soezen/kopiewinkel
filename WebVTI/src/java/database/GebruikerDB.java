/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.Gebruiker;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author soft
 */
public class GebruikerDB extends IdEntityDB<Gebruiker> {

    public GebruikerDB() {
        clazz = Gebruiker.class;
        type = DatabaseManager.EM_GEBRUIKER;
    }
    
    
    
    public Gebruiker getWithName(String naam) {
        EntityManager manager = DatabaseManager.getEntityManager(type);
//        try {
            String stmt = "select g from " + clazz.getSimpleName() + " g where g.naam = :naam";
            Query query = manager.createQuery(stmt);
            query.setParameter("naam", naam);

            System.out.println("QUERY: " + stmt);
            
            Gebruiker incompleteGebruiker = (Gebruiker) query.getSingleResult();
      //      System.out.println(incompleteGebruiker.getGebruikerType());
            
            return incompleteGebruiker;
//        } finally {
//            manager.close();
//        }
    }

}
