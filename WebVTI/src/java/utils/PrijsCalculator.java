/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import domain.Prijs;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Soezen
 */
public class PrijsCalculator {

    /**
     * Calculate prijs with a formule. Formule cannot contain DEFAULT reference.
     *
     * @param formule
     * @return
     */
    public static BigDecimal calculatePrijs(String formule) {
        return calculatePrijs(null, formule);
    }

    private static BigDecimal evaluateSimpleFormule(String formule, BigDecimal defaultPrijs) {
        System.out.println("simple formule: " + formule);
        System.out.println("default: " + defaultPrijs);
        while (formule.startsWith("(") && formule.endsWith(")")) {
            formule = formule.substring(1, formule.length() - 1);
        }

        if (defaultPrijs != null) {
            formule = formule.replaceAll("DEFAULT", defaultPrijs.toString());
        }

        return BigDecimal.valueOf(Double.valueOf(formule));
    }

    public static BigDecimal calculatePrijs(Prijs defaultPrijs, String formule) {
        System.out.println("formule: " + formule);
        while (formule.startsWith("(") && formule.endsWith(")")) {
            formule = formule.substring(1, formule.length() - 1);
        }

        BigDecimal prijs = BigDecimal.ZERO;

        int indexDivide = formule.indexOf('/');
        int indexMultiply = formule.indexOf('*');
        int indexAdd = formule.indexOf('+');
        int indexSubtract = formule.indexOf('-');

        if (indexDivide == -1 && indexMultiply == -1 && indexAdd == -1 && indexSubtract == -1) {
            prijs = evaluateSimpleFormule(formule, (defaultPrijs != null) ? defaultPrijs.getBedrag() : null);
        } else {
            int minIndex = getMinimumIndex(indexDivide, indexMultiply, indexAdd, indexSubtract);
            BigDecimal first = evaluateSimpleFormule(formule.substring(0, minIndex), (defaultPrijs != null) ? defaultPrijs.getBedrag() : null);
            String operator = formule.substring(minIndex, minIndex + 1);
            String restFormule = formule.substring(minIndex + 1);
            if ("+".equals(operator)) {
                prijs = first.add(calculatePrijs(defaultPrijs, restFormule));
            } else if ("-".equals(operator)) {
                prijs = first.subtract(calculatePrijs(defaultPrijs, restFormule));
            } else if ("*".equals(operator)) {
                prijs = first.multiply(calculatePrijs(defaultPrijs, restFormule));
            } else if ("/".equals(operator)) {
                prijs = first.divide(calculatePrijs(defaultPrijs, restFormule), RoundingMode.HALF_UP);
            } else {
                throw new IllegalArgumentException("Illegal formule: " + formule);
            }
        }

        return prijs;
    }

    /**
     * Get the minimum of valid indexes. Ignoring -1 values.
     *
     * @param indexes
     * @return minimum of all indexes, ignoring -1 values. Returns -1 if all
     * values are -1.
     */
    private static int getMinimumIndex(int... indexes) {
        int minIndex = -1;

        for (int index : indexes) {
            if (minIndex == -1) {
                minIndex = index;
            } else if (index != -1) {
                minIndex = Math.min(minIndex, index);
            }
        }

        return minIndex;
    }
}
