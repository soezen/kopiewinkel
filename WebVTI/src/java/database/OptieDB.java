/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.Optie;
import domain.OptieType;
import domain.enums.OptieStatus;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author soft
 */
public class OptieDB extends PriviligedEntityDB<Optie> {

    public OptieDB() {
        clazz = Optie.class;
        type = DatabaseManager.EM_OPTIE;
    }

    public Optie getCurrentOfTypeWithName(OptieType optieType, String name) {
        EntityManager manager = DatabaseManager.getEntityManager(type);

        String stmt = "select o from " + clazz.getSimpleName() + " o "
                + "where o.naam = :naam "
                + "and o.status = :status "
                + "and o.optieType = :type";
        Query query = manager.createQuery(stmt);
        query.setParameter("naam", name);
        query.setParameter("status", OptieStatus.HUIDIG);
        query.setParameter("type", optieType);

        System.out.println("QUERY: " + stmt.replaceAll(":naam", name)
                .replaceAll(":status", OptieStatus.HUIDIG.name())
                .replaceAll(":type", optieType.getId().toString()));

        return (Optie) query.getSingleResult();

    }
}
