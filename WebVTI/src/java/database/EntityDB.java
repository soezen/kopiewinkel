/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import com.google.appengine.api.datastore.Key;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author soft
 */
public abstract class EntityDB<E> {

    protected Class<E> clazz;
    protected int type;

    public void deleteAll() {

        // TODO change this to method in super type and E extends from it
        System.out.println("deleting all entities of " + clazz.getSimpleName());

        for (E e : list()) {
            try {
                delete((Key) clazz.getMethod("getKey", null).invoke(e, null));
            } catch (IllegalAccessException ex) {
                Logger.getLogger(EntityDB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(EntityDB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(EntityDB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(EntityDB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(EntityDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * create list of entities that match the given criteria
     *
     * @param constraint criteria an entity in the list should match with
     * @param params values of parameters used in the constriant
     * @return
     */
    public List<E> list(String constraint, Object[][] params) {
        EntityManager manager = DatabaseManager.getEntityManager(type);

        String stmt = "select e from " + clazz.getSimpleName() + " e and " + constraint;
        Query query = manager.createQuery(stmt);

        for (Object[] param : params) {
            query.setParameter(param[0].toString(), param[1]);
        }

        List<E> result = query.getResultList();
        result.size();
        System.out.println("QUERY: " + stmt);
        return query.getResultList();

    }

    public List<E> list() {
        EntityManager manager = DatabaseManager.getEntityManager(type);

        String stmt = "select e from " + clazz.getSimpleName() + " e";
        Query query = manager.createQuery(stmt);
        List<E> result = query.getResultList();
        result.size();
        System.out.println("QUERY: " + stmt);
        return query.getResultList();

    }

    public E get(Key key) {
        EntityManager manager = DatabaseManager.getEntityManager(type);
        return manager.find(clazz, key);
    }

    public E persist(E entity) {
        System.out.println("... persisting " + entity);
        EntityManager manager = DatabaseManager.getEntityManager(type);
        manager.clear();
        EntityTransaction tx = manager.getTransaction();

        try {
            tx.begin();
            manager.persist(entity);
            manager.flush();
            tx.commit();
            System.out.println("DONE");
        } catch (Exception e) {
            System.err.println("FAIL");
            e.printStackTrace();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        return entity;
    }

    public E update(E entity) {
        System.out.println("... updating " + entity);
        EntityManager manager = DatabaseManager.getEntityManager(type);
        EntityTransaction tx = manager.getTransaction();

        try {
            tx.begin();
            manager.merge(entity);
            manager.flush();
            tx.commit();
            System.out.println("DONE");
        } catch (Exception e) {
            System.err.println("FAIL");
            e.printStackTrace();
        } finally {
            if (tx.isActive()) {
                System.out.println("ROLLBACK");
                tx.rollback();
            }
        }
        return entity;
    }

    public boolean delete(Key key) {
        boolean done = false;
        EntityManager manager = DatabaseManager.getEntityManager(type);
        EntityTransaction tx = manager.getTransaction();

        try {
            tx.begin();
            E dbEntity = (E) manager.find(clazz, key);
            System.out.println("... deleting " + dbEntity);
            manager.remove(dbEntity);
            manager.flush();
            tx.commit();
            done = true;
            System.out.println("DONE");
        } catch (Exception e) {
            System.err.println("FAIL");
            e.printStackTrace();
            tx.rollback();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }

        return done;
    }
}
