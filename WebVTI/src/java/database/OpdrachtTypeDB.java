/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.Gebruiker;
import domain.OpdrachtType;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import utils.DateUtil;

/**
 *
 * @author soezen
 */
public class OpdrachtTypeDB extends PriviligedEntityDB<OpdrachtType> {

    public OpdrachtTypeDB() {
        clazz = OpdrachtType.class;
        type = DatabaseManager.EM_PRIJS_KLASSE;
    }

    public OpdrachtType getWithName(String naam) {
        EntityManager manager = DatabaseManager.getEntityManager(type);
        String stmt = "select g from " + clazz.getSimpleName() + " g where g.naam = :naam";
        Query query = manager.createQuery(stmt);
        query.setParameter("naam", naam);

        System.out.println("QUERY: " + stmt);

        return (OpdrachtType) query.getSingleResult();
    }

    public List<OpdrachtType> getActieveOpdrachtTypes(Gebruiker gebruiker) {
        List<OpdrachtType> result = new ArrayList<OpdrachtType>(list(gebruiker));

        Iterator<OpdrachtType> it = result.iterator();
        Date validOn = DateUtil.today();
        
        // TODO move to method is in between
        while (it.hasNext()) {
            OpdrachtType ot = it.next();
            
            if (ot.getVan().after(validOn)
               || (ot.getTot() != null && ot.getTot().before(validOn))) {
                it.remove();
            }
        }

        return result;
    }
    
}
