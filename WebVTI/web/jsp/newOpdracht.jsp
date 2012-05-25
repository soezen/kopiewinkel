<%-- 
    Document   : newOpdracht
    Created on : 19-feb-2012, 19:11:33
    Author     : soezen
--%>

<%@page import="database.ConnectionManager"%>
<%@page import="java.util.List"%>
<%@page import="domain.Gebruiker"%>
<%@page import="domain.OpdrachtType"%>
<%@page import="database.OpdrachtDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
            Gebruiker gebruiker = (Gebruiker) session.getAttribute("gebruiker");
            if (gebruiker == null) {
                gebruiker = ConnectionManager.getGastGebruiker();
            }
%>
<jsp:useBean id="opdracht" scope="application" class="decorators.OpdrachtDecorator" />

<script type="text/javascript">
    var getOpdrachtItems = function() {
        console.log("get opdracht form");
        var select = document.getElementById("typeSelect");
        var option = select.options[select.selectedIndex].id;
                
        redirect("jsp/newOpdrachtForm.jsp?type=" + option, function(xmlhttp) {
            options = document.getElementById("typeSelect").options;
            original = document.getElementById("originalBody");
            if (!original) {
                original = document.getElementById("body");
                original.id = "originalBody";
            }
            original.innerHTML = xmlhttp.responseText;
            setSelectedOption(options.selectedIndex);
            $("#klassen").accordion();
            $("#klassen").parent().addClass("klassen");
            setDependencies();
            $("#opdrachtBody").off('click', "#next", getOpdrachtItems);
            $("#opdrachtBody").on('click', "#next", save);
            $("#previous").removeAttr('disabled');
            $("#opdrachtBody").on('click', '#previous', backToOpdrachtType);
        });
    }

    backToOpdrachtType = function() {
        console.log('back');
        redirect('jsp/newOpdracht.jsp', function(inXmlHttp) {
            var body = document.getElementById("opdrachtBody").parentNode;
            body.innerHTML = inXmlHttp.responseText;
        $("#opdrachtBody").on('click', "#next", getOpdrachtItems);
        $("#opdrachtBody").off('click', "#next", save);
        $("#previous").attr('disabled', '');
        $("#opdrachtBody").off('click', '#previous', backToOpdrachtType);
        });
    }

    function setDependencies() {
        var xmlhttp;
        if (window.XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
        } else {
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                object = eval(xmlhttp.responseText);
                functions.push([object.required, setRequired]);
                functions.push([object.required, setIsRequiredBy]);
                functions.push([object.forbidden, setForbidden]);
            }
        }
        xmlhttp.open("GET", "DataServlet?action=constraints");
        xmlhttp.send();
    }

    function setSelectedOption(index) {
        select = document.getElementById("typeSelect");
        select.options[index].selected = true;
        // change to nicer layout
        // remove button
        select.disabled = true;
        next = document.getElementById("next");
            
    }

    $(document).ready(function(){
        $("#opdrachtBody").on('click', '#next', getOpdrachtItems);
        $("#previous").attr('disabled', '');
    });
</script>
<h1>Nieuwe Opdracht</h1>
<div id="opdrachtBody">
    <div class="buttons">
        <button id="cancel">Annuleer</button>
        <button id="previous">Vorige</button>
        <button id="next">Volgende</button>
    </div>
    <div class="error-background">
        <p>Errors:</p>
        <ul id="errors">
        </ul>
    </div>
    <table>
        <tr>
            <td>Type</td>
            <td>
                <select id="typeSelect">
                    <%
                                OpdrachtDB opdrachtDB = new OpdrachtDB();
                                List<OpdrachtType> types = opdrachtDB.getActieveOpdrachtTypes(gebruiker);
                                for (OpdrachtType type : types) {
                                    out.println("<option id='" + type.getId() + "'>" + type.getNaam() + "</option>");
                                }
                    %>
                </select>
            </td>
        </tr>
    </table>
    <div id="body">
    </div>
</div>
