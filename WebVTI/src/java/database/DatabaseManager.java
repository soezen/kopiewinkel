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

    private static EntityManager[] managers = new EntityManager[20];
    
    public static final int EM_GEBRUIKER = 0;
    public static final int EM_GEBRUIKER_TYPE = 0;
    public static final int EM_MELDING = 2;
    public static final int EM_MENU_ITEM = 3;
    public static final int EM_OPDRACHT = 4;
    public static final int EM_OPDRACHT_TYPE = 5;
    public static final int EM_OPTIE = 6;
    public static final int EM_OPTIE_TYPE = 6;
    public static final int EM_INPUT_VELD = 8;
    public static final int EM_AANVRAAG = 0;
    public static final int EM_CONDITIE = 10;
    public static final int EM_CONSTRAINT = 6;
    public static final int EM_DOELGROEP = 12;
    public static final int EM_PRIJS = 10;
    public static final int EM_PRIJS_FORMULE = 14;
    public static final int EM_PRIJS_KLASSE = 15;
    public static final int EM_OPDRACHT_TYPE_INPUT = 16;
    public static final int EM_INPUT_WAARDE = 4;
    public static final int EM_LEERLING = 18;
    public static final int EM_SCHOOLJAAR_GROEP = 19;
            
    
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
