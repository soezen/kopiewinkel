/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.google.appengine.api.datastore.KeyFactory;
import database.GebruikerDB;
import database.GebruikerTypeDB;
import database.MenuItemDB;
import domain.Gebruiker;
import domain.GebruikerType;
import domain.MenuItem;
import java.util.List;

/**
 *
 * @author soft
 */
public class TestDatabase {

    private GebruikerTypeDB gtdb = new GebruikerTypeDB();
    private GebruikerDB gdb = new GebruikerDB();
    private MenuItemDB midb = new MenuItemDB();

    /**
     * store a new gebruikerType in db.
     *
     * @return newly stored gebruikerType
     */
    public GebruikerType createGebruikerType(String naam) {
        GebruikerType expected = new GebruikerType(naam, true);
        expected.addRecht(KeyFactory.createKey("MenuItem", 1));
        return gtdb.persist(expected);
    }

    /**
     * create a new gebruiker with a new gebruikerType.
     *
     * @return
     */
    public Gebruiker createGebruiker() {
        GebruikerType type = createGebruikerType("TYPE");
        return createGebruiker(type);
    }

    /**
     * create a new gebruiker with an existing gebruikerType.
     */
    public Gebruiker createGebruiker(GebruikerType type) {
        Gebruiker expectedGebruiker = new Gebruiker(type, "Eline");
        return gdb.persist(expectedGebruiker);
    }
    
    public Gebruiker getGebruiker(Long id) {
        return gdb.getWithId(id);
    }
    
    public Gebruiker getGebruiker(String naam) {
        return gdb.getWithName(naam);
    }
    
    public GebruikerType getGebruikerType(Long id) {
        return gtdb.getWithId(id);
    }
    
    public GebruikerType getGebruikerType(String naam) {
        return gtdb.getWithName(naam);
    }

    public GebruikerType updateNaam(GebruikerType gt) {
        gt.setNaam(gt.getNaam().replace(gt.getNaam().charAt(0), 'X'));
        return gtdb.update(gt);
    }
    
    public GebruikerType updateStandaard(GebruikerType gt) {
        gt.setStandaard(!gt.isStandaard());
        return gtdb.update(gt);
    }
    
    public boolean deleteGebruikerType(GebruikerType gt) {
        return gtdb.delete(gt.getKey());
    }
    
    public boolean deleteGebruiker(Gebruiker g) {
        return gdb.delete(g.getKey());
    }
    
    public List<Gebruiker> getGebruikers() {
        return gdb.list();
    }
    
    public List<GebruikerType> getGebruikerTypes() {
        return gtdb.list();
    }
    
    public List<MenuItem> getMenuItems() {
        return midb.list();
    }
    
    public List<MenuItem> getMenuItems(Gebruiker gebruiker) {
        return midb.list(gebruiker);
    }
    
    public MenuItem createMenuItem() {
        MenuItem item = new MenuItem("Beheer", "http://www.google.be", true, 1);
        return midb.persist(item);
    }
    
    public MenuItem getMenuItem() {
        return midb.getWithId(1L);
    }
    
    // test unique constraint on name
    // has to be implemented in the service
    
}
