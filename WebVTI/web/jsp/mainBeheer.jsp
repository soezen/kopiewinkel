<%-- 
    Document   : manageOpdrachten
    Created on : 26-mrt-2012, 21:10:27
    Author     : soezen
--%>

<%@page import="domain.OpdrachtType"%>
<%@page import="domain.enums.OpdrachtStatus"%>
<%@page import="database.ConnectionManager"%>
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
                gebruiker = ConnectionManager.getGastGebruiker();
            }
%>

<link rel="stylesheet" type="text/css"
      href="css/blue/style.css" />
<style type="text/css">
    .hidden {
        display: none;
    }

    .statusGeweigerd>td {
        background-color: wheat !important;
    }

    .statusDataTekort>td {
        background-color: #EE5757 !important;
    }

    .statusInBehandeling>td {
        background-color: #FECA40 !important;
    }

    .statusAfgewerkt > td {
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

    function startenOpdracht() {
        
    }

    var reverse = false;

    $(document).ready(
    function() {
        $.tablesorter.addParser({
            id : 'statusParser',
            is : function(s) {
                return false;
            },
            format : function(s) {
                // add type here, if possible (and also if order stored in db)
                return s.toLowerCase().replace(/geweigerd/, 4).replace(
                /afgewerkt/, 5).replace(/aangevraagd/, 2)
                .replace(/in behandeling/, 1).replace(/data tekort/, 3);
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
                    allFields.removeClass("ui-state-error");

                    valid = checkLength(comments, "comments", 3, 50);

                    if (valid) {
                        // try to change status
                        // TODO replace with ajax
                        var newRow = "<td rowspan='2'>468</td><td>01-01-2012</td><td>Eline Rogge</td><td>Examens</td><td>bestand.om.te.kopieren.doc</td>"
                            + "<td>24</td><td>Afgewerkt</td>";
                        var newRowDetails = "<td colspan='7'><div class='details'><div>Acties:<ul>"
                            + "<li><a href='#'>Wijzigen</a></li></ul></div><div><i>Uitgevoerd op 20-05-2012 door Monique Lefebvre</i><br /> <br />"
                            + "<strong>Commentaar:</strong> ................................<br /><br /> <strong>Extra gegevens:</strong> <br />"
                            + "<table><tr><td>Veldnaam</td><td>Waarde</td></tr><tr><td>Veldnaam</td><td>Waarde</td></tr></table></div><div>"
                            + "<strong>Opties:</strong><table><tr><td>optie type</td><td>optie</td></tr><tr><td>optie type</td><td><ul><li>optie</li>"
                            + "<li>optie</li></ul></td></tr><tr><td>optie type</td><td>optie</td></tr></table></div></div></td>";

                        // if succeeded replace rows with new rows
                        // TODO put in success function
                        // redirect(jsp/rowDetails.jsp?opdrachtId=??, function() { getNewRow from responseText, getNewRowDetails from responseText, nextLine })
                        $("#" + opdrachtId).html(newRow).next(".rowDetails").html(newRowDetails);
                        $(this).dialog("close");
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

    function changeStatus(opdrachtId, newStatus) {

        // first show form to enter comments (only in certain situations)
        // in certain cases, comments are mandatory.
        // only show form in case of mandatory comments or other fields
        // other cases: add button to add comments

        $("#formUpdateOpdracht").dialog("option", {
            opdrachtId: opdrachtId,
            newStatus: newStatus
        });
        $("#formUpdateOpdracht").dialog("open");

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
                                    OpdrachtDB db = new OpdrachtDB();
                                    List<OpdrachtType> types = db.getActieveOpdrachtTypes(gebruiker);
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
                        List<Opdracht> opdrachten = db.getOpdrachten(gebruiker);
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

    <form>
        <fieldset>
            <label for="comments">Commentaar</label>
            <textarea rows="10" cols="40" id="comments"></textarea>
        </fieldset>
    </form>
</div>

