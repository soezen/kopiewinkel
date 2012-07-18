/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.Doelgroep;
import domain.SchooljaarGroep;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import utils.GlobalValues;

/**
 *
 * @author Soezen
 */
public class DoelgroepDB extends IdEntityDB<Doelgroep> {

    public DoelgroepDB() {
        clazz = Doelgroep.class;
        type = DatabaseManager.EM_DOELGROEP;
    }
    
    public Doelgroep getWithNameInGrade(String name, int grade) {
        EntityManager manager = DatabaseManager.getEntityManager(type);
        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {
            String stmt = "select e from " + clazz.getSimpleName() + " e where e.naam = :naam and e.graad = :graad";
            Query query = manager.createQuery(stmt);
            query.setParameter("naam", name);
            query.setParameter("graad", grade);

            System.out.println("QUERY: " + stmt.replaceAll(":naam", name).replaceAll(":graad", "" + grade));
            Doelgroep d = (Doelgroep) query.getSingleResult();
            return d;
        } finally {
            tx.commit();
        }
    }
    
    public List<Doelgroep> getDoelgroepenHuidigSchooljaar() {
        SchooljaarGroepDB sgdb = new SchooljaarGroepDB();
        List<SchooljaarGroep> groepen = sgdb.list("e.schooljaar = :jaar", new Object[][] {
           new Object[] { "jaar", GlobalValues.HUIDIG_SCHOOLJAAR } 
        });
        
        List<Doelgroep> result = new ArrayList<Doelgroep>();
        
        for (SchooljaarGroep groep : groepen) {
            if (!result.contains(groep.getDoelgroep())) {
                result.add(groep.getDoelgroep());
            }
        }
        
        return result;
    }
    
}
