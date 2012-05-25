/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author soft
 */
public class IdEntityDB<E> extends EntityDB<E> {

    public E getWithId(Long id) {
        EntityManager manager = DatabaseManager.getEntityManager(type);
//        try {
            String stmt = "select e from " + clazz.getSimpleName() + " e where e.id = :id";
            Query query = manager.createQuery(stmt);
            query.setParameter("id", id);

            System.out.println("QUERY: " + stmt);
            return (E) query.getSingleResult();
//        } finally {
//            manager.close();
//        }
    }
    
}
