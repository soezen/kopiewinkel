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

    public List<E> list(Gebruiker gebruiker) {
        EntityManager manager = DatabaseManager.getEntityManager(type);
        List<E> result;

//        try {
            List<Long> keys = gebruiker.getRechten();
            keys.addAll(gebruiker.getGebruikerType().getRechten());

            if (keys.isEmpty()) {
                if (gebruiker.getGebruikerType().isStandaard()) {
                    return list();
                } else {
                    return new ArrayList<E>(0);
                }
            } 
            
            String stmt = "select e from " + clazz.getSimpleName() + " e "
                    + "where e.key member of :keys";
            Query query = manager.createQuery(stmt);
            query.setParameter("keys", keys);

            result = query.getResultList();

            if (gebruiker.getGebruikerType().isStandaard()) {
                List<E> all = list();
                
                for (E e : result) {
                    all.remove(e);
                }
                result = all;
            }
            
            System.out.println("QUERY: " + stmt);
            System.out.println("RESULTS: " + result.size());
//
//        } finally {
//            manager.close();
//        }

        return result;
    }
}
