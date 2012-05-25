<%-- 
    Document   : newOpdrachtForm
    Created on : 21-feb-2012, 17:35:09
    Author     : soezen
--%>

<%@page import="domain.OptieType"%>
<%@page import="java.util.HashMap"%>
<%@page import="domain.Doelgroep"%>
<%@page import="domain.enums.InputVeldType"%>
<%@page import="domain.OpdrachtTypeInput"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="f" uri="/tld/custom.tld"%>
<%@page import="java.util.List"%>
<%@page import="domain.Gebruiker"%>
<%@page import="database.ConnectionManager"%>
<%@page import="domain.InputVeld"%>
<%@page import="domain.OpdrachtType"%>
<%@page import="database.OpdrachtDB"%>
<%@page import="domain.Opdracht"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<jsp:useBean id="opdracht" scope="session" class="decorators.OpdrachtDecorator" />
<jsp:setProperty name="opdracht" property="opdrachtType" param="type" />
<%
            Gebruiker gebruiker = (Gebruiker) session.getAttribute("gebruiker");
            if (gebruiker == null) {
                gebruiker = ConnectionManager.getGastGebruiker();
            }
            OpdrachtDB db = new OpdrachtDB();
            Opdracht o = new Opdracht();
            OpdrachtType ot = db.getOpdrachtType(opdracht.opdrachtType);
            o.setOpdrachtType(ot);

            List<OpdrachtTypeInput> velden = db.getInputVelden(ot, gebruiker);
            request.setAttribute("velden", velden);
            request.setAttribute("opdrachtType", ot);
%>
<div>
    <button onclick="save()">Volgende</button>
</div>
<form id="opdrachtForm" action="InputOpdrachtServlet" method="post" enctype="multipart/form-data">

    <div id="top">
        <%= "<div class='velden'>"%>
        <c:forEach items="${velden}" var="veld">
            <c:if test="${veld.inputVeld.naam == 'Klassen'}">
                <%= "</div>"%>
            </c:if>
            <div id="${veld.inputVeldInt}">
                <label for="${veld.inputVeldInt}">${veld.inputVeld.naam}</label>

                <c:choose>
                    <c:when test="${veld.inputVeld.type == 'VAST'}">
                        <c:choose>
                            <c:when test="${veld.inputVeld.naam == 'Klassen'}">
                                (<c:choose>
                                    <c:when test="${veld.verplicht}">
                                        <input type="number" class="txtLabel" id="totalKlassen" value="0" min="1" required disabled readonly />
                                    </c:when>
                                    <c:otherwise>
                                        <input type="number" class="txtLabel" id="totalKlassen" value="0" disabled readonly />
                                    </c:otherwise>
                                </c:choose> geselecteerd)
                                <a href="#" onclick="toggleToewijzingen(${veld.inputVeldInt})">toon selectie</a>
                                <div id="klassen">
                                    <%
                                                List<Doelgroep> doelgroepen = db.getDoelgroepenHuidigSchooljaar();
                                                HashMap<Integer, Doelgroep> toewijzingen = new HashMap<Integer, Doelgroep>();
                                                for (Doelgroep d : gebruiker.getDoelgroepen()) {
                                                    toewijzingen.put(d.getId(), d);
                                                }
                                                request.setAttribute("doelgroepen", doelgroepen);
                                                request.setAttribute("toewijzingen", toewijzingen);

                                                // TODO constraints tussen kleur en gewicht kaft maken
                                    %>
                                    <c:set var="prev" value="0" />
                                    <c:forEach items="${doelgroepen}" var="doelgroep">
                                        <c:if test="${prev < doelgroep.graad}">
                                            <c:if test="${prev != 0}">
                                                <%= "</div>"%>
                                            </c:if>
                                            <h3><a href="#">Graad ${doelgroep.graad}</a></h3>
                                            <c:set var="prev" value="${prev+1}" />
                                            <%= "<div>"%>
                                        </c:if>
                                        <c:forEach items="${doelgroep.aantallenPerGroep}" var="groep">
                                            <div>
                                                <c:set var="groepLabel" value="${doelgroep.naam} (${groep.value})" />
                                                <c:choose>
                                                    <c:when test="${fn:length(doelgroep.aantallenPerGroep) > 1}">
                                                        <c:set var="groepLabel" value="${doelgroep.naam} - ${groep.key} (${groep.value})" />
                                                    </c:when>
                                                </c:choose>
                                                <c:choose>
                                                    <c:when test="${toewijzingen[doelgroep.id] == null}">
                                                        <input type="hidden" value="${groep.value}" />
                                                        <input type="checkbox" id="${doelgroep.id}-${groep.key}" name="${veld.inputVeldInt}" value="${doelgroep.id}" onchange="updateAantal(this, ${groep.value})" />
                                                        <label for="${doelgroep.id}-${groep.key}">${groepLabel}</label>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="checkbox" id="${doelgroep.id}-${groep.key}" class="toewijzing" name="${veld.inputVeldInt}" value="${doelgroep.id}" onchange="updateAantal(this, ${groep.value})" />
                                                        <label for="${doelgroep.id}-${groep.key}" class="toewijzing">${groepLabel}</label>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </c:forEach>
                                    </c:forEach>
                                    <%= "</div>"%>
                                </div>
                            </c:when>
                            <c:when test="${veld.inputVeld.naam == 'Opdrachtgever'}">
                                <%
                                            List<Gebruiker> gebruikers = db.getOpdrachtGevers(ot);
                                            request.setAttribute("gebruikers", gebruikers);
                                %>
                                <c:choose>
                                    <c:when test="${veld.wijzigbaar}">
                                        <select id="${veld.inputVeldInt}" name="${veld.inputVeldInt}">
                                            <c:forEach items="${gebruikers}" var="gebruiker">
                                                <option value="${gebruiker.id}">${gebruiker.naam}</option>
                                            </c:forEach>
                                        </select>
                                    </c:when>
                                    <c:otherwise>
                                        <select id="${veld.inputVeldInt}" name="${veld.inputVeldInt}" disabled>
                                            <c:forEach items="${gebruikers}" var="gebruiker">
                                                <option>${gebruiker.naam}</option>
                                            </c:forEach>
                                        </select>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:when test="${veld.inputVeld.naam == 'Bestand'}">
                                <input type="file" id="${veld.inputVeldInt}" name="${veld.inputVeldInt}" required accept="application/pdf,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/msword" />
                            </c:when>
                            <c:when test="${veld.inputVeld.naam == 'Aantal'}">
                                <span id="aantal">
                                    <c:choose>
                                        <c:when test="${veld.wijzigbaar && veld.verplicht}">
                                            <input type="number" id="${veld.inputVeldInt}" name="${veld.inputVeldInt}" min="1" max="999" value="1" required />
                                        </c:when>
                                        <c:when test="${veld.wijzigbaar && !veld.verplicht}">
                                            <input type="number" id="${veld.inputVeldInt}" name="${veld.inputVeldInt}" min="1" max="999" value="1" />
                                        </c:when>
                                        <c:when test="${!veld.wijzigbaar && veld.verplicht}">
                                            <input type="number" id="${veld.inputVeldInt}" name="${veld.inputVeldInt}" min="1" max="999" value="1" disabled required />
                                        </c:when>
                                        <c:otherwise>
                                            <input type="number" id="${veld.inputVeldInt}" name="${veld.inputVeldInt}" min="1" max="999" value="1" disabled />
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </c:when>
                            <c:when test="${veld.inputVeld.naam == 'Commentaar'}">
                                <c:choose>
                                    <c:when test="${veld.wijzigbaar && veld.verplicht}">
                                        <textarea id="${veld.inputVeldInt}" name="${veld.inputVeldInt}" required cols="25" rows="5"></textarea>
                                    </c:when>
                                    <c:when test="${veld.wijzigbaar && !veld.verplicht}">
                                        <textarea id="${veld.inputVeldInt}" name="${veld.inputVeldInt}" cols="25" rows="5"></textarea>
                                    </c:when>
                                    <c:when test="${!veld.wijzigbaar && veld.verplicht}">
                                        <textarea id="${veld.inputVeldInt}" name="${veld.inputVeldInt}" required disabled cols="25" rows="5"></textarea>
                                    </c:when>
                                    <c:otherwise>
                                        <textarea id="${veld.inputVeldInt}" name="${veld.inputVeldInt}" disabled cols="25" rows="5"></textarea>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${veld.wijzigbaar && veld.verplicht}">
                                        <input type="text" id="${veld.inputVeldInt}" name="${veld.inputVeldInt}" required />
                                    </c:when>
                                    <c:when test="${veld.wijzigbaar && !veld.verplicht}">
                                        <input type="text" id="${veld.inputVeldInt}" name="${veld.inputVeldInt}" />
                                    </c:when>
                                    <c:when test="${!veld.wijzigbaar && veld.verplicht}">
                                        <input type="text" id="${veld.inputVeldInt}" name="${veld.inputVeldInt}" disabled required />
                                    </c:when>
                                    <c:otherwise>
                                        <input type="text" id="${veld.inputVeldInt}" name="${veld.inputVeldInt}" disabled />
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <!-- TODO when date add maxlength and size attributes -->
                        <c:choose>
                            <c:when test="${veld.wijzigbaar && !veld.verplicht}">
                                <input type="${veld.inputVeld.type.naam}" id="${veld.inputVeldInt}" name="${veld.inputVeldInt}" />
                            </c:when>
                            <c:when test="${veld.wijzigbaar && veld.verplicht}">
                                <input type="${veld.inputVeld.type.naam}" id="${veld.inputVeldInt}" name="${veld.inputVeldInt}" required />
                            </c:when>
                            <c:when test="${!veld.wijzigbaar && veld.verplicht}">
                                <input type="${veld.inputVeld.type.naam}" id="${veld.inputVeldInt}" name="${veld.inputVeldInt}" disabled required />
                            </c:when>
                            <c:otherwise>
                                <input type="${veld.inputVeld.type.naam}" id="${veld.inputVeldInt}" name="${veld.inputVeldInt}" disabled />
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:forEach>
        <%= "</div>"%>
    </div>
    <div id="opties">
        <c:forEach items="${opdrachtType.optieTypes}" var="optieType">
            <c:set var="style" value="" />
            <c:if test="${f:isVisibleOrEnabled(optieType)}">
                <c:set var="style" value="style='display:none'" />
            </c:if>
            <div class="opties ui-accordion ui-widget ui-helper-reset ui-accordion-icons" ${style}>
                <h3 class="ui-widget-header ui-helper-reset ui-state-default ui-state-active ui-corner-top">
                    <label for="${optieType.id}Aantal">${optieType.naam}</label>
                    <input type="number" id="${optieType.id}Aantal" required disabled step="1" value="0" min="${optieType.min}" max="${optieType.max}" />
                </h3>
                <div id="${optieType.id}" class="ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active">
                    ${optieType.omschrijving}
                    <br />
                    <c:forEach items="${optieType.opties}" var="optie">
                        <c:choose>
                            <c:when test="${f:isVisibleOrEnabled(optie)}">
                                <input type="checkbox" id="${optie.id}" value="${optie.id}" name="${optie.id}" onchange="setSelected(this, null, [], function() {})" disabled />
                                <label for="${optie.id}" title="${optie.omschrijving}">${optie.naam}</label>
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox" id="${optie.id}" name="${optie.id}" value="${optie.id}" onchange="setSelected(this, null, [], function() {})" />
                                <label for="${optie.id}" title="${optie.omschrijving}">${optie.naam}</label>
                            </c:otherwise>
                        </c:choose>
                        <br />
                    </c:forEach>
                </div>
            </div>
        </c:forEach>
    </div>
</form>