<%-- 
    Document   : manageOpdrachten
    Created on : 26-mrt-2012, 21:10:27
    Author     : soezen
--%>

<%@page import="database.GebruikerDB"%>
<%@page import="database.OpdrachtTypeDB"%>
<%@page import="domain.OpdrachtType"%>
<%@page import="domain.enums.OpdrachtStatus"%>
<%@page import="domain.Gebruiker"%>
<%@page import="database.OpdrachtDB"%>
<%@page import="java.util.List"%>
<%@page import="domain.Opdracht"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
            Gebruiker gebruiker = (Gebruiker) session.getAttribute("gebruiker");
            if (gebruiker == null) {
                GebruikerDB gdb = new GebruikerDB();
                gebruiker = gdb.getGastGebruiker();
            }
            
    request.setAttribute("statusList", OpdrachtStatus.values());
%>

<link rel="stylesheet" type="text/css"
      href="css/blue/style.css" />
<style type="text/css">
    .hidden {
        display: none;
    }

    .status5>td {
        background-color: wheat !important;
    }

    .status4>td {
        background-color: #EE5757 !important;
    }

    .status2>td {
        background-color: #FECA40 !important;
    }

    .status3 > td {
        background-color: #aaaaaa !important;
    }

    .rowDetails {
        display: none;
    }

    .details {
        height: 150px;
    }

    .details>div {
        margin: 5px;
        height: 100%; float : left;
        padding: 5px;
        border: 2px lightgray solid;
        float: left;
    }

    td>ul {
        list-style-type: none;
        padding: 0px;
        margin: 0px;
    }

    div>ul {
        list-style-type: none;
        padding: 7px;
        margin: 5px;
    }

    div>ul>li {
        margin-top: 2px;
    }

    div>ul>li:last-child {
        margin-top: 15px;
    }

    td>div>ul {
        list-style-type: none;
        padding: 0px;
        margin: 0px;
    }

    td>div>ul>li:last-child {
        margin-top: inherit;
    }
    
    ul[status="1"]>li>a.starten, ul[status="1"]>li>a.dataTekort, ul[status="1"]>li>a.weigeren, 
    ul[status="2"]>li>a.dataTekort, ul[status="2"]>li>a.weigeren, ul[status="2"]>li>a.afwerken,
    ul[status="4"]>li>a.starten, ul[status="4"]>li>a.weigeren {
        display:block;
    }
    
    ul[status="1"]>li>a.afwerken, 
    ul[status="2"]>li>a.starten, 
    ul[status="3"]>li>a.starten, ul[status="3"]>li>a.dataTekort, ul[status="3"]>li>a.weigeren, ul[status="3"]>li>a.afwerken,
    ul[status="4"]>li>a.dataTekort, ul[status="4"]>li>a.afwerken,
    ul[status="5"]>li>a.starten, ul[status="5"]>li>a.dataTekort, ul[status="5"]>li>a.weigeren, ul[status="5"]>li>a.afwerken {
        display:none;
    }
    
</style>
<link rel="stylesheet" href="../jquery/development-bundle/themes/cupertino/jquery.ui.all.css">


<script type="text/javascript" src="../jquery/jquery-latest.js"></script>
<script type="text/javascript" src="../jquery/jquery.tablesorter.js"></script>
<script type="text/javascript" src="../jquery/development-bundle/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="../jquery/development-bundle/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="../jquery/development-bundle/ui/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="../jquery/development-bundle/ui/jquery.ui.button.js"></script>
<script type="text/javascript" src="../jquery/development-bundle/ui/jquery.ui.draggable.js"></script>
<script type="text/javascript" src="../jquery/development-bundle/ui/jquery.ui.position.js"></script>
<script type="text/javascript" src="../jquery/development-bundle/ui/jquery.ui.resizable.js"></script>
<script type="text/javascript" src="../jquery/development-bundle/ui/jquery.ui.dialog.js"></script>
<script type="text/javascript" src="../jquery/development-bundle/ui/jquery.effects.core.js"></script>

<script type="text/javascript">
    function search(box, column) {
        var searchValue = box.value.toLowerCase();

        var rows = $("#results>tbody>tr");

        rows.each(function () {
            if (!$(this).hasClass("rowDetails")) {
                var value = this.getElementsByTagName("td")[column].innerHTML.toLowerCase();

                if (value.indexOf(searchValue) == -1) {
                    // search value not found -> hide row
                    this.style.display = "none";
                    var details = $(this).next("tr");
                    if (details.hasClass("rowDetails") && (details.css("display") == 'table-row' || !details.css("display"))) {
                        reverse = !reverse;
                        details.css("display", "none");
                        this.childNodes[1].rowSpan = 1;
                    }
                } else if (this.style.display == "none") {
                    // search value found and row used to be hidden
                    // see if there are other search values that need to be matched before showing row
                    checkAllColumns(this);
                }
            }
        });

    }

    function checkAllColumns(row) {
        var filters = document.getElementById("results").getElementsByTagName(
        "thead")[0].getElementsByTagName("td");

        var visible = true;
        for ( var i = 0; i < filters.length; i++) {
            var inputs = filters[i].getElementsByTagName("input");
            var value = row.getElementsByTagName("td")[i].innerHTML
            .toLowerCase();
            if (inputs.length == 1) {
                var filter = inputs[0].value.toLowerCase();

                if (value.indexOf(filter) == -1) {
                    visible = false;
                    break;
                }
            } else if (inputs.length == 2) {
                var beforeInput = inputs[1];
                var afterInput = inputs[0];

                visible = visible
                    && inInterval(row, i, true, new Date(afterInput.value),
                true);
                visible = visible
                    && inInterval(row, i, false,
                new Date(beforeInput.value), true);
            } else {
                var selects = filters[i].getElementsByTagName("select");
                if (selects.length > 0) {
                    // selection filter -> value has to be present in list
                    var filter = $(selects[0]).prev("div").find("ul");
                    var values = new Array();
                    filter.find("li").each(function() {
                        values.push($(this).text().substring(2).toLowerCase());
                    });

                    if (values.indexOf(value) == -1 && values.length != 0) {
                        visible = false;
                        break;
                    }
                }
            }

        }

        if (!visible) {
            row.style.display = "none";
        } else {
            row.style.display = "table-row";
        }

    }

    function searchSelection(select, column) {
        var parent = $(select).prev("div").find("ul");

        if (select.selectedIndex != 0) {
            var selectedOption = select.options[select.selectedIndex];

            // add select to list of allowed options
            var li = document.createElement("li");
            li.innerHTML = ' ' + selectedOption.text;

            var a = document.createElement("a");
            a.innerHTML = 'X';
            a.href = '#';
            a.onclick = function() {
                $(select).append(selectedOption);
                $(li).remove();
                select.selectedIndex = 0;
                searchSelection(select, column);
            };
            li.insertBefore(a, li.firstChild);
            parent.append(li);

            // remove from select list
            $(selectedOption).remove();

        }

        // go through rows to see which should become visible
        var values = new Array();
        parent.find("li").each(function() {
            values.push($(this).text().substring(2).toLowerCase());
        });

        var rows = $("#results>tbody>tr");

        rows
        .each(function() {
            if (!$(this).hasClass("rowDetails")) {
                var value = this.getElementsByTagName("td")[column].innerHTML
                .toLowerCase();

                if (values.indexOf(value) == -1 && values.length != 0) {
                    // search value not found -> hide row
                    this.style.display = "none";
                    var details = $(this).next("tr");
                    if (details.hasClass("rowDetails")
                        && (details.css("display") == 'table-row' || !details
                    .css("display"))) {
                        reverse = !reverse;
                        details.css("display", "none");
                        this.childNodes[1].rowSpan = 1;
                    }
                } else if (this.style.display == "none") {
                    // search value found and row used to be hidden
                    // see if there are other search values that need to be matched before showing row
                    checkAllColumns(this);
                }
            }
        });
    }

    function searchWithInterval(input, column, after) {
        var searchValue = "";
        if (input.value != "" && input.value != null) {
            searchValue = new Date(input.value);
        }

        var rows = $("#results>tbody>tr").not(".rowDetails");

        rows.each(function () {
            inInterval(this, column, after, searchValue, false);
        });

    }

    function inInterval(row, column, after, searchValue, stop) {
        var visible;
        var value = new Date(row.getElementsByTagName("td")[column].innerHTML
        .toLowerCase());

        if (((after && value < searchValue) || (!after && value > searchValue))
            && searchValue != "") {
            row.style.display = "none";
            visible = false;
        } else if (row.style.display == "none" && !stop) {
            checkAllColumns(row);
            return true;
        } else if (stop) {
            visible = true;
        }
        return visible;
    }
    
    var statusList = [];
    
    
<c:forEach items="${statusList}" var="status">
        statusList.push(${status.code}, {
           name: "${status.naam}",
           code: ${status.code},
           commentRequired: ${status.commentRequired}
        });
</c:forEach>

    var reverse = false;

    function startenOpdracht(opdrachtId, list) {
        changeStatus(opdrachtId, 2, list);
    }
    
    function afwerkenOpdracht(opdrachtId, list) {
        changeStatus(opdrachtId, 3, list);
    }
    
    function weigerenOpdracht(opdrachtId, list) {
        changeStatus(opdrachtId, 5, list);
    }
    
    function dataTekortOpdracht(opdrachtId, list) {
        changeStatus(opdrachtId, 4, list);
    }

    $(document).ready(
    function() {
        $.tablesorter.addParser({
            id : 'statusParser',
            is : function(s) {
                return false;
            },
            format : function(s) {
                // add type here, if possible (and also if order stored in db)
                var lower = s.toLowerCase();
                var i = 1;
                for (i=1; i<statusList.length; i+=2) {
                    var status = statusList[i];
                    lower = lower.replace(new RegExp(status.name.toLowerCase()), status.code);
                }
                return lower;
            },
            type : 'numeric'
        });
        $("#results").tablesorter({
            cssChildRow : 'rowDetails',
            headers : {
                6 : {
                    sorter : 'statusParser'
                },
                1 : {
                    sorter : 'shortDate'
                }
            }
        });

        $("#results>tbody>tr").not(".rowDetails").toggle(function() {
            if (!reverse) {
                if($(this).next(".rowDetails").size() == 1) {
                    $(this).next(".rowDetails").css("display", "table-row");
                    $(this).find("td:first-child").attr("rowSpan", 2);
                }
            } else {
                if($(this).next(".rowDetails").size() == 1) {
                    $(this).next(".rowDetails").css("display", "none");
                    $(this).find("td:first-child").attr("rowSpan", 1);
                }
            }
        }, function() {
            if (reverse) {
                if($(this).next(".rowDetails").size() == 1) {
                    $(this).next(".rowDetails").css("display", "table-row");
                    $(this).find("td:first-child").attr("rowSpan", 2);
                }
            } else {
                if($(this).next(".rowDetails").size() == 1) {
                    $(this).next(".rowDetails").css("display", "none");
                    $(this).find("td:first-child").attr("rowSpan", 1);
                }
            }
        });

        var comments = $("#comments");
        var allFields = $([]).add(comments);
        var tips = $(".validateTips");


        function updateTips( t ) {
            tips
            .text( t )
            .addClass( "ui-state-highlight" );
            setTimeout(function() {
                tips.removeClass( "ui-state-highlight", 1500 );
            }, 500 );
        }

        function checkLength( o, n, min, max ) {
            if ( o.val().length > max || o.val().length < min ) {
                o.addClass( "ui-state-error" );
                updateTips( "Length of " + n + " must be between " +
                    min + " and " + max + "." );
                return false;
            } else {
                return true;
            }
        }

        $("#formUpdateOpdracht").dialog({
            autoOpen: false,
            height: 300,
            width: 350,
            modal: true,
            buttons: {
                "Opslaan": function() {
                    var valid = true;
                    var opdrachtId = $(this).dialog("option", "opdrachtId");
                    var newStatus = $(this).dialog("option", "newStatus");
                    var oldStatus = $(this).dialog("option", "oldStatus");
                    allFields.removeClass("ui-state-error");

                    if (newStatus.commentRequired) {
                        valid = checkLength(comments, "comments", 3, 50);
                    }

                    var form = document.getElementById("updateOpdrachtForm");
                    if (valid) {
                        if (supportFormData()) {
                            var formData = new FormData(form);
                            // TODO add status to data;
                           
                            xmlhttp.open("POST", "UpdateOpdrachtServlet");
                            xmlhttp.send(formData);
                        } else {
                            var dialog = $(this);
                            var list = dialog.dialog("option", "list");
                            $(form).submit(function() {
                                jQuery.ajax({
                                    data: $(this).serialize(),
                                    url: "UpdateOpdrachtServlet",
                                    type: "POST",
                                    error: function(a, b, c) {
                                        // TODO show message to user
                                        alert("error: " + b + " - " + c)
                                    },
                                    success: function(response) {
                                        if (response == "S") {
                                            var row = $("#" + opdrachtId);
                                            row.removeClass("status" + oldStatus.code);
                                            row.addClass("status" + newStatus.code);
                                            row.find("td:nth-child(7)").text(newStatus.name);
                                            // TODO and add comment to display
                                            list.setAttribute("status", newStatus.code);
                                            dialog.dialog("close");
                                        } else {
                                            // save not successful, show message to user;
                                        }
                                    }
                                });
                                return false;
                            });
                            $(form).submit();
                        }
                    }
                },
                "Annuleer": function() {
                    $(this).dialog("close");
                }
            },
            close: function() {
                allFields.val("").removeClass("ui-state-error");
            }
        });
    });

    function changeStatus(opdrachtId, newStatus, list) {

        document.getElementById("oldStatus").value = list.getAttribute("status");
        document.getElementById("newStatus").value = newStatus;
        document.getElementById("opdrachtId").value = opdrachtId;
        
        $("#formUpdateOpdracht").dialog("option", {
            opdrachtId: opdrachtId,
            newStatus: getStatus(newStatus),
            oldStatus: getStatus(list.getAttribute("status")),
            list: list
        });
        $("#formUpdateOpdracht").dialog("open");

    }
    
    function getStatus(statusCode) {
        var index = statusList.indexOf(Number(statusCode));
        if (index != -1) {
            return statusList[index+1];
        }
    }
</script>

<h1>Overzicht opdrachten</h1>
<div id="opdrachtBody">
    <table id="results" class="tablesorter">
        <thead>
            <tr>
                <th>ID</th>
                <th>Aanmaakdatum</th>
                <th>Opdrachtgever</th>
                <th>Type</th>
                <th>Bestand</th>
                <th>Aantal</th>
                <th>Status</th>
            </tr>
            <tr>
                <td><input type="search" placeholder="ID" list="ids"
                           onsearch="search(this, 0)" onblur="search(this, 0)" /> <!-- search id --></td>
                <td><input type="search" pattern="dd/MM/yyyy" placeholder="na ..."
                           onsearch="searchWithInterval(this, 1, true)" onblur="searchWithInterval(this, 1, true)" /><br /> <input
                           type="search" placeholder="voor ..."
                           onsearch="searchWithInterval(this, 1, false)" onblur="searchWithInterval(this, 1, false)" /></td>
                <td><input type="search" placeholder="Opdrachtgever"
                           onsearch="search(this, 2)" onblur="search(this, 2)" /></td>
                <td>
                    <div><ul></ul></div>
                    <select onchange="searchSelection(this, 3)" placeholder="Type">
                        <option></option>
                        <%
                                    OpdrachtTypeDB otdb = new OpdrachtTypeDB();
                                    List<OpdrachtType> types = otdb.getActieveOpdrachtTypes(gebruiker);
                                    request.setAttribute("types", types);
                        %>

                        <c:forEach items="${types}" var="type">
                            <option>${type.naam}</option>
                        </c:forEach>
                    </select></td>
                <td><input type="search" placeholder="Bestand"
                           onsearch="search(this, 4)" onblur="search(this, 4)" /> <!-- bestand contains --></td>
                <td></td>
                <td>
                    <div><ul></ul></div>
                    <select onchange="searchSelection(this, 6)">
                        <option></option>
                        <%
                                    List<String> statussen = OpdrachtStatus.getNamen();
                                    request.setAttribute("statussen", statussen);
                        %>
                        <c:forEach items="${statussen}" var="status">
                            <option>${status}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>

        </thead>
        <tbody>
            <%
                OpdrachtDB db = new OpdrachtDB();
                List<Opdracht> opdrachten = db.list(gebruiker);
                        request.setAttribute("opdrachten", opdrachten);
            %>
            <c:forEach items="${opdrachten}" var="opdracht">
                <jsp:include page="rowsOpdracht.jsp" flush="false">
                    <jsp:param name="opdracht" value="${opdracht.id}" />
                </jsp:include>        
            </c:forEach>
        </tbody>

    </table>
</div>

<div id="formUpdateOpdracht" title="Wijzigen Opdracht">
    <p class="validateTips">Alle velden zijn verplicht in te vullen.</p>

    <form id="updateOpdrachtForm">
        <fieldset>
            <input type="hidden" id="opdrachtId" name="opdrachtId" value="" />
            <input type="hidden" id="action" name="action" value="updateStatus" />
            <input type="hidden" id="newStatus" name="newStatus" value="" />
            <input type="hidden" id="oldStatus" name="oldStatus" value="" />
            <label for="comments">Commentaar</label>
            <textarea rows="10" cols="40" name="comments" id="comments"></textarea>
        </fieldset>
    </form>
</div>

