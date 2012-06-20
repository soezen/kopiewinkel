<%-- 
    Document   : newOpdrachtForm
    Created on : 21-feb-2012, 17:35:09
    Author     : soezen
--%>

<%@page import="database.OptieTypeDB"%>
<%@page import="database.OpdrachtTypeInputDB"%>
<%@page import="database.OpdrachtTypeDB"%>
<%@page import="database.GebruikerDB"%>
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
<%@page import="domain.InputVeld"%>
<%@page import="domain.OpdrachtType"%>
<%@page import="database.OpdrachtDB"%>
<%@page import="domain.Opdracht"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<jsp:useBean id="opdracht" scope="session" class="decorators.OpdrachtDecorator" />
<jsp:setProperty name="opdracht" property="opdrachtType" param="type" />
<%
            GebruikerDB gdb = new GebruikerDB();
            Gebruiker gebruiker = (Gebruiker) session.getAttribute("gebruiker");
            if (gebruiker == null) {
                gebruiker = gdb.getGastGebruiker();
            }
            OpdrachtDB odb = new OpdrachtDB();
            OpdrachtTypeDB otdb = new OpdrachtTypeDB();
            Opdracht o = new Opdracht();
            OpdrachtType ot = otdb.getWithId(opdracht.opdrachtType);
            o.setOpdrachtType(ot);

            OpdrachtTypeInputDB otidb = new OpdrachtTypeInputDB();
            // TODO get only for gebruiker?
            List<OpdrachtTypeInput> velden = ot.getInputVelden();
            request.setAttribute("velden", velden);
            request.setAttribute("opdrachtType", ot);
%>
<form id="opdrachtForm" action="InputOpdrachtServlet" method="post" enctype="multipart/form-data">

    <div id="top">
        <%= "<div class='velden'>"%>
        <c:forEach items="${velden}" var="veld">
            <c:if test="${veld.inputVeld.naam == 'Klassen'}">
                <%= "</div>"%>
            </c:if>
            <div id="${veld.inputVeld.id}">
                <c:choose>
                    <c:when test="${veld.zichtbaar}">
                
                <label for="${veld.inputVeld.id}">${veld.inputVeld.naam}</label>
                    </c:when>
                </c:choose>
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
                                <a href="#" onclick="toggleToewijzingen(${veld.inputVeld.id})">toon selectie</a>
                                <div id="klassen">
                                    <%
                                            // TODO
                                            //     List<Doelgroep> doelgroepen = db.getDoelgroepenHuidigSchooljaar();
                                            //    HashMap<Integer, Doelgroep> toewijzingen = new HashMap<Integer, Doelgroep>();
                                            //    for (Doelgroep d : gebruiker.getDoelgroepen()) {
                                            //        toewijzingen.put(d.getId(), d);
                                            //    }
                                            //    request.setAttribute("doelgroepen", doelgroepen);
                                            //    request.setAttribute("toewijzingen", toewijzingen);

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
                                                        <input type="checkbox" id="${doelgroep.id}-${groep.key}" name="${veld.inputVeld.id}" value="${doelgroep.id}" onchange="updateAantal(this, ${groep.value})" />
                                                        <label for="${doelgroep.id}-${groep.key}">${groepLabel}</label>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="checkbox" id="${doelgroep.id}-${groep.key}" class="toewijzing" name="${veld.inputVeld.id}" value="${doelgroep.id}" onchange="updateAantal(this, ${groep.value})" />
                                                        <label for="${doelgroep.id}-${groep.key}" class="toewijzing">${groepLabel}</label>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </c:forEach>
                                    </c:forEach>
                                    <%= "</div>"%>
                                </div>
                            </c:when>
                            <c:when test="${veld.inputVeld.naam == 'Opdrachtgever' && veld.zichtbaar}">
                                <%  
                                            List<Gebruiker> gebruikers = gdb.getOpdrachtGevers(ot);
                                            request.setAttribute("gebruikers", gebruikers);
                                %>
                                <c:choose>
                                    <c:when test="${veld.wijzigbaar}">
                                        <select id="${veld.inputVeld.id}" name="${veld.inputVeld.id}">
                                            <c:forEach items="${gebruikers}" var="gebruiker">
                                                <option value="${gebruiker.id}">${gebruiker.naam}</option>
                                            </c:forEach>
                                        </select>
                                    </c:when>
                                    <c:otherwise>
                                        <select id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" disabled>
                                            <c:forEach items="${gebruikers}" var="gebruiker">
                                                <option>${gebruiker.naam}</option>
                                            </c:forEach>
                                        </select>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:when test="${veld.inputVeld.naam == 'Bestand' && veld.zichtbaar}">
                                <input type="file" id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" required accept="application/pdf,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/msword" />
                            </c:when>
                            <c:when test="${veld.inputVeld.naam == 'Aantal'}">
                                <span id="aantal">
                                    <c:choose>
                                        <c:when test="${veld.wijzigbaar && veld.verplicht}">
                                            <input type="number" id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" min="1" max="999" value="1" required />
                                        </c:when>
                                        <c:when test="${veld.wijzigbaar && !veld.verplicht}">
                                            <input type="number" id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" min="1" max="999" value="1" />
                                        </c:when>
                                        <c:when test="${!veld.wijzigbaar && veld.verplicht}">
                                            <input type="number" id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" min="1" max="999" value="1" disabled required />
                                        </c:when>
                                        <c:otherwise>
                                            <input type="number" id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" min="1" max="999" value="1" disabled />
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </c:when>
                            <c:when test="${veld.inputVeld.naam == 'Commentaar'}">
                                <c:choose>
                                    <c:when test="${veld.wijzigbaar && veld.verplicht}">
                                        <textarea id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" required cols="25" rows="5"></textarea>
                                    </c:when>
                                    <c:when test="${veld.wijzigbaar && !veld.verplicht}">
                                        <textarea id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" cols="25" rows="5"></textarea>
                                    </c:when>
                                    <c:when test="${!veld.wijzigbaar && veld.verplicht}">
                                        <textarea id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" required disabled cols="25" rows="5"></textarea>
                                    </c:when>
                                    <c:otherwise>
                                        <textarea id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" disabled cols="25" rows="5"></textarea>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:when test="${veld.inputVeld.naam == 'Prijs'}">
                                    <c:choose>
                                        <c:when test="${veld.wijzigbaar && veld.verplicht}">
                                            <input type="number" id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" value="0" required />
                                        </c:when>
                                        <c:when test="${veld.wijzigbaar && !veld.verplicht}">
                                            <input type="number" id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" value="0" />
                                        </c:when>
                                        <c:when test="${!veld.wijzigbaar && veld.verplicht}">
                                            <input type="number" id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" value="0" disabled required />
                                        </c:when>
                                        <c:otherwise>
                                            <input type="number" id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" value="0" disabled />
                                        </c:otherwise>
                                    </c:choose>
                            </c:when>
                            <c:when test="${veld.zichtbaar}">
                                <c:choose>
                                    <c:when test="${veld.wijzigbaar && veld.verplicht}">
                                        <input type="text" id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" required />
                                    </c:when>
                                    <c:when test="${veld.wijzigbaar && !veld.verplicht}">
                                        <input type="text" id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" />
                                    </c:when>
                                    <c:when test="${!veld.wijzigbaar && veld.verplicht}">
                                        <input type="text" id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" disabled required />
                                    </c:when>
                                    <c:otherwise>
                                        <input type="text" id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" disabled />
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <!-- TODO when date add maxlength and size attributes -->
                        <c:choose>
                            <c:when test="${veld.wijzigbaar && !veld.verplicht}">
                                <input type="${veld.inputVeld.type.naam}" id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" />
                            </c:when>
                            <c:when test="${veld.wijzigbaar && veld.verplicht}">
                                <input type="${veld.inputVeld.type.naam}" id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" required />
                            </c:when>
                            <c:when test="${!veld.wijzigbaar && veld.verplicht}">
                                <input type="${veld.inputVeld.type.naam}" id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" disabled required />
                            </c:when>
                            <c:otherwise>
                                <input type="${veld.inputVeld.type.naam}" id="${veld.inputVeld.id}" name="${veld.inputVeld.id}" disabled />
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:forEach>
        <%= "</div>"%>
    </div>
    <div id="opties">
        <%
            OptieTypeDB optdb = new OptieTypeDB();
            List<OptieType> optieTypes = optdb.list(gebruiker);
            request.setAttribute("optieTypes", optieTypes);
        %>
        <c:forEach items="${optieTypes}" var="optieType">
            <c:set var="style" value="" />
            <c:if test="${f:isVisibleOrEnabled(optieType)}">
                <c:set var="style" value="style='display:none'" />
            </c:if>
            <div class="opties ui-accordion ui-widget ui-helper-reset ui-accordion-icons" ${style}>
                <c:set var="h3class" value="ui-widget-header ui-helper-reset ui-state-default ui-state-active ui-corner-top" />
                <c:if test="${optieType.min > 0}">
                    <c:set var="h3class" value="${h3class} error" />
                </c:if>
                <h3 class="${h3class}">
                    <label for="OT${optieType.id}Aantal">${optieType.naam}</label>
                    <input type="number" id="OT${optieType.id}Aantal" required disabled step="1" value="0" min="${optieType.min}" max="${optieType.max}" />
                </h3>
                <div id="OT${optieType.id}" class="ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active">
                    ${optieType.omschrijving}
                    <br />
                    <c:forEach items="${optieType.opties}" var="optie">
                        <c:choose>
                            <c:when test="${f:isVisibleOrEnabled(optie)}">
                                <input type="checkbox" id="OP${optie.id}" value="${optie.id}" name="OP${optie.id}" onchange="setSelected(this, null, [], function() {})" disabled />
                                <label for="OP${optie.id}" title="${optie.omschrijving}">${optie.naam}</label>
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox" id="OP${optie.id}" name="OP${optie.id}" value="${optie.id}" onchange="setSelected(this, null, [], function() {})" />
                                <label for="OP${optie.id}" title="${optie.omschrijving}">${optie.naam}</label>
                            </c:otherwise>
                        </c:choose>
                        <br />
                    </c:forEach>
                </div>
            </div>
        </c:forEach>
    </div>
</form>