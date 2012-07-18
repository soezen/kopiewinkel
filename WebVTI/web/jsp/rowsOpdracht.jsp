<%-- 
    Document   : rowsOpdracht
    Created on : 30-apr-2012, 7:04:32
    Author     : soezen
--%>

<%@page import="database.OptieDB"%>
<%@page import="database.OpdrachtTypeDB"%>
<%@page import="domain.Gebruiker"%>
<%@page import="database.GebruikerDB"%>
<%@page import="domain.Opdracht"%>
<%@page import="database.OpdrachtDB"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
    OpdrachtDB db = new OpdrachtDB();
    String opdrachtId = request.getParameter("opdracht");
    Opdracht opdracht = db.getWithId(Long.valueOf(opdrachtId));
    request.setAttribute("opdracht", opdracht);
    
    GebruikerDB gdb = new GebruikerDB();
    request.setAttribute("opdrachtgever", gdb.get(opdracht.getOpdrachtgever()));
    if (opdracht.getUitvoerder() != null) {
        request.setAttribute("uitvoerder", gdb.get(opdracht.getUitvoerder()));
    }
    
    OpdrachtTypeDB otdb = new OpdrachtTypeDB();
    request.setAttribute("opdrachtType", otdb.get(opdracht.getOpdrachtType()));
    
    OptieDB odb = new OptieDB();
    request.setAttribute("opties", odb.list(opdracht.getOpties()));
%>
<c:set var="class" value="status${opdracht.status.code}" />


<tr id="${opdracht.id}" class="${class}">
    <td>${opdracht.id}</td>
    <td><fmt:formatDate value="${opdracht.aanmaakDatum}" pattern="dd/MM/yyyy" /></td>
    <td>${opdrachtgever.naam}</td>
    <td>${opdrachtType.naam}</td>
    <!-- change to link on server -->
    <td>${opdracht.bestand}</td>
    <td>${opdracht.aantal}</td>
    <td>${opdracht.status.naam}</td>
</tr>
<tr class="rowDetails">
    <td colspan="6">
        <div class="details">
            <div>
                <p class="div-header">Acties</p>
               <ul class="list-buttons" status="${opdracht.status.code}">
                    <li class="starten" onclick="startenOpdracht(${opdracht.id}, this.parentNode)">Starten</li>
                    <li class="afwerken" onclick="afwerkenOpdracht(${opdracht.id}, this.parentNode)">Afwerken</li>
                    <li class="dataTekort" onclick="dataTekortOpdracht(${opdracht.id}, this.parentNode)">Data Tekort</li>
                    <li class="weigeren" onclick="weigerenOpdracht(${opdracht.id}, this.parentNode)">Weigeren</li>
                    <li onclick="wijzigenOpdracht(${opdracht.id}, this.parentNode">Wijzigen</li>
                </ul>
            </div>
            <div>
                <p class="div-header">Algemene informatie</p>
                <c:if test="${opdracht.status == 'AFGEWERKT'}">
                    <i>Uitgevoerd op ${opdracht.printDatum} door ${uitvoerder.naam}</i>
                </c:if>
                <p class="strong">Commentaar:</p> ${opdracht.commentaar}<br />
                <c:if test="${fn:length(opdracht.inputWaarden) > 0}">
                    <p class="strong">Extra gegevens:</p><br />
                    <table>
                        <c:forEach items="${opdracht.inputWaarden}" var="input">
                            <tr>
                                <!-- TODO get naam from inputveld -->
                                <td>${input.inputVeld}</td>
                                <td>${input.waarde}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:if>
            </div>
            <div>
                <p class="div-header">Opties</p>
                <table>
                    <c:set var="count" value="0" />
                    <c:forEach items="${opties}" var="optie" >
                        <tr>
                            <td class="strong">${optie.optieType.naam}</td>
                            <td>&gt;</td>
                            <td>${optie.naam}</td>
                        </tr>
                        <c:set var="count" value="${count + 1}" />
                        <c:if test="${count > 4}">
                            <c:set var="count" value="0" />
                            <%= "</table><table>" %>    
                        </c:if>
                    </c:forEach>
                </table>
            </div>
        </div>
    </td>
</tr>
