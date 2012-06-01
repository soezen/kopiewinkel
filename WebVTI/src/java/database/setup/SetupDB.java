/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database.setup;

import database.*;
import domain.*;
import domain.constraints.ConnectionConstraint;
import domain.constraints.FormuleConstraint;
import domain.constraints.OptiePrijsConstraint;
import domain.constraints.OptieTypePrijsConstraint;
import domain.enums.InputVeldType;
import utils.DateUtil;

/**
 *
 * @author soft
 */
public class SetupDB {
    
    public static void removeAll() {
        System.out.println("CLEANING DB...");
        EntityDB[] dbs = new EntityDB[] {
            new InputWaardeDB(), 
            new OpdrachtDB(),
            new OpdrachtTypeInputDB(),
            new OpdrachtTypeDB(),
            new PrijsKlasseDB(),
            new PrijsDB(),
            new PrijsFormuleDB(),
            new ConditieDB(),
            new LeerlingDB(),
            new SchooljaarGroepDB(),
            new DoelgroepDB(),
            new OptieTypeDB(),
            new OptieDB(),
            new MenuItemDB(),
            new AanvraagDB(),
          //  new MeldingDB(),
            new GebruikerTypeDB(),
            new GebruikerDB(),
            new InputVeldDB()
        };
        
        Class[] cts = new Class[] {
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

    // TODO read from file and update db if file has been changed
    private static void createDoelgroepen() {
        DoelgroepDB db = new DoelgroepDB();
        
        String[][] data = new String[][] {
            new String[] { "1ste jaar Wetenschappen", "1" },
            new String[] { "2de jaar Wetenschappen", "1" }, 
            new String[] { "3de jaar Industriele Wetenschappen", "2" },
            new String[] { "4de jaar Industriele Wetenschappen", "2" },
            new String[] { "3de jaar Techniek-Wetenschappen", "2" },
            new String[] { "4de jaar Techniek-Wetenschappen", "2" },
            new String[] { "3de jaar Electromechanica", "2" },
            new String[] { "4de jaar Electromechanica", "2" },
            new String[] { "5de jaar Industriele Wetenschappen", "3" },
            new String[] { "6de jaar Industriele Wetenschappen", "3" },
            new String[] { "5de jaar Techniek-Wetenschappen", "3" },
            new String[] { "6de jaar Techniek-Wetenschappen", "3" },
            new String[] { "5de jaar Electromechanica", "3" },
            new String[] { "6de jaar Electromechanica", "3" },
            new String[] { "5de jaar Industriele ICT", "3" },
            new String[] { "6de jaar Industriele ICT", "3" },
            new String[] { "1ste jaar Kunst", "1" },
            new String[] { "2de jaar Kunst", "1" },
            new String[] { "3de jaar Beeldende en Architecturale Kunsten", "2" },
            new String[] { "4de jaar Beeldende en Architecturale Kunsten", "2" },
            new String[] { "5de jaar Vrije Beeldende Kunst", "3" },
            new String[] { "6de jaar Vrije Beeldende Kunst", "3" },
            new String[] { "5de jaar Architecturale en Binnenhuiskunst", "3" },
            new String[] { "6de jaar Architecturale en Binnenhuiskunst", "3" },
            new String[] { "1ste jaar Technieken", "1" },
            new String[] { "2de jaar Mechanica-Elektriciteit", "1" },
            new String[] { "2de jaar Bouw- en Houttechnieken", "1" },
            new String[] { "3de jaar Elektrotechnieken", "2" },
            new String[] { "4de jaar Elektrotechnieken", "2" },
            new String[] { "3de jaar Mechanische Technieken", "2" },
            new String[] { "4de jaar Mechanische Technieken", "2" },
            new String[] { "3de jaar Houttechnieken", "2" },
            new String[] { "4de jaar Houttechnieken", "2" },
            new String[] { "3de jaar Bouwtechnieken", "2" },
            new String[] { "4de jaar Bouwtechnieken", "2" },
            new String[] { "5de jaar Elektrische Installatietechnieken", "3" },
            new String[] { "6de jaar Elektrische Installatietechnieken", "3" },
            new String[] { "5de jaar Mechanische Vormgevingstechnieken", "3" },
            new String[] { "6de jaar Mechanische Vormgevingstechnieken", "3" },
            new String[] { "5de jaar Houttechnieken", "3" },
            new String[] { "6de jaar Houttechnieken", "3" },
            new String[] { "5de jaar Bouwtechnieken", "3" },
            new String[] { "6de jaar Bouwtechnieken", "3" },
            new String[] { "5de jaar Autotechnieken", "3" },
            new String[] { "6de jaar Autotechnieken", "3" },
            new String[] { "7de jar Stuur- en Beveiligingstechnieken", "4" },
            new String[] { "1ste jaar Praktijk", "1" },
            new String[] { "2de jaar Elektriciteit-Metaal en Schilderen-Decoratie", "1" },
            new String[] { "2de jaar Bouw-Hout en Schilderen-Decoratie", "1" },
            new String[] { "3de jaar Elektrische Installaties", "2" },
            new String[] { "4de jaar Elektrische Installies", "2" },
            new String[] { "3de jaar Basismechanica", "2" },
            new String[] { "4de jaar Basismechanica", "2" },
            new String[] { "3de jaar Hout", "2" },
            new String[] { "4de jaar Hout", "2" },
            new String[] { "3de jaar Bouw", "2" },
            new String[] { "4de jaar Bouw", "2" },
            new String[] { "3de jaar Schilderwerk en Decoratie", "2" },
            new String[] { "4de jaar Schilderwerk en Decoratie", "2" },
            new String[] { "5de jaar Elektrische Installaties", "3" },
            new String[] { "6de jaar Elektrische Installaties", "3" },
            new String[] { "5de jaar Carrosserie", "3" },
            new String[] { "6de jaar Carrosserie", "3" },
            new String[] { "5de jaar Auto", "3" },
            new String[] { "6de jaar Auto", "3" },
            new String[] { "5de jaar Vrachtwagenchauffeur", "3" },
            new String[] { "6de jaar Vrachtwagenchauffeur", "3" },
            new String[] { "5de jaar Lassen-Constructie", "3" },
            new String[] { "6de jaar Lassen-Constructie", "3" },
            new String[] { "5de jaar Centrale Verwarming en Sanitaire Installatie", "3" },
            new String[] { "6de jaar Centrale Verwarming en Sanitaire Installatie", "3" },
            new String[] { "5de jaar Houtbewerking", "3" },
            new String[] { "6de jaar Houtbewerking", "3" },
            new String[] { "5de jaar Ruwbouw", "3" },
            new String[] { "6de jaar Ruwbouw", "3" },
            new String[] { "5de jaar Ruwbouwafwerking", "3" },
            new String[] { "6de jaar Ruwbouwafwerking", "3" },
            new String[] { "5de jaar Schilderwerk en Decoratie", "3" },
            new String[] { "6de jaar Schilderwerk en Decoratie", "3" },
            new String[] { "7de jaar Industriele Elektriciteit", "4" },
            new String[] { "7de jaar Fotolassen", "4" },
            new String[] { "7de jaar Auto-Elektriciteit", "4" },
            new String[] { "7de jaar Bijzonder Transport", "4" },
            new String[] { "7de jaar Interieurinrichting", "4" },
            new String[] { "7de jaar Renovatie Bouw", "4" },
            new String[] { "7de jaar Decoratie en Restauratie", "4" },
            new String[] { "7de jaar Schilderwerk", "4" },
            new String[] { "OKAN", "0" }
        };
        
        for (String[] entry : data) {
            Doelgroep dgr = new Doelgroep(entry[0], Integer.valueOf(entry[1]));
            db.persist(dgr);
        }
    }

    private static void createLeerlingen() {
        LeerlingDB db = new LeerlingDB();
    }
    
}
