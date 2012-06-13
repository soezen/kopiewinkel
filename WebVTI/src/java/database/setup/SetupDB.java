/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database.setup;

//import au.com.bytecode.opencsv.CSVReader;
import com.google.appengine.api.datastore.Key;
import database.*;
import domain.*;
import domain.constraints.ConnectionConstraint;
import domain.constraints.FormuleConstraint;
import domain.constraints.OptiePrijsConstraint;
import domain.constraints.OptieTypePrijsConstraint;
import domain.enums.Eenheid;
import domain.enums.InputVeldType;
import domain.enums.OptieStatus;
import domain.enums.PrijsType;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.DateUtil;

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
            new ConditieDB(),
            new PrijsKlasseDB(),
            new PrijsFormuleDB(),
            new PrijsDB(),
            new OpdrachtTypeInputDB(),
            new OpdrachtTypeDB(),
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
        createPrijsTypes();
        createOpdrachtTypes();
        createOptieTypes();
        createOpdrachtTypeInput();
        createOpties();
        createCondities();
        createPrijzen();
        createConstraints();
        createMenuItems();
    }
    
    private static void createMenuItems() {
        MenuItemDB db = new MenuItemDB();
        
        db.persist(new MenuItem("Home", "home.jsp", false, 0));
        db.persist(new MenuItem("Nieuw", "newOpdracht.jsp", false, 1));
        db.persist(new MenuItem("Beheer", "mainBeheer.jsp", false, 2));
    }
    
    private static void createConstraints() {
        ConstraintDB db = new ConstraintDB();
        OptieDB odb = new OptieDB();
        OptieTypeDB otdb = new OptieTypeDB();
        
        // VERBIEDT is altijd wederkerig -> maak aparte constructor
        db.persist(new ConnectionConstraint(otdb.getCurrentWithName("Kleur Kaft"), odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Andere"), "Kaft"), false, true));
        db.persist(new ConnectionConstraint(otdb.getCurrentWithName("Gewicht Kaft"), odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Andere"), "Kaft"), false, true));
        db.persist(new ConnectionConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Druktype"), "Recto Verso"), odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Andere"), "Transparant"), true, false));
        db.persist(new ConnectionConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Gewicht"), "160gr"), otdb.getCurrentWithName("Kleur"), false, true));
        db.persist(new ConnectionConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Gewicht"), "170gr"), otdb.getCurrentWithName("Kleur"), true, false));
        db.persist(new ConnectionConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Gewicht"), "200gr"), otdb.getCurrentWithName("Kleur"), true, false));
        db.persist(new ConnectionConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Nieten"), "Plooi Boekje"), odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Andere"), "Boekje"), false, true));        
    }
    
    private static void createPrijzen() {
        PrijsDB db = new PrijsDB();
        ConditieDB cdb = new ConditieDB();
        ConstraintDB pcdb = new ConstraintDB();
        OptieDB odb = new OptieDB();
        OptieTypeDB otdb = new OptieTypeDB();
        
        Prijs p1 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), BigDecimal.valueOf(0.01), Eenheid.PAGINA, PrijsType.OPTIE));
        pcdb.persist(new OptiePrijsConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Druktype"), "Recto"), p1, true));
        
        Prijs p2 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), BigDecimal.valueOf(0.02), Eenheid.PAGINA, PrijsType.OPTIETYPE));
        pcdb.persist(new OptieTypePrijsConstraint(otdb.getCurrentWithName("Druktype"), p2, true));
        
        Prijs p3 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), BigDecimal.valueOf(0.013), Eenheid.PAGINA, PrijsType.OPTIETYPE, cdb.getWithName("Geen opties geselecteerd")));
        pcdb.persist(new OptieTypePrijsConstraint(otdb.getCurrentWithName("Druktype"), p3, false));
        
        Prijs p4 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), BigDecimal.valueOf(0.02), Eenheid.PAGINA, PrijsType.OPTIE));
        pcdb.persist(new OptiePrijsConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Druktype"), "Recto Verso"), p4, true));
        
        Prijs p5 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), BigDecimal.valueOf(0.005), Eenheid.PAGINA, PrijsType.OPTIETYPE));
        pcdb.persist(new OptieTypePrijsConstraint(otdb.getCurrentWithName("Kleur"), p5, true));
        
        Prijs p6 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), BigDecimal.valueOf(0.015), Eenheid.PAGINA, PrijsType.OPTIE));
        pcdb.persist(new OptiePrijsConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Gewicht"), "120gr"), p6, true));
        
        Prijs p7 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), Eenheid.PAGINA, cdb.getWithName("A3 geselecteerd")));
        pcdb.persist(new OptiePrijsConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Gewicht"), "120gr"), p7, false));
        pcdb.persist(new FormuleConstraint(db.get(p7.getKey()), new PrijsFormule("DEFAULT*(3+2/(DEFAULT*1000))")));
        
        Prijs p8 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), Eenheid.PAGINA, cdb.getWithName("A3 geselecteerd")));
        pcdb.persist(new OptiePrijsConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Gewicht"), "160gr"), p8, false));
        pcdb.persist(new FormuleConstraint(db.get(p8.getKey()), new PrijsFormule("DEFAULT*(3+8/(DEFAULT*1000))")));
        
        Prijs p9 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), Eenheid.PAGINA, cdb.getWithName("A3 geselecteerd")));
        pcdb.persist(new OptiePrijsConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Gewicht"), "170gr"), p9, false));
        pcdb.persist(new FormuleConstraint(db.get(p9.getKey()), new PrijsFormule("DEFAULT*(3+8/(DEFAULT*1000))")));
        
        Prijs p10 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), BigDecimal.valueOf(0.1), Eenheid.PAGINA, PrijsType.OPTIE, cdb.getWithName("A3 geselecteerd")));
        pcdb.persist(new OptiePrijsConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Gewicht"), "200gr"), p10, false));
        
        Prijs p11 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), BigDecimal.valueOf(0.04), Eenheid.KOPIE, PrijsType.OPTIE));
        pcdb.persist(new OptiePrijsConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Andere"), "Kaft"), p11, true));
        
        Prijs p12 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), BigDecimal.valueOf(0.01), Eenheid.PAGINA, PrijsType.OPTIE));
        pcdb.persist(new OptiePrijsConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Formaat"), "A3"), p12, true));
        
        Prijs p13 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), BigDecimal.valueOf(0.62), Eenheid.KOPIE, PrijsType.OPTIE));
        pcdb.persist(new OptiePrijsConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Andere"), "Inbinden"), p13, true));
        
        Prijs p14 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), BigDecimal.valueOf(0.477), Eenheid.PAGINA, PrijsType.OPTIE));
        pcdb.persist(new OptiePrijsConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Andere"), "Transparant"), p14, true));
        
        Prijs p15 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), BigDecimal.valueOf(0.022), Eenheid.PAGINA, PrijsType.OPTIE));
        pcdb.persist(new OptiePrijsConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Gewicht"), "170gr"), p15, true));
        
        Prijs p16 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), BigDecimal.valueOf(0.022), Eenheid.PAGINA, PrijsType.OPTIE));
        pcdb.persist(new OptiePrijsConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Gewicht"), "160gr"), p16, true));
        
        Prijs p17 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), BigDecimal.valueOf(0.04), Eenheid.KOPIE, PrijsType.OPTIE));
        pcdb.persist(new OptiePrijsConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Gewicht"), "200gr"), p17, true));
        
        Prijs p18 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), BigDecimal.valueOf(0.01), Eenheid.PAGINA, PrijsType.OPTIETYPE));
        pcdb.persist(new OptieTypePrijsConstraint(otdb.getCurrentWithName("Kleur Kaft"), p18, true));
        
        Prijs p19 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), BigDecimal.valueOf(0.03), Eenheid.KOPIE, PrijsType.OPTIE));
        pcdb.persist(new OptiePrijsConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Gewicht Kaft"), "120gr"), p19, true));
        
        Prijs p20 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), BigDecimal.valueOf(0.044), Eenheid.KOPIE, PrijsType.OPTIE));
        pcdb.persist(new OptiePrijsConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Gewicht Kaft"), "160gr"), p20, true));
        
        Prijs p21 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), BigDecimal.valueOf(0.044), Eenheid.KOPIE, PrijsType.OPTIE));
        pcdb.persist(new OptiePrijsConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Gewicht Kaft"), "170gr"), p21, true));
        
        Prijs p22 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), BigDecimal.valueOf(0.08), Eenheid.KOPIE, PrijsType.OPTIE));
        pcdb.persist(new OptiePrijsConstraint(odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Gewicht Kaft"), "200gr"), p22, true));
        
        // TODO get empty condition in service layer
        Prijs p23 = db.persist(new Prijs(DateUtil.date(2012, 1, 1), Eenheid.PAGINA));
        pcdb.persist(new OptieTypePrijsConstraint(otdb.getCurrentWithName("Kleur"), p23, false));
        pcdb.persist(new FormuleConstraint(db.get(p23.getKey()), new PrijsFormule("2*DEFAULT")));
    }
    
    private static void createCondities() {
        ConditieDB db = new ConditieDB();
        OptieDB odb = new OptieDB();
        OptieTypeDB otdb = new OptieTypeDB();
        
        db.persist(new Conditie("empty", "", ""));
        db.persist(new Conditie("A3 geselecteerd", "enkel van toepassing indien de optie 'A3' geselecteerd is", "O(" + odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Formaat"), "A3").getId() + ")"));
        db.persist(new Conditie("Geen opties geselecteerd", "enkel van toepassing indien geen enkele optie geselecteerd is", "O(LIST)=" + odb.getCurrentOfTypeWithName(otdb.getCurrentWithName("Druktype"), "Recto").getId()));
    }
    
    private static void createOpties() {
        OptieDB db = new OptieDB();
        OptieTypeDB otdb = new OptieTypeDB();
        
        db.persist(new Optie(otdb.getCurrentWithName("Druktype"), "Recto", "pagina's enkel aan de voorkant bedrukken", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 1));
        db.persist(new Optie(otdb.getCurrentWithName("Druktype"), "Recto Verso", "pagina's aan beidie kanten bedrukken", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 2));
        db.persist(new Optie(otdb.getCurrentWithName("Druktype"), "Blanco", "pagina's niet bedrukken", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 0));
        db.persist(new Optie(otdb.getCurrentWithName("Kleur"), "Blauw", "blauwe pagina's gebruiken", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 1));
        db.persist(new Optie(otdb.getCurrentWithName("Kleur"), "Rood", "rode pagina's gebruiken", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 2));
        db.persist(new Optie(otdb.getCurrentWithName("Kleur"), "Geel", "gele papier ", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 3));
        db.persist(new Optie(otdb.getCurrentWithName("Gewicht"), "120gr", "pagina's van 120 gram gebruiken", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 1));
        db.persist(new Optie(otdb.getCurrentWithName("Gewicht"), "160gr", "pagina's van 160 gram gebruiken", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 2));
        db.persist(new Optie(otdb.getCurrentWithName("Gewicht"), "170gr", "pagina's van 170 gram gebruiken", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 3));
        db.persist(new Optie(otdb.getCurrentWithName("Gewicht"), "200gr", "pagina's van 200 gram gebruiken", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 4));
        db.persist(new Optie(otdb.getCurrentWithName("Formaat"), "A3", "A3 pagina's gebruiken", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 1));
        db.persist(new Optie(otdb.getCurrentWithName("Nieten"), "Linkerboven", "pagina's in de linker bovenhoek bij elkaar nieten", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 1));
        db.persist(new Optie(otdb.getCurrentWithName("Nieten"), "Tweemaal Links", "pagina's aan de linkerkant bij elkaar nieten met 2 nietjes", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 2));
        db.persist(new Optie(otdb.getCurrentWithName("Nieten"), "Plooi Boekje", "boekje nieten in de plooi", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 3));
        db.persist(new Optie(otdb.getCurrentWithName("Andere"), "Boekje", "drukken als een boekje", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 1));
        db.persist(new Optie(otdb.getCurrentWithName("Andere"), "Perforeren", "pagina's perforeren aan de linkerkant", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 2));
        db.persist(new Optie(otdb.getCurrentWithName("Andere"), "Transparant", "drukken op transparanten (het is niet mogelijk om RV te drukken)", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 3));
        db.persist(new Optie(otdb.getCurrentWithName("Andere"), "Kaft", "kaft toevoegen aan de kopie (voor en achterkant): kaft kleur en gewicht moeten opgegeven worden", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 4));
        db.persist(new Optie(otdb.getCurrentWithName("Andere"), "Inbinden", "bundel inbinden", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 5));
        db.persist(new Optie(otdb.getCurrentWithName("Kleur Kaft"), "Blauw", "blauwe kaft gebruiken", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 1));
        db.persist(new Optie(otdb.getCurrentWithName("Kleur Kaft"), "Rood", "rode kaft gebruiken", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 2));
        db.persist(new Optie(otdb.getCurrentWithName("Kleur Kaft"), "Geel", "gele kaft gebruiken", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 3));
        db.persist(new Optie(otdb.getCurrentWithName("Gewicht Kaft"), "120gr", "kaft van 120 gram gebruiken", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 1));
        db.persist(new Optie(otdb.getCurrentWithName("Gewicht Kaft"), "160gr", "kaft van 160 gram gebruiken", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 2));
        db.persist(new Optie(otdb.getCurrentWithName("Gewicht Kaft"), "170gr", "kaft van 170 gram gebruiken", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 3));
        db.persist(new Optie(otdb.getCurrentWithName("Gewicht Kaft"), "200gr", "kaft van 200 gram gebruiken", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 4));
        
    }
    
    private static void createOpdrachtTypeInput() {
        OpdrachtTypeInputDB db = new OpdrachtTypeInputDB();
        OpdrachtTypeDB otdb = new OpdrachtTypeDB();
        InputVeldDB ivdb = new InputVeldDB();
        
        OpdrachtTypeInput oti1 = new OpdrachtTypeInput(ivdb.getWithName("Opdrachtgever"), otdb.getWithName("Standaard"), true, true, false, 0);
        db.persist(oti1);
        
        OpdrachtTypeInput oti2 = new OpdrachtTypeInput(ivdb.getWithName("Bestand"), otdb.getWithName("Standaard"), true, true, true, 1);
        db.persist(oti2);
        
        OpdrachtTypeInput oti3 = new OpdrachtTypeInput(ivdb.getWithName("Aantal"), otdb.getWithName("Standaard"), true, true, true, 2);
        db.persist(oti3);
        
        OpdrachtTypeInput oti4 = new OpdrachtTypeInput(ivdb.getWithName("Commentaar"), otdb.getWithName("Standaard"), false, true, true, 4);
        db.persist(oti4);
        
        OpdrachtTypeInput oti5 = new OpdrachtTypeInput(ivdb.getWithName("Prijs"), otdb.getWithName("Standaard"), true, false, false, 3);
        db.persist(oti5);
        
        OpdrachtTypeInput oti6 = new OpdrachtTypeInput(ivdb.getWithName("Klassen"), otdb.getWithName("Standaard"), true, true, true, 5);
        db.persist(oti6);
        
        OpdrachtTypeInput oti7 = new OpdrachtTypeInput(ivdb.getWithName("Opdrachtgever"), otdb.getWithName("Administratie"), true, true, false, 0);
        db.persist(oti7);
        
        OpdrachtTypeInput oti8 = new OpdrachtTypeInput(ivdb.getWithName("Bestand"), otdb.getWithName("Administratie"), true, true, true, 1);
        db.persist(oti8);
        
        OpdrachtTypeInput oti9 = new OpdrachtTypeInput(ivdb.getWithName("Aantal"), otdb.getWithName("Administratie"), true, true, true, 2);
        db.persist(oti9);
        
        OpdrachtTypeInput oti10 = new OpdrachtTypeInput(ivdb.getWithName("Commentaar"), otdb.getWithName("Administratie"), false, true, true, 3);
        db.persist(oti10);
        
        OpdrachtTypeInput oti11 = new OpdrachtTypeInput(ivdb.getWithName("Bestand"), otdb.getWithName("Prive"), true, true, true, 0);
        db.persist(oti11);
        
        OpdrachtTypeInput oti12 = new OpdrachtTypeInput(ivdb.getWithName("Aantal"), otdb.getWithName("Prive"), true, true, true, 1);
        db.persist(oti12);
        
        OpdrachtTypeInput oti13 = new OpdrachtTypeInput(ivdb.getWithName("Commentaar"), otdb.getWithName("Prive"), false, true, true, 3);
        db.persist(oti13);
        
        OpdrachtTypeInput oti14 = new OpdrachtTypeInput(ivdb.getWithName("Prijs"), otdb.getWithName("Prive"), true, false, true, 2);
        db.persist(oti14);
    }

    private static void createOptieTypes() {
        OptieTypeDB db = new OptieTypeDB();
        GebruikerTypeDB gtdb = new GebruikerTypeDB();

        OptieType otDruk = new OptieType("Druktype", "hoe een pagina gedrukt moet worden", 1, 1, DateUtil.date(2012, 1, 1));
        db.persist(otDruk);

        OptieType otKleur = new OptieType("Kleur", "welke kleur de pagina's moeten hebben", 1, 0, DateUtil.date(2012, 1, 1));
        db.persist(otKleur);

        OptieType otGewicht = new OptieType("Gewicht", "welk gewicht de pagina's hebben", 1, 0, DateUtil.date(2012, 1, 1));
        db.persist(otGewicht);

        OptieType otFormaat = new OptieType("Formaat", "de grootte van de pagina's", 1, 0, DateUtil.date(2012, 1, 1));
        db.persist(otFormaat);

        OptieType otNieten = new OptieType("Nieten", "hoe de pagina's aan elkaar geniet moeten worden", 1, 0, DateUtil.date(2012, 1, 1));
        db.persist(otNieten);

        OptieType otAndere = new OptieType("Andere", "andere mogelijke opties", -1, 0, DateUtil.date(2012, 1, 1));
        db.persist(otAndere);

        OptieType otKleurKaft = new OptieType("Kleur Kaft", "kleur van de kaft", 1, 0, DateUtil.date(2012, 1, 1));
        db.persist(otKleurKaft);

        OptieType otGewichtKaft = new OptieType("Gewicht Kaft", "gewicht van de kaft", 1, 0, DateUtil.date(2012, 1, 1));
        db.persist(otGewichtKaft);

        // geef gebruiker types rechten op optietypes indien nog niet zo
        // TODO testen wat er gebeurt in gui indien verplichte optie(type) niet aanwezig is
        for (GebruikerType gt : gtdb.list()) {
            if (!gt.isStandaard()) {
                gt.addRecht(otDruk);
                gt.addRecht(otKleurKaft);
                gt.addRecht(otGewichtKaft);
                gt.addRecht(otGewicht);
                gt.addRecht(otFormaat);
                gt.addRecht(otNieten);
                gt.addRecht(otAndere);
                if (!"Administratie".equals(gt.getNaam())) {
                    gt.addRecht(otKleur);
                }
                gtdb.update(gt);
            }
        }
    }

    private static void createOpdrachtTypes() {
        OpdrachtTypeDB db = new OpdrachtTypeDB();
        PrijsKlasseDB pkdb = new PrijsKlasseDB();
        GebruikerTypeDB gtdb = new GebruikerTypeDB();

        OpdrachtType otStandaard = new OpdrachtType(pkdb.getWithName("Standaard"), "Standaard", "Standaard invulbon voor leerkrachten", DateUtil.date(2012, 1, 1));
        db.persist(otStandaard);
        
        OpdrachtType otAdministratie = new OpdrachtType(pkdb.getWithName("Gratis"), "Administratie", "Invulbon voor administratieve opdrachten, deze worden niet aangerekend aan de leerlingen", DateUtil.date(2012, 1, 1));
        db.persist(otAdministratie);
        
        OpdrachtType otPrive = new OpdrachtType(pkdb.getWithName("Standaard"), "Prive", "Opdrachten voor prive doeleinden, deze moeten betaald worden door de opdrachtgever", DateUtil.date(2012, 1, 1));
        db.persist(otPrive);
    
        GebruikerType gtLeerkracht = gtdb.getWithName("Leerkrachten");
        gtLeerkracht.addRecht(otStandaard);
        gtLeerkracht.addRecht(otPrive);
        gtdb.update(gtLeerkracht);
      
        GebruikerType gtBeheerder = gtdb.getWithName("Beheerders");
        gtBeheerder.addRecht(otAdministratie);
        gtdb.update(gtBeheerder);
    }

    private static void createPrijsTypes() {
        PrijsKlasseDB db = new PrijsKlasseDB();

        PrijsKlasse pkGratis = new PrijsKlasse("Gratis");
        db.persist(pkGratis);

        PrijsKlasse pkStandaard = new PrijsKlasse("Standaard");
        db.persist(pkStandaard);
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

//        CSVReader reader;
//        try {
//            reader = new CSVReader(new FileReader("data/db-data-Doelgroepen.csv"), ';');
//            
//            String[] line;
//            while ((line = reader.readNext()) != null) {
//                if (line.length == 2) {
//                    Doelgroep dgr = new Doelgroep(line[1], Integer.valueOf(line[0]));
//                    db.persist(dgr);
//                } 
//            }
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(SetupDB.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(SetupDB.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private static void createLeerlingen() {
//        try {
//            LeerlingDB db = new LeerlingDB();
//            DoelgroepDB dgrdb = new DoelgroepDB();
//            SchooljaarGroepDB sdb = new SchooljaarGroepDB();
//            
//            CSVReader reader = new CSVReader(new FileReader("data/db-data-DoelgroepLeerlingen.csv"), ';');
//            
//            String[] line;
//            while ((line = reader.readNext()) != null) {
//                // Leerling
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                Leerling lln = new Leerling(line[0], formatter.parse(line[1]));
//                if (line[2] != null && !"".equals(line[2])) {
//                    lln.setEindDatum(formatter.parse(line[2]));
//                }
//                
//                // doelgroepen
//                String[] groep = line[3].substring(1, line[3].length()-1).split(";");
//                
//                for (int i=0; i<groep.length-1; i+=2) {
//                    Doelgroep dgr = dgrdb.getWithNameInGrade(groep[0], Integer.valueOf(groep[1]));
//                    SchooljaarGroep sg = new SchooljaarGroep(dgr, groep[2], Integer.valueOf(groep[3]));
//                    sg = sdb.persist(sg);
//                    lln.addGroep(sg);
//                }
//                
//                db.persist(lln);
//            }
//        } catch (ParseException ex) {
//            Logger.getLogger(SetupDB.class.getName()).log(Level.SEVERE, "Error parsing start or end date of Leerling", ex);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(SetupDB.class.getName()).log(Level.SEVERE, "Error locating file: data/db-data-DoelgroepLeerlingen.csv", ex);
//        } catch (IOException ex) {
//            Logger.getLogger(SetupDB.class.getName()).log(Level.SEVERE, "Error reading file data/db-data-DoelgroepLeerlingen.csv", ex);
//        }
    }
}
