/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.PrijsKlasse;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Soezen
 */
public class PrijsKlasseDB extends IdEntityDB<PrijsKlasse> {

    public PrijsKlasseDB() {
        clazz = PrijsKlasse.class;
        type = DatabaseManager.EM_PRIJS_KLASSE;
    }

    public PrijsKlasse getWithName(String naam) {
        EntityManager manager = DatabaseManager.getEntityManager(type);

        String stmt = "select g from " + clazz.getSimpleName() + " g where g.naam = :naam";
        Query query = manager.createQuery(stmt);
        query.setParameter("naam", naam);

        System.out.println("QUERY: " + stmt);

        return (PrijsKlasse) query.getSingleResult();
    }
}
