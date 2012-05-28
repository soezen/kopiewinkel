/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.GebruikerType;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author soezen
 */
// TODO find out how to use generated value with custom generator
public class GebruikerTypeDB extends IdEntityDB<GebruikerType> {

    public GebruikerTypeDB() {
        clazz = GebruikerType.class;
        type = DatabaseManager.EM_GEBRUIKER_TYPE;
    }
    
    

    public GebruikerType getWithName(String naam) {
        EntityManager manager = DatabaseManager.getEntityManager(type);
            String stmt = "select gt from " + clazz.getSimpleName() + " gt where gt.naam = :naam";
            Query query = manager.createQuery(stmt);
            query.setParameter("naam", naam);

            System.out.println("QUERY: " + stmt);
            
            return (GebruikerType) query.getSingleResult();
    }
}