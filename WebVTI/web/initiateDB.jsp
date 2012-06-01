<%-- 
    Document   : index
    Created on : 1-mei-2012, 14:19:42
    Author     : soezen
--%>

<%@page import="database.setup.SetupDB"%>
<%@page import="database.InputWaardeDB"%>
<%@page import="domain.InputWaarde"%>
<%@page import="domain.enums.OpdrachtStatus"%>
<%@page import="domain.Opdracht"%>
<%@page import="database.OpdrachtDB"%>
<%@page import="database.OpdrachtTypeInputDB"%>
<%@page import="domain.OpdrachtTypeInput"%>
<%@page import="domain.OpdrachtType"%>
<%@page import="database.OpdrachtTypeDB"%>
<%@page import="database.PrijsKlasseDB"%>
<%@page import="domain.PrijsKlasse"%>
<%@page import="domain.constraints.FormuleConstraint"%>
<%@page import="domain.PrijsFormule"%>
<%@page import="database.PrijsFormuleDB"%>
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
        <title>Initiate DB</title>
    </head>
    <body>
        <h1>Initiate DB</h1>
            <%
               SetupDB.removeAll();
               SetupDB.insertData();
            %>
            <h2>DONE</h2>
    </body>
</html>
