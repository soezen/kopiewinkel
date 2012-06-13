/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import com.google.appengine.api.datastore.Key;
import domain.Gebruiker;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author soft
 */
public class PriviligedEntityDB<E> extends IdEntityDB<E> {

    public List<E> list(Gebruiker gebruiker, String constraint, Object[][] params) {
        EntityManager manager = DatabaseManager.getEntityManager(type);
        List<E> result;
        List<Long> keys = gebruiker.getRechten();
        keys.addAll(gebruiker.getGebruikerType().getRechten());

        if (keys.isEmpty()) {
            if (gebruiker.getGebruikerType().isStandaard()) {
                return list(constraint, params);
            } else {
                return new ArrayList<E>();
            }
        }

        if (!gebruiker.getGebruikerType().isStandaard()) {
            String stmt = "select e from " + clazz.getSimpleName() + " e "
                    + "where e.id member of :keys and " + constraint;
            Query query = manager.createQuery(stmt);
            query.setParameter(":keys", keys);

            for (Object[] paramSet : params) {
                query.setParameter(paramSet[0].toString(), paramSet[1]);
            }

            result = query.getResultList();
        } else {
            result = new ArrayList<E>(list(gebruiker));
            List<E> all = list(constraint, params);

            for (E e : result) {
                all.remove(e);
            }
            result = all;
        }

        return result;

    }

    public List<E> list(Gebruiker gebruiker) {
        EntityManager manager = DatabaseManager.getEntityManager(type);
        List<E> result = new ArrayList<E>();

        List<Long> keys = gebruiker.getRechten();
        keys.addAll(gebruiker.getGebruikerType().getRechten());

        if (keys.isEmpty()) {
            if (gebruiker.getGebruikerType().isStandaard()) {
                return list();
            } else {
                return result;
            }
        }
        
        String stmt = "select e from " + clazz.getSimpleName() + " e "
                + "where e.id member of :keys";
        Query query = manager.createQuery(stmt);
        query.setParameter("keys", keys);

        result = query.getResultList();

        if (gebruiker.getGebruikerType().isStandaard()) {
            List<E> all = new ArrayList<E>(list());

            for (E e : result) {
                all.remove(e);
            }
            result = all;
        }

        System.out.println("QUERY: " + stmt.replaceAll(":keys", "" + keys));
        System.out.println("RESULTS: " + result.size());

        return result;
    }
}
