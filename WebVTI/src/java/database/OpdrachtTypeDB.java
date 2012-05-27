/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.OpdrachtType;
import javax.persistence.EntityManager;
import javax.persistence.Query;

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
}
