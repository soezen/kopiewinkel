/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.Doelgroep;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author Soezen
 */
public class DoelgroepDB extends IdEntityDB<Doelgroep> {

    public DoelgroepDB() {
        clazz = Doelgroep.class;
        type = DatabaseManager.EM_DOELGROEP;
    }
    
    public Doelgroep getWithNameInGrade(String name, int grade) {
        EntityManager manager = DatabaseManager.getEntityManager(type);
        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {
            String stmt = "select e from " + clazz.getSimpleName() + " e where e.naam = :naam and e.graad = :graad";
            Query query = manager.createQuery(stmt);
            query.setParameter("naam", name);
            query.setParameter("graad", grade);

            System.out.println("QUERY: " + stmt);
            return (Doelgroep) query.getSingleResult();
        } finally {
            tx.commit();
        }
    }
    
}
