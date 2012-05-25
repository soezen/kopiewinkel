<%-- 
    Document   : index
    Created on : 10-feb-2012, 22:03:35
    Author     : soezen
--%>

<%@page import="database.DatabaseManager"%>
<%@page import="java.util.List"%>
<%@page import="domain.Gebruiker"%>
<%@page import="database.MenuItemDB"%>
<%@page import="domain.MenuItem"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Kopiewinkel</title>
        <script src="js/validation.js" type="text/javascript"></script>
        <script src="jquery/js/jquery-1.7.1.min.js" type="text/javascript"></script>
        <script src="jquery/js/jquery-ui-1.8.17.custom.min.js" type="text/javascript"></script>
        <script src="jquery/jquery.tablesorter.js"></script>
        <link media="all" type="text/css" href="jquery/css/cupertino/jquery-ui-1.8.17.custom.css" rel="stylesheet">
        <link type="text/css" href="css/main.css" rel="stylesheet" />
        <script type="text/javascript">
            function redirect(page, success) {
                var xmlhttp;
                if (window.XMLHttpRequest) {
                    // code for IE7+, Firefox, Chrome, Opera, Safari
                    xmlhttp=new XMLHttpRequest();
                } else {
                    // code for IE6, IE5
                    xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
                }
                xmlhttp.onreadystatechange=function() {
                    if (xmlhttp.readyState==4 && xmlhttp.status==200) {
                        success(xmlhttp);
                    }
                }
                xmlhttp.open("GET", page);
                xmlhttp.send();
            }

            $(function() {
                $("#menu").tabs({
                    ajaxOptions: {
                        error: function( xhr, status, index, anchor ) {
                            $( anchor.hash ).html("Couldn't load this tab. We'll try to fix this as soon as possible.");
                        }
                    }
                });
            });

            setForbidden = function(a, b, origins, callback) {
                origins.push(a);
                return setEnabled(document.getElementById(b), !a.checked, origins, callback);
            }

            setRequired = function(a, b, origins, callback) {
                b = document.getElementById(b);
                if (a.checked && isOptie(b.id)) {
                    origins.push(a);
                    return setSelected(b, true, origins, callback);
                } else if (isOptieType(b.id) && a.checked) {
                    callback2 = callback;
                    otInput = getOptieTypeInput(b.id);
                    if (otInput.min == 0) {
                        otInput.min = 1;
                        checkNumberInput(otInput);
                        callback2 = function() {
                            b.min = 0;
                            checkNumberInput(otInput);
                            callback();
                        }
                    }
                    b.parentNode.style.display = "block";
                    origins.push(a);
                    return setEnabled(b, true, origins, function() {
                        b.parentNode.style.display = "none";
                        callback2();
                    });
                } else if (isOptieType(b.id) && !a.checked) {
                    otInput = getOptieTypeInput(b.id);
                    oldMin = otInput.min;
                    otInput.min = 0;
                    checkNumberInput(otInput);
                    return function() {
                        otInput.min = oldMin;
                        checkNumberInput(otInput);
                        callback();
                    };
                } 
                return callback;
            }

            setIsRequiredBy = function(a, b, origins, callback) {
                b = document.getElementById(b);
                origins.push(a);
                if (isOptieType(b.id)) {
                    return setVisible(b, a.checked, origins, callback);
                } else if (isOptie(a.id)) {
                    return setEnabled(b, a.checked, origins, callback);
                }

                return callback;
            }

            save = function() {
                console.log('save');
                if (validate()) {
                    console.log('validated');
                    var xmlhttp;
                    if (window.XMLHttpRequest) {
                        // code for IE7+, Firefox, Chrome, Opera, Safari
                        xmlhttp = new XMLHttpRequest();
                    } else {
                        //  code for IE6, IE5
                        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
                    }
                    xmlhttp.onreadystatechange = function() {
                        if (xmlhttp.readyState==4 && xmlhttp.status==200) {
                            console.log('save reply');
                            var response = xmlhttp.responseText;
                            if (response.indexOf("<li>") == 0
                                && response.lastIndexOf("</li>") == response.length - 5) {
                                var errors = document.getElementById("errors");
                                errors.innerHTML = response;
                                errors.style.display = "block";
                            } else {
                                redirect(response, function(inXmlHttp) {
                                    var body = document.getElementById("opdrachtBody");
                                    body.innerHTML = inXmlHttp.responseText;
                                    $("#opdrachtBody").on('click', "#next", getOpdrachtItems);
                                    $("#opdrachtBody").off('click', "#next", save);
                                    $("#previous").attr('disabled', '');
                                    $("#opdrachtBody").off('click', '#previous', backToOpdrachtType);
                                });
                            } 
                        }
                    }
                    var formData = new FormData(document.getElementById("opdrachtForm"));
                    
                    xmlhttp.open("POST", "InputOpdrachtServlet");
                    xmlhttp.send(formData);
                    // TODO show busy sign, so user knows reply is still coming.
                } 
            }

            function getOptieTypeInput(otId) {
                if (isOptieType(otId)) {
                    return document.getElementById(otId + "Aantal");
                } else if(isOptie(otId)) {
                    return document.getElementById(document.getElementById(otId).parentNode.id + "Aantal");
                } else {
                    return document.getElementById(otId.parentNode.id + "Aantal");
                }
            }

            var functions = [];

            function updateAantalSelected(input, selected) {
                if (selected) {
                    input.value = eval(input.value) + 1;
                } else {
                    input.value = eval(input.value) - 1;
                }

                checkNumberInput(input);
            }

            function checkNumberInput(input) {
                header = $(input.parentNode.parentNode).children("h3");
                if ((input.value > input.max && input.max != -1) || input.value < input.min) {
                    header.addClass("error");
                } else {
                    header.removeClass("error");
                }
            }

            function setVisible(a, visible, origins, callback) {
                var parentStyle = a.parentNode.style;
                if (parentStyle.display == "block" && !visible) {
                    parentStyle.display = "none";
                    callback2 = function() {
                        parentStyle.display = "block";
                        callback();
                    };
                    return setEnabled(a, false, origins, callback2);
                } else if (parentStyle.display == "none" && visible) {
                    parentStyle.display = "block";
                    callback2 = function() {
                        parentStyle.display = "none";
                        callback();
                    };
                    return setEnabled(a, true, origins, callback2);
                }
            }

            function setEnabled(b, enabled, origins, callback) {
                inOrigins = origins.indexOf(b.id) != -1;
                if (!isOptieType(b.id)) {
                    if ((!enabled && b.checked && inOrigins)
                        || (inOrigins && b.disabled != !enabled)) {
                        callback();
                        throw "illegal state";
                    } else if (!enabled && b.checked && !inOrigins) {
                        callback2 = setSelected(b, false, origins, callback);
                        b.disabled = !enabled;
                        return function() {
                            b.disabled = enabled;
                            callback2();
                        };
                    } else if ((b.disabled != !enabled) && !inOrigins) {
                        b.disabled = !enabled;
                        return function() {
                            b.disabled = enabled;
                            callback();
                        };
                    }
                } else {
                    children = b.getElementsByTagName("input");
                    origins.push(b);
                    hasErrors = false;
                    callback2 = callback;
                    for (var i=0; i<children.length; i++) {
                        callback2 = setEnabled(children[i], enabled, origins, callback2);
                        
                    }
                    return callback2;
                }
                return callback;
            }

            function setSelected(a, selected, origins, callback) {
                inOrigins = origins.indexOf(a.id) != -1;
                if (a.checked != selected) {
                    if (selected == null) {
                        selected = a.checked;
                    }
                    if (!inOrigins && !a.disabled) {
                        // checking max of optie type
                        var otInput = getOptieTypeInput(a);
                        newValue = otInput.value;
                        if(selected) {
                            newValue++;
                        } else {
                            newValue--;
                        }
                        done = false;
                        if (otInput.max >= newValue || otInput.max == -1) {
                            a.checked = selected;
                            updateAantalSelected(otInput, selected);
                            done = true;
                        } else if(otInput.max == 1 && selected) {
                            // deselecting other first
                            newOrigins = origins;
                            newOrigins.push(a);
                            other = getFirstSelectedOptie(otInput.id, a);
                            callback = setSelected(other, false, newOrigins, callback);
                            a.checked = true;
                            updateAantalSelected(otInput, true);
                            done = true;
                        }
                        if (done) {
                            callback2 = function() {
                                a.checked = !selected;
                                updateAantalSelected(otInput, !selected);
                                callback();
                            };
                        } else {
                            callback2 = callback;
                        }

                        for(var j=0; j<functions.length; j++) {
                            reverse = false;
                            if (j==1) {
                                reverse = true;
                            }
                            var dependents = getValues(functions[j][0], a, reverse);
                            for(var k=0; k<dependents.length; k++) {
                                callback2 = functions[j][1](a, dependents[k], origins, callback2);
                            }
                        }
                        return callback2;
                    } else {
                        callback();
                    }
                }
                return callback;
            }

            function getFirstSelectedOptie(otId, not) {
                otId = otId.replace("Aantal", "");
                ot = document.getElementById(otId);
                children = ot.getElementsByTagName("input");
                for(var i=0; i<children.length; i++) {
                    if (children[i].type == "checkbox") {
                        if (children[i].checked && children[i].id != not.id) {
                            return children[i];
                        }
                    }
                }
                return null;
            }

            function getValues(list, a, reverse) {
                result = [];
                key = 0;
                value = 1;
                if (reverse) {
                    key = 1;
                    value = 0;
                }
                for (var i=0; i<list.length; i++) {
                    if (list[i][key] == a.id) {
                        result.push(list[i][value]);
                    }
                }
                return result;
            }

            function isOptie(id) {
                return (id >= 5000000 && id < 6000000);
            }

            function isOptieType(id) {
                return (id >= 4000000 && id < 5000000);
            }

            function updateAantal(check, inAantal) {
                txtAantal = document.getElementById("aantal");
                txtAantal = $(txtAantal).find(":only-child");
                txtTotaal = document.getElementById("totalKlassen");
                aantal = eval(txtAantal.val());
                totaal = eval(txtTotaal.value);
                if (check.checked) {
                    aantal += eval(inAantal);
                    totaal++;

                } else {
                    aantal -= eval(inAantal);
                    totaal--;
                }
                if (aantal < 1) {
                    aantal = 1;
                }
                txtTotaal.value = totaal;
                txtAantal.val(aantal);
            }
            
            function toggleToewijzingen(id) {
                div = $("#klassen");
                link = div.prev();
                div = div.children().children();
                if ("toon selectie" == link.text()) {
                    txtTotaal = document.getElementById("totalKlassen");
                    txtAantal = $("#aantal").find(":only-child");
                    aantal = eval(txtAantal.val());
                    totaal = eval(txtTotaal.value);
                    div.children(":checkbox").not(".toewijzing").map(function() {
                        $(this).css("display", "none");
                        $(this).next().css("display", "none");
                        if (this.checked) {
                            totaal--;
                            this.checked = false;
                            aantal -= eval($(this).prev().val());
                        }
                    });
                    link.text("toon alles");
                    txtTotaal.value = totaal;
                    txtAantal.val(aantal);
                } else {
                    div.children().each(function() {
                        if (this.type == "checkbox") {
                            $(this).css("display", "inline");
                            $(this).next().css("display", "inline");
                        }
                    });
                    link.text("toon selectie");
                }
            }
        </script>
        <%          Gebruiker gebruiker = (Gebruiker) session.getAttribute("gebruiker");
                    if (gebruiker == null) {
                        gebruiker = DatabaseManager.getGastGebruiker();
                        session.setAttribute("gebruiker", gebruiker);
                    }
        %>
    </head>
    <body>
        <div id="menu">
            <ul>
                <%
                            MenuItemDB dbMenu = new MenuItemDB();
                            List<MenuItem> items = dbMenu.getMenuItems(gebruiker);
                            if (items != null) {
                                for (MenuItem item : items) {
                                    out.println("<li><a href='jsp/" + item.getLink() + "'>" + item.getNaam() + "</a></li>");
                                }
                            }
                %>
            </ul>
        </div>
    </body>
</html>
