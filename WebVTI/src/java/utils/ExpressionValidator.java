/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import domain.Optie;
import java.util.List;

/**
 *
 * @author Soezen
 */
public class ExpressionValidator {

    public static boolean validate(String expressie, List<Optie> opties) {
        return validateCompositeExpression(expressie, opties);
    }

    private static boolean validateCompositeExpression(String expressie, List<Optie> opties) {
        System.out.println("> " + expressie);
        while (expressie.startsWith("(") && expressie.endsWith(")")) {
            expressie = expressie.substring(1, expressie.length() - 1);
        }
        boolean valid = false;
        int indexAnd = expressie.indexOf("AND");
        int indexOr = expressie.indexOf("OR");

        if (indexOr == -1 && indexAnd == -1) {
            valid = validateSingleExpression(expressie, opties);
        } else if (indexOr == -1 && indexAnd != -1) {
            valid = splitExpression(expressie, true, indexAnd, opties);
        } else if (indexOr != -1 && indexAnd == -1) {
            valid = splitExpression(expressie, false, indexOr, opties);
        } else if (indexOr < indexAnd) {
            valid = splitExpression(expressie, false, indexOr, opties);
        } else if (indexOr > indexAnd) {
            valid = splitExpression(expressie, true, indexAnd, opties);
        }

        return valid;
    }

    private static boolean splitExpression(String expressie, boolean and, int index, List<Optie> opties) {
        boolean valid = false;
        String first = expressie.substring(0, index).trim();
        String second = expressie.substring(index + (and ? 3 : 2)).trim();
        int countOpenFirst = first.length() - first.replace("(", "").length();
        int countClosedFirst = first.length() - first.replace(")", "").length();
        if (countOpenFirst == countClosedFirst) {
            valid = validateCompositeExpression(first, opties) || validateCompositeExpression(second, opties);
        } else if (countOpenFirst > countClosedFirst) {
            String firstSub = first.substring(first.lastIndexOf("(") + 1);
            String secondSub = second.substring(0, second.indexOf(")"));
            boolean subValid;
            if (and) {
                subValid = validateSingleExpression(firstSub, opties) && validateSingleExpression(secondSub, opties);
            } else {
                subValid = validateSingleExpression(firstSub, opties) || validateSingleExpression(secondSub, opties);
            }
            String completeExp = first.substring(0, first.lastIndexOf("(")) + subValid + second.substring(second.indexOf(")") + 1);
            valid = validateCompositeExpression(completeExp, opties);
        } else {
            throw new IllegalArgumentException("Illegal (sub)expression: " + expressie);
        }
        return valid;
    }

    private static boolean validateSingleExpression(String expressie, List<Optie> opties) {
        System.out.println(">> " + expressie);
        while (expressie.startsWith("(") && expressie.endsWith(")")) {
            expressie = expressie.substring(1, expressie.length() - 1);
        }
        boolean valid = false;
        boolean not = expressie.startsWith("NOT");

        int countOpen = expressie.length() - expressie.replace("(", "").length();
        int countClosedFirst = expressie.length() - expressie.replace(")", "").length();
        if (countOpen == countClosedFirst) {
            if (not) {
                valid = validateSingleExpression(expressie.substring(3).trim(), opties);
            } else if (expressie.startsWith("O-")) {
                // check if optie is in list of opties
                long optieId = Long.valueOf(expressie.substring(2).trim());
                for (Optie optie : opties) {
                    if (optie.getId().equals(optieId)) {
                        valid = true;
                        break;
                    } 
                }
            } else if (expressie.startsWith("OT-")) {
                // check if any selected optie is of this optietype
                long optieTypeId = Long.valueOf(expressie.substring(3).trim());
                for (Optie optie : opties) {
                    if (optie.getOptieType().getId().equals(optieTypeId)) {
                        valid = true;
                        break;
                    }
                }
            } else if ("true".equals(expressie)) {
                valid = true;
            } else if ("false".equals(expressie)) {
                valid = false;
            } else if ("".equals(expressie)) {
                valid = true;
            } else if (expressie.startsWith("O.")) {
                expressie = expressie.substring(2);
                if (expressie.startsWith("SIZE = ")) {
                    int expSize = Integer.valueOf(expressie.substring(7));
                    valid = (expSize == opties.size());
                }
            } else {
                throw new IllegalArgumentException("Illegal variable in expression: " + expressie);
            }
        } else {
            throw new IllegalArgumentException("Illegal single expression: " + expressie);
        }

        return not ? !valid : valid;
    }
}
