/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.OptieType;
import domain.enums.OptieStatus;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author soft
 */
public class OptieTypeDB extends PriviligedEntityDB<OptieType> {

    public OptieTypeDB() {
        clazz = OptieType.class;
        type = DatabaseManager.EM_OPTIE_TYPE;
    }
    
    public OptieType getCurrentWithName(String naam) {
        EntityManager manager = DatabaseManager.getEntityManager(type);
        
        String stmt = "select ot from " + clazz.getSimpleName() + " ot "
                + "where ot.naam = :naam "
                + "and ot.status = :status";
        Query query = manager.createQuery(stmt);
        query.setParameter("naam", naam);
        query.setParameter("status", OptieStatus.HUIDIG);
        
        System.out.println("QUERY: " + stmt.replaceAll(":naam", naam).replaceAll(":status", OptieStatus.HUIDIG.name()));
        
        return (OptieType) query.getSingleResult();
    }
    
}
