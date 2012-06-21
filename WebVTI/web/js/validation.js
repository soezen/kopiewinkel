/**
 * checks for all the input elements whether the correct values are given:
 *  - required fields cannot be empty
 *  - number fields must be a valid number and in between interval (if borders specified)
 *  - forbidden fields cannot have a value
 *  - email fields must match email pattern
 *  
 *  if errors are present, messages are added to the div with id 'errors'
 */
function validate() {
    emptyErrors();
    var form = document.getElementById("opdrachtForm");
    var fields = form.getElementsByTagName("input");
    var errors = false;
    for (var i=0; i<fields.length; i++) {
        var field = fields[i];
        var errorOnField = false;
        var type = field.getAttribute("type");

        if (type == "text" || type == "number" || type == "file" || type == "email") {
            // TODO check to see if this does not cause problems in chrome
            if (field.required || field.getAttribute("required") == "") {
                errorOnField = !checkRequiredField(field, true);
            } else if (field.disabled) {
                errorOnField = !checkForbiddenField(field, true);
            } 

            if (!errorOnField) {
                errors = !checkLimits(field, true) || errors;
                errors = !checkEmailField(field, true) || errors;
            } else {
                errors = true;
            }
        }
    }
    return !errors;
}

/**
 * Check if a required field has a value. Returns falls if not.
 * 
 * @param field Field of which you want to check whether it is has a correct value
 * @param report Boolean whether or not you want to add an error description to the log
 * @returns {Boolean} true if value is not empty or null
 */
function checkRequiredField(field, report) {
    var text = field.value;
    var emptyOrNull = false;
    if (text == null) {
        emptyOrNull = true;
    } else if (text.trim() == "") {
        emptyOrNull = true;
    }

    if (report && emptyOrNull) {
        reportError(field, "required");
    }
	
    return !emptyOrNull;
}

/**
 * Controleert indien een getal binnen een interval ligt, grenzen inbegrepen.
 * Retourneert false indien de waarde geen getal of indien de waarde buiten de grenzen
 * ligt.
 * 
 * @param field
 * @param report
 * @returns {Boolean}
 */
function checkLimits(field, report) {
    var underMin = false;
    var overMax = false;
    var notNumber = false;
    var fieldValue = Number(field.value.trim());
    if (isNaN(fieldValue) && field.getAttribute("type") == "number") {
        notNumber = true;
		
        if (report) {
            reportError(field, "number");
        }
    }
	
    var min = field.min;
    var max = field.max;
    if (!min) {
        min = field.getAttribute("min");
    }
    if (!max) {
        max = field.getAttribute("max");
    }
    	
    if ((min || max) && !notNumber) {
		
        if (min) {
            if (fieldValue < min) {
                underMin = true;
            }
        }
		
        if (max && max != -1) {
            if (fieldValue > max) {
                overMax = true;
            }
        }
		
        if (report && (underMin || overMax)) {
            var attributes = new Object({
                min: min,
                max: max
            });
            if (isOptieType(field.id)) {
                attributes.optieType = true;
            }
            reportError(field, "limits", attributes);
        }
    }
	
    return !(notNumber || underMin || overMax);
}

/**
 * Controleert indien een veld geen waarde heeft.
 * Retourneert false indien dit wel het geval is.
 * @param field
 * @param report
 * @returns
 */
function checkForbiddenField(field, report) {
    var hasValue = checkRequiredField(field, false);
	
    if (hasValue && report) {
        reportError(field, "forbidden", new Object({
            field: field.id
        }));
    }
	
    // get fields that forbid this field
    // -> method isForbiddenBy: idem isRequiredBy
    // see which one is selected
    // give option to deselect this option so that field is no longer forbidden
	
    return !hasValue;
}

/**
 * Add an error message to the ul element with id 'errors'
 * This message is clickable and will focus the input element that caused the error.
 * 
 * @param field
 * @param type
 * @param attributes
 */
function reportError(field, type, attributes) {
    var log = document.getElementById("errors");
    var error = document.createElement("li");
    error.id = "error" + field.id;
    var label = getLabel(field);
    if (!label) {
        label = $(field).prev().text();
    } else {
        label = label.innerHTML
    }
    
    var klassen = false;
    if ("Klassen" == label) {
        klassen = true;
        attributes.klassen = true;
    }        
    
    if (type != "forbidden") {
        var link = document.createElement("a");
        link.href = "#";
        link.onclick = function() {
            field.select();
            field.focus();
        };
        if (klassen) {
            label = "";
        }
        link.innerHTML = label + getErrorMessage(type, attributes);
        error.appendChild(link);
    } else {
        error.innerHTML = label + getErrorMessage(type, attributes);
    }
    log.appendChild(error);
    log.parentNode.style.display = "block";
    // TODO hide log when no errors

    field.onkeydown = function() {
        removeError(field);
    };
}

/**
* Remove the error message that was caused by the field.
* 
* @param field
*/
function removeError(field) {
    error = document.getElementById("error" + field.id);
    if (error != null) {
        error.parentNode.removeChild(error);
    }
    
    // TODO if last error, hide error block
}

/**
* Get the error message of an error type. Some error messages make use of the 
* attributes parameter:
* 
*  - type 'limits' uses attributes: min and/or max
*  - type 'number' uses attribute: field (id of the number field)
*  - type 'format' uses attribute: message
* 
* @param type
* @param attributes
* @returns {String}
*/
function getErrorMessage(type, attributes) {
    if (type == "required") {
        return " moet nog ingevuld worden";
    } else if (type == "limits") {
        if (!attributes.optieType) {
            if (attributes.min && attributes.max) {
                return " moet een waarde hebben tussen " + attributes.min + " en " + attributes.max;
            } else if (attributes.min) {
                if (!attributes.klassen) {
                    return " moet een waarde hebben groter dan " + attributes.min;
                } else {
                    return "Er moet minimum " + attributes.min + " klas(sen) geselecteerd zijn";
                }
            } else if (attributes.max) {
                return " moet een waarde hebben kleiner dan " + attributes.max;
            }
        } else {
            if (attributes.min && attributes.max) {
                if (attributes.min == attributes.max && attributes.min != 1) {
                    return " moet " + attributes.min + " opties geselecteerd hebben";
                } else if (attributes.min == attributes.max) {
                    return " moet 1 optie geselecteerd hebben";
                } else if (attributes.min != 1) {
                    return " moet minimum " + attributes.min + " opties geselecteerd hebben en maximum " + attributes.max;
                } else {
                    return " moet minimum 1 optie geselecteerd hebben en maximum " + attributes.max;
                }
            } else if (attributes.min && attributes.min == 1) {
                return " moet minimum 1 optie geselecteerd hebben";
            } else if (attributes.min) {
                return " moet minimum " + attributes.min + " opties geselecteerd hebben";
            } else if (attributes.max && attributes.max == 1) {
                return " mag maximum maar 1 optie geselecteerd hebben";
            } else if (attributes.max) {
                return " mag maximum maar " + attributes.max + " opties geselecteerd hebben";
            }
        }
    } else if (type == "number") {
        return " moet een getal zijn";
    } else if (type == "forbidden") {
        return " mag niet ingevuld zijn; <a href='#' onclick='emptyField(\"" + attributes.field + "\")'>verwijder waarde</a>";
    } else if (type == "format") {
        return " heeft geen geldig formaat " + attributes.message;
    }
}

/**
* Empty the value of a field.
* 
* @param field
*/
function emptyField(field) {
    field = document.getElementById(field);
    field.value = "";
    removeError(field);
}

/**
* Find the corresponding label to an input field.
* 
* @param input
* @returns
*/
function getLabel(input) {
    var labels = document.getElementsByTagName("label");
    for (var i=0; i<labels.length; i++) {
        if (labels[i].htmlFor == input.id) {
            return labels[i];
        }
    }
}

/**
* Remove all errors from the ul with id 'errors'
*/
function emptyErrors() {
    var log = document.getElementById("errors");
    log.innerHTML = "";
}

/**
* Check if the value of a field matches the email pattern.
* 
* @param field
* @param report
* @returns {Boolean}
*/
function checkEmailField(field, report) {
    var errors = false;
    var email = field.value.trim();
    var message = "<ul>";
	
    if (field.getAttribute("type") == "email" && email != "" && email != null) {
        if (email.indexOf("@") == -1) {
            errors = true;
            message += "<li>Email must contain @ symbol</li>";
        } else {
            var before = email.substring(0, email.indexOf("@"));
            var after = email.substring(email.indexOf("@")+1);
			
            // second @ sign always in after part since we split on first @ sign
            if (after.indexOf("@") != -1) {
                errors = true;
                message += "<li>Email can only contain one @ sign</li>";
            }
			
            // checks on part before @
            if (before.indexOf(".") == 0) {
                errors = true;
                message += "<li>Email cannot start with .</li>";
            }
            var beforeParts = before.split(".");
            if (beforeParts.length == 1 && beforeParts[0].length == 0) {
                errors = true;
                message += "<li>Missing local part; Email cannot start with @</li>";
            } else if (before.lastIndexOf(".") == before.length-1) {
                errors = true;
                message += "<li>Local part of email (before @) cannot end with .</li>";
            } else {
                var localCorrect = true;
                beforeParts.forEach(function(part) {
                    if (part.length == 0) {
                        localCorrect = false;
                    }
                });
				
                if (!localCorrect) {
                    errors = true;
                    message += "<li>Local part (before @) cannot have two dots in a row</li>";
                }
            }
			
			
			
            // checks on part after @
            var afterParts = after.split(".");
            if (afterParts.length <= 1) {
                errors = true;
                message += "<li>Domain part of email (after @) must contain at least one .</li>";
            } else {
                var tldCorrect = true;
                afterParts.forEach(function(part) {
                    if (part.length == 0 || part.length > 63) {
                        tldCorrect = false;
                    }
                });
				
                if (afterParts[afterParts.length-1].length == 0) {
                    errors = true;
                    message += "<li>Email cannot end with .</li>";
                } else if (afterParts[afterParts.length-1].length < 2) {
                    errors = true;
                    message += "<li>Domain part (after @) must end with an extension of at least 2 characters</li>";
                }
                if (afterParts[0].length == 0) {
                    errors = true;
                    message += "<li>Domain part (after @) cannot start with .";
                } else if (!tldCorrect) {
                    errors = true;
                    message += "<li>Domain part (after @) cannot have two dots in a row</li>";
                }
            }
            // check for illegal characters
            var domainPattern = new RegExp("^(([a-zA-Z]|[a-zA-Z][a-zA-Z0-9\-]*[a-zA-Z0-9])\.)*([a-zA-Z]|[a-zA-Z][a-zA-Z0-9\-]*[a-zA-Z0-9])$");
            if (!domainPattern.test(after) && !errors) {
                errors = true;
                message += "<li>Domain part of email (after @) can only contain digits, alphabetic characters, - or .</li>";
            }
        }

		
    }
	
    if (report && errors) {
        message += "</ul>";
        reportError(field, "format", new Object({
            message: message
        }));
    }
	
    return !errors;
}
