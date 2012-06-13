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
        String stmt = "select e from " + clazz.getSimpleName() + " e";
        Query query = manager.createQuery(stmt);
        System.out.println("QUERY: " + stmt);
        return query.getResultList();
    }

    public List<? extends Constraint> list(Class<? extends Constraint> clazz, String constraint, Object[][] params) {
        EntityManager manager = DatabaseManager.getEntityManager(type);
        String stmt = "select e from " + clazz.getSimpleName() + " e where " + constraint;
        Query query = manager.createQuery(stmt);
        
        for (Object[] param : params) {
            query.setParameter(param[0].toString(), param[1]);
            stmt = stmt.replaceAll(":" + param[0], "" + param[1]);
        }
        
        System.out.println("QUERY: " + stmt);
        return query.getResultList();
        
    }

    public void deleteAll(Class clazz) {
        Class oldClazz = clazz;
        this.clazz = clazz;
        super.deleteAll();
        this.clazz = oldClazz;
    }
}
