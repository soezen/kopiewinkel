/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author soezen
 */
public class DatabaseManager {
    
    private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("transactions-optional");

    private static EntityManager[] managers = new EntityManager[10];
    
    public static final int EM_GEBRUIKER = 0;
    public static final int EM_GEBRUIKER_TYPE = 1;
    public static final int EM_MELDING = 2;
    public static final int EM_MENU_ITEM = 3;
    public static final int EM_OPDRACHT = 4;
    public static final int EM_OPDRACHT_TYPE = 5;
    public static final int EM_OPTIE = 6;
    public static final int EM_OPTIE_TYPE = 6;
    public static final int EM_INPUT_VELD = 8;
    public static final int EM_AANVRAAG = 0;
    
    private DatabaseManager() {
    }

    public static EntityManager getEntityManager(int type) {
        if (managers[type] == null) {
            managers[type] = factory.createEntityManager();
        }
        
        if (!managers[type].isOpen()) {
            managers[type] = factory.createEntityManager();
        }
        
        return managers[type];
    }
}
