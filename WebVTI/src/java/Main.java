
import database.GebruikerTypeDB;
import domain.GebruikerType;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author soezen
 */
public class Main {

    public static void main(String args[]) {

       persistSimpleGebruikerType();

    }
    
    /**
     * persisting a Gebruiker type without gebruikers.
     */
    private static void persistSimpleGebruikerType() {
        GebruikerTypeDB db = new GebruikerTypeDB();
        
        GebruikerType expected = new GebruikerType("TEST", true);
        GebruikerType actual = db.persist(expected);
        
        if (actual.getId() == null) {
            throw new IllegalStateException("GebruikerType " + expected + " NOT persisted.");
        }
        assertSameCoreValues(expected, actual);
    }
    
    private static void assertSameCoreValues(GebruikerType expected, GebruikerType actual) {
        if (expected.getNaam().equals(actual.getNaam())) {
            throw new IllegalStateException("GebruikerType.naam not as expected.");
        }
       
        if (expected.isStandaard() != actual.isStandaard()) {
            throw new IllegalStateException("GebruikerType.standaard not as expected.");
        }
    }
}
