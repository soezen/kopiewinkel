<%-- 
    Document   : index
    Created on : 1-mei-2012, 14:19:42
    Author     : soezen
--%>

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
                boolean fresh = false;

                // db managers
                GebruikerTypeDB gtdb = new GebruikerTypeDB();
                GebruikerDB gdb = new GebruikerDB();
                MenuItemDB midb = new MenuItemDB();
                OptieTypeDB otdb = new OptieTypeDB();
                OptieDB odb = new OptieDB();
                InputVeldDB ivdb = new InputVeldDB();
                AanvraagDB adb = new AanvraagDB();
                    
                GebruikerType gt = null;
                Gebruiker g = null;
                MenuItem mi1 = null;
                MenuItem mi2 = null;
                OptieType ot = null;
                Optie o = null;
                InputVeld iv = null;
                Aanvraag a = null;
                
                if (fresh) {
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
                } 
                
                gt = gtdb.getWithName("Leerkrachten");
                g = gdb.getWithName("Suzan");
                ot = otdb.getCurrentWithName("Kleur");
                o = odb.getCurrentOfTypeWithName(ot, "Rood");
                iv = ivdb.getWithName("Aantal");
                
                out.println("<br />" + gt);
                out.println("<br />" + g);
                out.println("<br />" + ot);           
                out.println("<br />" + o);
                out.println("<br />" + iv);
                
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
            %>
        </ul>
    </body>
</html>
