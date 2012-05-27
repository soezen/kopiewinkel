package domain.enums;

/**
 * Type prijs.
 * @author soezen
 */
public enum PrijsType {
    /**
     * Prijs is van toepassing op een optie.
     */
    OPTIE,
    /**
     * Prijs is van toepassing indien 1 of meerdere opties van een optie type geselecteerd zijn.
     */
    OPTIETYPE,
    /**
     * Prijs is van toepassing op een opdracht.
     */
    OPDRACHT,
    /**
     * Prijs wordt berekend met een formule.
     */
    FORMULE
}
