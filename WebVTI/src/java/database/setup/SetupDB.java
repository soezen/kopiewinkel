/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database.setup;

import au.com.bytecode.opencsv.CSVReader;
import database.*;
import domain.*;
import domain.constraints.ConnectionConstraint;
import domain.constraints.FormuleConstraint;
import domain.constraints.OptiePrijsConstraint;
import domain.constraints.OptieTypePrijsConstraint;
import domain.enums.InputVeldType;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author soft
 */
public class SetupDB {

    public static void removeAll() {
        System.out.println("CLEANING DB...");
        EntityDB[] dbs = new EntityDB[]{
            new InputWaardeDB(),
            new OpdrachtDB(),
            new OpdrachtTypeInputDB(),
            new OpdrachtTypeDB(),
            new PrijsKlasseDB(),
            new PrijsDB(),
            new PrijsFormuleDB(),
            new ConditieDB(),
            new DoelgroepDB(),
            new SchooljaarGroepDB(),
     //       new LeerlingDB(),
            new OptieTypeDB(),
            new OptieDB(),
            new MenuItemDB(),
            new AanvraagDB(),
            //  new MeldingDB(),
            new GebruikerTypeDB(),
            new GebruikerDB(),
            new InputVeldDB()
        };

        Class[] cts = new Class[]{
            ConnectionConstraint.class,
            FormuleConstraint.class,
            OptiePrijsConstraint.class,
            OptieTypePrijsConstraint.class
        };

        ConstraintDB cdb = new ConstraintDB();
        for (Class ct : cts) {
            cdb.deleteAll(ct);
        }


        for (EntityDB db : dbs) {
            db.deleteAll();
        }
    }

    public static void insertData() {
        createGebruikerTypes();
        createGebruikers();
        createInputVelden();
        createDoelgroepen();
        createLeerlingen();
    }

    private static void createGebruikerTypes() {
        GebruikerTypeDB db = new GebruikerTypeDB();

        GebruikerType gtLeerkrachten = new GebruikerType("Leerkrachten", false);
        db.persist(gtLeerkrachten);

        GebruikerType gtBeheerders = new GebruikerType("Beheerders", true);
        db.persist(gtBeheerders);

        GebruikerType gtAdministratie = new GebruikerType("Administratie", false);
        db.persist(gtAdministratie);
    }

    private static void createGebruikers() {
        GebruikerDB db = new GebruikerDB();
        GebruikerTypeDB gtdb = new GebruikerTypeDB();

        Gebruiker gAnne = new Gebruiker(gtdb.getWithName("Leerkrachten"), "Anne Saelens");
        db.persist(gAnne);

        Gebruiker gMonique = new Gebruiker(gtdb.getWithName("Beheerders"), "Monique Lefebvre");
        db.persist(gMonique);

        Gebruiker gGuillaume = new Gebruiker(gtdb.getWithName("Administratie"), "Guillaume Buysschaert");
        db.persist(gGuillaume);
    }

    private static void createInputVelden() {
        InputVeldDB db = new InputVeldDB();

        InputVeld ivOpdrachtgever = new InputVeld("Opdrachtgever", InputVeldType.VAST);
        db.persist(ivOpdrachtgever);

        InputVeld ivBestand = new InputVeld("Bestand", InputVeldType.VAST);
        db.persist(ivBestand);

        InputVeld ivAantal = new InputVeld("Aantal", InputVeldType.VAST);
        db.persist(ivAantal);

        InputVeld ivCommentaar = new InputVeld("Commentaar", InputVeldType.VAST);
        db.persist(ivCommentaar);

        InputVeld ivPrijs = new InputVeld("Prijs", InputVeldType.VAST);
        db.persist(ivPrijs);

        InputVeld ivKlassen = new InputVeld("Klassen", InputVeldType.VAST);
        db.persist(ivKlassen);
    }

    public static void createDoelgroepen() {
        DoelgroepDB db = new DoelgroepDB();
        
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader("data/db-data-Doelgroepen.csv"), ';');
            
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length == 2) {
                    Doelgroep dgr = new Doelgroep(line[1], Integer.valueOf(line[0]));
                    db.persist(dgr);
                } 
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SetupDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SetupDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void createLeerlingen() {
        try {
            LeerlingDB db = new LeerlingDB();
            DoelgroepDB dgrdb = new DoelgroepDB();
            SchooljaarGroepDB sdb = new SchooljaarGroepDB();
            
            CSVReader reader = new CSVReader(new FileReader("data/db-data-DoelgroepLeerlingen.csv"), ';');
            
            String[] line;
            while ((line = reader.readNext()) != null) {
                // Leerling
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Leerling lln = new Leerling(line[0], formatter.parse(line[1]));
                if (line[2] != null && !"".equals(line[2])) {
                    lln.setEindDatum(formatter.parse(line[2]));
                }
                
                // doelgroepen
                String[] groep = line[3].substring(1, line[3].length()-1).split(";");
                
                for (int i=0; i<groep.length-1; i+=2) {
                    Doelgroep dgr = dgrdb.getWithNameInGrade(groep[0], Integer.valueOf(groep[1]));
                    SchooljaarGroep sg = new SchooljaarGroep(dgr, groep[2], Integer.valueOf(groep[3]));
                    sg = sdb.persist(sg);
                    lln.addGroep(sg);
                }
                
                db.persist(lln);
            }
        } catch (ParseException ex) {
            Logger.getLogger(SetupDB.class.getName()).log(Level.SEVERE, "Error parsing start or end date of Leerling", ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SetupDB.class.getName()).log(Level.SEVERE, "Error locating file: data/db-data-DoelgroepLeerlingen.csv", ex);
        } catch (IOException ex) {
            Logger.getLogger(SetupDB.class.getName()).log(Level.SEVERE, "Error reading file data/db-data-DoelgroepLeerlingen.csv", ex);
        }
    }
}
