<%-- 
    Document   : rowsOpdracht
    Created on : 30-apr-2012, 7:04:32
    Author     : soezen
--%>

<%@page import="domain.Opdracht"%>
<%@page import="database.OpdrachtDB"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
    OpdrachtDB db = new OpdrachtDB();
    String opdrachtId = request.getParameter("opdracht");
    Opdracht opdracht = db.getOpdracht(Integer.valueOf(opdrachtId));
    request.setAttribute("opdracht", opdracht);
%>
<c:set var="class" value="statusAangevraagd" />
<c:choose>
    <c:when test="${opdracht.status == 'IN_BEHANDELING'}">
        <c:set var="class" value="statusInBehandeling" />
    </c:when>
    <c:when test="${opdracht.status == 'AFGEWERKT'}">
        <c:set var="class" value="statusAfgewerkt" />
    </c:when>
    <c:when test="${opdracht.status == 'DATA_TEKORT'}">
        <c:set var="class" value="statusDataTekort" />
    </c:when>
    <c:when test="${opdracht.status == 'GEWEIGERD'}">
        <c:set var="class" value="statusGeweigerd" />
    </c:when>
</c:choose>

<tr class="${class}">
    <td>${opdracht.id}</td>
    <td>${opdracht.aanmaakDatum}</td>
    <td>${opdracht.opdrachtgever.naam}</td>
    <td>${opdracht.opdrachtType.naam}</td>
    <!-- change to link on server -->
    <td>${opdracht.bestand}</td>
    <td>${opdracht.aantal}</td>
    <td>${opdracht.status.naam}</td>
</tr>
<tr class="rowDetails">
    <td colspan="6">
        <div class="details">
            <div>
                <strong>Acties:</strong>
                <ul>
                    <c:if test="${opdracht.status == 'AANGEVRAAGD' || opdracht.status == 'DATA_TEKORT'}">
                        <li><a onclick="startenOpdracht(${opdracht.id})">Starten</a></li>
                    </c:if>
                    <c:if test="${opdracht.status == 'IN_BEHANDELING'}">
                        <li><a onclick="afwerkenOpdracht(${opdracht.id})">Afwerken</a></li>
                    </c:if>
                    <c:if test="${opdracht.status == 'AANGEVRAAGD' || opdracht.status == 'IN_BEHANDELING'}">
                        <li><a onclick="dataTekortOpdracht(${opdracht.id})">Data te kort</a></li>
                    </c:if>
                    <c:if test="${opdracht.status == 'AANGEVRAAGD' || opdracht.status == 'IN_BEHANDELING' || opdracht.status == 'DATA_TEKORT'}">
                        <li><a onclick="weigerenOpdracht(${opdracht.id})">Weigeren</a></li>
                    </c:if>
                    <li><a onclick="wijzigenOpdracht(${opdracht.id})">Wijzigen</a></li>
                </ul>
            </div>
            <div>
                <c:if test="${opdracht.status == 'AFGEWERKT'}">
                    <i>Uitgevoerd op ${opdracht.printDatum} door ${opdracht.uitvoerder}</i>
                </c:if>
                <strong>Commentaar:</strong> ${opdracht.commentaar}<br />
                <strong>Extra gegevens:</strong><br />
                <table>
                    <c:forEach items="${opdracht.inputWaarden}" var="input">
                        <tr>
                            <td>${input.inputVeld.naam}</td>
                            <td>${input.waarde}</td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
            <div>
                <strong>Opties:</strong>
                <table>
                    <c:forEach items="${opdracht.opties}" var="optie">
                        <tr>
                            <td>${optie.optieType.naam}</td>
                            <td>${optie.naam}</td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
    </td>
</tr>
