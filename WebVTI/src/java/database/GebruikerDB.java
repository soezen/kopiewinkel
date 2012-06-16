/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.Gebruiker;
import domain.OpdrachtType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author soft
 */
public class GebruikerDB extends IdEntityDB<Gebruiker> {

    public GebruikerDB() {
        clazz = Gebruiker.class;
        type = DatabaseManager.EM_GEBRUIKER;
    }

    public Gebruiker getGastGebruiker() {
        return getWithName("Monique Lefebvre");
    }

    public Gebruiker getWithName(String naam) {
        EntityManager manager = DatabaseManager.getEntityManager(type);
        String stmt = "select g from " + clazz.getSimpleName() + " g where g.naam = :naam";
        Query query = manager.createQuery(stmt);
        query.setParameter("naam", naam);

        System.out.println("QUERY: " + stmt.replaceAll(":naam", naam));

        Gebruiker incompleteGebruiker = (Gebruiker) query.getSingleResult();

        return incompleteGebruiker;
    }

    public List<Gebruiker> getOpdrachtGevers(OpdrachtType ot) {
        List<Gebruiker> result = new ArrayList<Gebruiker>(list());

        Iterator<Gebruiker> it = result.iterator();

        while (it.hasNext()) {
            Gebruiker g = it.next();

            List<Long> ids = new ArrayList<Long>(g.getRechten());
            ids.addAll(g.getGebruikerType().getRechten());

            if (g.getGebruikerType().isStandaard() && ids.contains(ot.getId())) {
                it.remove();
            } else if (!g.getGebruikerType().isStandaard() && !ids.contains(ot.getId())) {
                it.remove();
            }
        }

        return result;
    }
}
