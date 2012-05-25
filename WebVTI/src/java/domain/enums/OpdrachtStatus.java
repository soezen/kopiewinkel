package domain.enums;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author soezen
 */
public enum OpdrachtStatus {
    AANGEVRAAGD("Aangevraagd"),
    IN_BEHANDELING("In behandeling"),
    AFGEWERKT("Afgewerkt"),
    DATA_TEKORT("Data te kort om opdracht uit te voeren"),
    GEWEIGERD("Geweigerd");

    private String naam;

    private OpdrachtStatus(String naam) {
        this.naam = naam;
    }

    public String getNaam() {
        return naam;
    }

    public static List<String> getNamen() {
        List<String> namen = new ArrayList<String>();

        for (OpdrachtStatus status : values()) {
            namen.add(status.getNaam());
        }

        return namen;
    }
}
