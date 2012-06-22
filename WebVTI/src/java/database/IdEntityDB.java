/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author soft
 */
public class IdEntityDB<E> extends EntityDB<E> {

    public E getWithId(Long id) {
        EntityManager manager = DatabaseManager.getEntityManager(type);
        String stmt = "select e from " + clazz.getSimpleName() + " e where e.id = :id";
        Query query = manager.createQuery(stmt);
        query.setParameter("id", id);

        System.out.println("QUERY: " + stmt.replaceAll(":id", "" + id));
        return (E) query.getSingleResult();
    }
    
    public List<E> list(List<Long> ids) {
        return list("e.id member of :ids", new Object[][] {
           new Object[] { "ids", ids } 
        });
    }
}
