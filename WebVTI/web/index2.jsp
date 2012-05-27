<%-- 
    Document   : index
    Created on : 1-mei-2012, 14:19:42
    Author     : soezen
--%>

<%@page import="domain.constraints.OptiePrijsConstraint"%>
<%@page import="domain.constraints.PrijsConstraint"%>
<%@page import="domain.enums.Eenheid"%>
<%@page import="domain.enums.PrijsType"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.math.BigInteger"%>
<%@page import="domain.Prijs"%>
<%@page import="database.PrijsDB"%>
<%@page import="domain.enums.Operator"%>
<%@page import="database.LeerlingDB"%>
<%@page import="domain.Leerling"%>
<%@page import="database.SchooljaarGroepDB"%>
<%@page import="domain.SchooljaarGroep"%>
<%@page import="domain.Doelgroep"%>
<%@page import="database.DoelgroepDB"%>
<%@page import="domain.constraints.ConnectionConstraint"%>
<%@page import="domain.enums.ConstraintType"%>
<%@page import="domain.constraints.Constraint"%>
<%@page import="database.ConstraintDB"%>
<%@page import="database.ConditieDB"%>
<%@page import="domain.enums.AanvraagStatus"%>
<%@page import="domain.enums.AanvraagType"%>
<%@page import="database.AanvraagDB"%>
<%@page import="domain.enums.InputVeldType"%>
<%@page import="domain.InputVeld"%>
<%@page import="database.InputVeldDB"%>
<%@page import="utils.DateUtil"%>
<%@page import="java.util.TimeZone"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.Calendar"%>
<%@page import="domain.enums.OptieStatus"%>
<%@page import="domain.OptieType"%>
<%@page import="database.OptieTypeDB"%>
<%@page import="domain.Optie"%>
<%@page import="database.OptieDB"%>
<%@page import="database.MenuItemDB"%>
<%@page import="org.hibernate.dialect.DB2390Dialect"%>
<%@page import="domain.Melding"%>
<%@page import="database.MeldingDB"%>
<%@page import="javax.persistence.NoResultException"%>
<%@page import="test.TestDatabase"%>
<%@page import="database.GebruikerDB"%>
<%@page import="database.GebruikerTypeDB"%>
<%@page import="com.google.appengine.api.datastore.KeyFactory"%>
<%@page import="com.google.appengine.api.datastore.Key"%>
<%@page import="java.util.Date"%>
<%@page import="domain.GebruikerType"%>
<%@page import="domain.MenuItem"%>
<%@page import="java.util.List"%>
<%@page import="database.DatabaseManager"%>
<%@page import="domain.Conditie"%>
<%@page import="domain.Aanvraag"%>
<%@page import="domain.Gebruiker"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <ul>
            <%
                boolean rebuild = false;

                // db managers
                GebruikerTypeDB gtdb = new GebruikerTypeDB();
                GebruikerDB gdb = new GebruikerDB();
                MenuItemDB midb = new MenuItemDB();
                OptieTypeDB otdb = new OptieTypeDB();
                OptieDB odb = new OptieDB();
                InputVeldDB ivdb = new InputVeldDB();
                AanvraagDB adb = new AanvraagDB();
                ConditieDB cdb = new ConditieDB();
                ConstraintDB csdb = new ConstraintDB();
                DoelgroepDB ddb = new DoelgroepDB();
                SchooljaarGroepDB sgdb = new SchooljaarGroepDB();
                LeerlingDB ldb = new LeerlingDB();
                PrijsDB pdb = new PrijsDB();

                GebruikerType gt = null;
                Gebruiker g = null;
                MenuItem mi1 = null;
                MenuItem mi2 = null;
                OptieType ot = null;
                Optie o = null;
                InputVeld iv = null;
                Aanvraag a = null;
                Conditie c = null;
                ConnectionConstraint cc = null;
                Doelgroep d = null;
                Leerling l = null;
                Prijs p = null;
                
                if (rebuild) {
                    gt = new GebruikerType("Leerkrachten", false);
                    gtdb.persist(gt);

                    g = new Gebruiker(gt, "Suzan");
                    gdb.persist(g);
                    gt = gtdb.getWithName("Leerkrachten");

                    mi1 = new MenuItem("Startpagina", "http://www.google.com", true, 1);
                    mi2 = new MenuItem("Beheer", "http://www.gmail.com", true, 2);
                    midb.persist(mi1);
                    midb.persist(mi2);

                    gt.addRecht(mi1.getKey());
                    gtdb.update(gt);

                    Date startDate = DateUtil.date(2000, 1, 1);
                    ot = new OptieType("Kleur", "Kleur van het papier voor de gehele opdracht", 1, 1, startDate);
                    otdb.persist(ot);

                    o = new Optie(ot, "Rood", "Rood papier", startDate, OptieStatus.HUIDIG, 1);
                    odb.persist(o);
                    ot = otdb.getCurrentWithName("Kleur");

                    iv = new InputVeld("Aantal", InputVeldType.VAST, 1, 999);
                    ivdb.persist(iv);

                    Date aStartDate = DateUtil.date(2012, 1, 1);
                    a = new Aanvraag(gdb.getWithName("Suzan"), AanvraagType.INFO, AanvraagStatus.NIEUW, aStartDate, "Beheerder");
                    adb.persist(a);


                    OptieType otAndere = new OptieType("Andere", "Verscheidene opties", -1, 0, DateUtil.date(2012, 1, 1));
                    otdb.persist(otAndere);
                    Optie oTransparant = new Optie(otAndere, "Transparant", "Transparant papier", DateUtil.date(2012, 1, 1), OptieStatus.HUIDIG, 1);
                    odb.persist(oTransparant);

                    cc = new ConnectionConstraint(ot.getKey(), oTransparant.getKey(), true, false);
                    csdb.persist(cc);

                    d = new Doelgroep("Mechanica", 1);
                    ddb.persist(d);

                    SchooljaarGroep sg = new SchooljaarGroep(d, "A", 2012);
                    sgdb.persist(sg);
                    d = ddb.getWithNameInGrade("Mechanica", 1);

                    l = new Leerling("Niels Rogge", DateUtil.date(2012, 9, 1));
                    l.setCurrentGroep(d.getGroepen().get(0));
                    ldb.persist(l);

                    c = new Conditie("Combined", "Conditie consisting of two condities", "NOT (A3)");
                    cdb.persist(c);

                    p = new Prijs(null, DateUtil.date(2012, 1, 1), BigDecimal.valueOf(1.20), Eenheid.PAGINA, PrijsType.OPTIE);
                    pdb.persist(p);
                    OptiePrijsConstraint pc = new OptiePrijsConstraint(o, p, true);
                    csdb.persist(pc);
                    p = new Prijs(null, c, DateUtil.date(2012,1,1), null, BigDecimal.valueOf(2.0), Eenheid.OPDRACHT, PrijsType.OPDRACHT);
                    pdb.persist(p);    
                }

                gt = gtdb.getWithName("Leerkrachten");
                g = gdb.getWithName("Suzan");
                ot = otdb.getCurrentWithName("Kleur");
                o = odb.getCurrentOfTypeWithName(ot, "Rood");
                iv = ivdb.getWithName("Aantal");
                c = cdb.getWithName("Combined");
                d = ddb.getWithNameInGrade("Mechanica", 1);
           
                out.println("<br />" + gt);
                out.println("<br />" + g);
                out.println("<br />" + ot);
                out.println("<br />" + o);
                out.println("<br />" + iv);
                out.println("<br />" + c);
                out.println("<br />" + d);

                out.println("<br />Prijzen waaraan de conditie gelinkt is:<ul>");
                for (Prijs prijs : c.getPrijzen()) {
                    out.println("<li>" + prijs + "</li>");
                }
                out.println("</ul>");
                
                out.println("<br />Leerlingen van doelgroep per groep:<ul>");
                for (SchooljaarGroep sg : d.getGroepen()) {
                    out.println("<li>" + sg + "</li><ul>");
                    for (Leerling lln : sg.getLeerlingen()) {
                        out.println("<li>" + lln + "</li>");
                    }
                    out.println("</ul>");
                }
                out.println("</ul>");

                out.println("<br />All MenuItems:<ul>");
                List<MenuItem> all = midb.list();

                for (MenuItem item : all) {
                    out.println("<li>" + item + "</li>");
                }
                out.println("</ul>");

                out.println("<br />MenuItems visible to Gebruiker:<ul>");
                List<MenuItem> items = midb.list(g);

                for (MenuItem item : items) {
                    out.println("<li>" + item + "</li>");
                }
                out.println("</ul>");

                out.println("<br />Opties van OptieType:<ul>");

                for (Optie optie : ot.getOpties()) {
                    out.println("<li>" + optie + "</li>");
                }
                out.println("</ul>");

                out.println("<br />Aanvragen voor gebruiker:<ul>");
                for (Aanvraag aanvraag : g.getAanvragen()) {
                    out.println("<li>" + aanvraag + "</li>");
                }
                out.println("</ul>");

                out.println("<br />Alle constraints tussen opties en optietypes:<ul>");
                for (Constraint constraint : csdb.list(ConnectionConstraint.class)) {
                    out.println("<li>" + constraint + "</li>");
                }
                out.println("</ul>");
                
                out.println("<br />Alle prijs constraints op opties:<ul>");
                for (Constraint constraint : csdb.list(OptiePrijsConstraint.class)) {
                    out.println("<li>" + constraint + "</li>");
                }
                out.println("</ul>");
            %>
        </ul>
    </body>
</html>
