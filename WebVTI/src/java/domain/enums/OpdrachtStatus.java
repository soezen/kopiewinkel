package domain.enums;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author soezen
 */
public enum OpdrachtStatus {

    AANGEVRAAGD(1, "Aangevraagd", false),
    IN_BEHANDELING(2, "In behandeling", false),
    AFGEWERKT(3, "Afgewerkt", false),
    DATA_TEKORT(4, "Data tekort", true),
    GEWEIGERD(5, "Geweigerd", true);
    private int code;
    private String naam;
    private boolean commentRequired;

    private OpdrachtStatus(int code, String naam, boolean commentRequired) {
        this.naam = naam;
        this.code = code;
        this.commentRequired = commentRequired;
    }

    public boolean isCommentRequired() {
        return commentRequired;
    }

    public int getCode() {
        return code;
    }

    public String getNaam() {
        return naam;
    }

    public boolean nextAllowed(OpdrachtStatus newStatus) {
        return (this == AANGEVRAAGD && (newStatus == DATA_TEKORT || newStatus == GEWEIGERD || newStatus == IN_BEHANDELING))
                || (this == DATA_TEKORT && (newStatus == IN_BEHANDELING || newStatus == GEWEIGERD))
                || (this == IN_BEHANDELING && (newStatus == GEWEIGERD || newStatus == DATA_TEKORT || newStatus == AFGEWERKT));
    }

    public static OpdrachtStatus valueOf(int code) {
        switch (code) {
            case 1:
                return AANGEVRAAGD;
            case 2:
                return IN_BEHANDELING;
            case 3:
                return AFGEWERKT;
            case 4:
                return DATA_TEKORT;
            case 5:
                return GEWEIGERD;
            default:
                throw new IllegalArgumentException("Invalid OpdrachtStatus code: " + code);
        }
    }

    public static List<String> getNamen() {
        List<String> namen = new ArrayList<String>();

        for (OpdrachtStatus status : values()) {
            namen.add(status.getNaam());
        }

        return namen;
    }
}
