/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package domain.enums;

import java.io.Serializable;

/**
 *
 * @author soezen
 */
public enum InputVeldType implements Serializable {
    TEKST("text"),
    DATUM("date"),
    GETAL("number"),
    VAST("static"),
    EMAIL("email");

    private String naam;

    private InputVeldType(String naam) {
        this.naam = naam;
    }

    public String getNaam() {
        return naam;
    }
}
