<%-- 
    Document   : index
    Created on : 1-mei-2012, 14:19:42
    Author     : soezen
--%>

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
                GebruikerTypeDB gtdb = new GebruikerTypeDB();
             //   GebruikerType gt = new GebruikerType("Leerkrachten", false);
             //   gtdb.persist(gt);
                GebruikerType gt = gtdb.getWithName("Leerkrachten");
                out.println(gt);
                
                GebruikerDB gdb = new GebruikerDB();
          //      Gebruiker g = new Gebruiker(gt, "Suzan");
          //      gdb.persist(g);
                
                Gebruiker gebruiker = gdb.getWithName("Suzan");
                out.println("<br />" + gebruiker);
                
                MenuItemDB midb = new MenuItemDB();
                
        //        MenuItem mi1 = new MenuItem("Startpagina", "http://www.google.com", true, 1);
        //        midb.persist(mi1);
        //        MenuItem mi2 = new MenuItem("Beheer", "http://www.gmail.com", true, 2);
        //        midb.persist(mi2);
                
                out.println("<br />ALL");
                List<MenuItem> all = midb.list();
                
        //        gt.addRecht(mi1.getKey());
        //        gtdb.update(gt);
                
                for (MenuItem item : all) {
                    out.println("<br /> - " + item);
                }
                
                out.println("<br />PRIVILIGED to ");
                List<MenuItem> items = midb.list(gebruiker);
                
                for (MenuItem item : items) {
                    out.println("<br /> - " + item);
                }
                
                Date startDate = DateUtil.date(2000, 1, 1);
                OptieTypeDB otdb = new OptieTypeDB();
        //        otdb.persist(new OptieType("Kleur", "Kleur van het papier voor de gehele opdracht", 1, 1, startDate));
                
                OptieType ot = otdb.getCurrentWithName("Kleur");
                
                out.println("<br />" + ot);
                
                OptieDB odb = new OptieDB();
        //        Optie o = new Optie(ot, "Rood", "Rood papier", startDate, OptieStatus.HUIDIG, 1);
        //        odb.persist(o);
                
                out.println("<br />" + odb.getCurrentOfTypeWithName(ot, "Rood"));
                
                InputVeldDB ivdb = new InputVeldDB();
          //      InputVeld iv = new InputVeld("Aantal", InputVeldType.VAST, 1, 999);
          //      ivdb.persist(iv);
                
                out.println("<br />" + ivdb.getWithName("Aantal"));
                
            %>
        </ul>
    </body>
</html>
