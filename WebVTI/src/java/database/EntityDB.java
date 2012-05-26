/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import com.google.appengine.api.datastore.Key;
import java.util.ArrayList;
import java.util.List;
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

    public List<E> list() {
        EntityManager manager = DatabaseManager.getEntityManager(type);
        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {
            Query query = manager.createQuery("select e from " + clazz.getSimpleName() + " e");
            List<E> result = query.getResultList();
            result.size();
            return query.getResultList();
        } finally {
            tx.commit();
        }

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
        manager.clear();
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
