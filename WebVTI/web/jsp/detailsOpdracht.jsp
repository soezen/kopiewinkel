<%-- 
    Document   : detailsOpdracht
    Created on : 26-mrt-2012, 21:10:27
    Author     : soezen
--%>

<%@page import="domain.Doelgroep"%>
<%@page import="java.util.ArrayList"%>
<%@page import="database.DoelgroepDB"%>
<%@page import="database.OpdrachtTypeDB"%>
<%@page import="database.GebruikerDB"%>
<%@page import="java.util.List"%>
<%@page import="database.OptieDB"%>
<%@page import="domain.Optie"%>
<%@page import="database.OpdrachtDB"%>
<%@page import="domain.enums.OpdrachtStatus"%>
<%@page import="domain.Opdracht"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
            Long id = Long.valueOf(request.getParameter("id"));
            OpdrachtDB db = new OpdrachtDB();
            Opdracht newOpdracht = db.getWithId(id);
            request.setAttribute("newOpdracht", newOpdracht);
            
            GebruikerDB gdb = new GebruikerDB();
            request.setAttribute("opdrachtgever", gdb.get(newOpdracht.getOpdrachtgever()));
            request.setAttribute("uitvoerder", gdb.get(newOpdracht.getUitvoerder()));
            
            OpdrachtTypeDB otdb = new OpdrachtTypeDB();
            request.setAttribute("opdrachtType", otdb.get(newOpdracht.getOpdrachtType()));
            
            OptieDB odb = new OptieDB();
            List<Optie> opties = odb.list(newOpdracht.getOpties());
            request.setAttribute("opties", opties);
            
            DoelgroepDB ddb = new DoelgroepDB();
            List<Doelgroep> doelgroepen = new ArrayList<Doelgroep>();
            if (!newOpdracht.getDoelgroepen().isEmpty()) {
                doelgroepen = ddb.list(newOpdracht.getDoelgroepen());
            }
            request.setAttribute("doelgroepen", doelgroepen);
%>
<style type="text/css">
    #opdrachtBody>div>div>label {
        margin: 5px;
        margin-right: 15px;
        float:left;
        width:150px;
        clear:left;
        text-align: right;
        font-weight: bold;
    }

    #opdrachtBody>div>div>div {
        margin: 5px;
        float:left;
    }

    #opdrachtBody>div>div {
        float:left;
    }

    #opdrachtBody>div>div>div>label {
        font-weight: bold;
        text-align: left;
    }

    #opdrachtBody>div>div>div>div {
        margin-left: 5px;
    }
</style>
<div>
    <div>
        <label>ID</label>
        <div>${newOpdracht.id}</div>
        <label>Opdrachtgever</label>
        <div>${opdrachtgever.naam}</div>
        <label>Type</label>
        <div>${opdrachtType.naam}</div>
        <label>Aanmaakdatum</label>
        <div><fmt:formatDate value="${newOpdracht.aanmaakDatum}" pattern="dd-MM-yyyy" /></div>
        <label>Bestand</label>
        <div>${newOpdracht.bestand}</div>
        <label>Aantal</label>
        <div>${newOpdracht.aantal}</div>
        <label>Status</label>
        <div>${newOpdracht.status.naam}</div>
        <label>Printdatum</label>
        <div><fmt:formatDate value="${newOpdracht.printDatum}" /></div>
        <label>Uitvoerder</label>
        <div>${uitvoerder.naam}</div>
        <label>Commentaar</label>
        <div>${newOpdracht.commentaar}</div>
        <label>Prijs</label>
        <div><a src="">${newOpdracht.prijs}</a></div>
    </div>
    <div>
        <label>Opties</label>
        <div>
            <c:set var="previousType" value="" />
            <c:forEach items="${opties}" var="optie">
                <c:if test="${previousType != optie.optieType.id}">
                    <label>${optie.optieType.naam}</label>
                </c:if>
                <c:set var="previousType" value="${optie.optieType.id}" />
                <div>${optie.naam}</div>
            </c:forEach>
        </div>
    </div>
    <div>
        <label>Klassen</label>
        <div>
            <c:set var="previousKlas" value="" />
            <c:forEach items="${doelgroepen}" var="doelgroep">
                <c:if test="${previousKlas != doelgroep.graad}">
                    <c:if test="${previousKlas != ''}">
                        <%= "</ul>"%>
                    </c:if>
                    <label>Graad ${doelgroep.graad}</label>
                    <%= "<ul>"%>
                </c:if>
                <c:set var="previousKlas" value="${doelgroep.graad}" />
                <li>${doelgroep.naam}</li>
            </c:forEach>
        </div>
    </div>
</div>
