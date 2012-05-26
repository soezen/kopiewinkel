/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.Conditie;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Soezen
 */
public class ConditieDB extends IdEntityDB<Conditie> {

    public ConditieDB() {
        clazz = Conditie.class;
        type = DatabaseManager.EM_CONDITIE;
    }

    public Conditie getWithName(String name) {
        EntityManager manager = DatabaseManager.getEntityManager(type);
        String stmt = "select c from " + clazz.getSimpleName() + " c where c.naam = :naam";
        Query query = manager.createQuery(stmt);
        query.setParameter("naam", name);

        System.out.println("QUERY: " + stmt);

        return (Conditie) query.getSingleResult();
    }
}
