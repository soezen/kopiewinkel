/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.constraints.Constraint;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;


/**
 *
 * @author Soezen
 */
public class ConstraintDB extends IdEntityDB<Constraint> {

    public ConstraintDB() {
        clazz = Constraint.class;
        type = DatabaseManager.EM_CONSTRAINT;
    }

    public List<? extends Constraint> list(Class<? extends Constraint> clazz) {
        EntityManager manager = DatabaseManager.getEntityManager(type);
        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {
            Query query = manager.createQuery("select e from " + clazz.getSimpleName() + " e");
            List<? extends Constraint> result = query.getResultList();
            result.size();
            return query.getResultList();
        } finally {
            tx.commit();
        }
    }   
    
}
