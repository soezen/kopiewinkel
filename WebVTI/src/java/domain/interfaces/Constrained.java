/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package domain.interfaces;

import java.io.Serializable;

/**
 * Klasse waar een gebruiker gelimiteerd of gerechtigd kan zijn
 * @author soezen
 */
public interface Constrained extends Serializable {
    public void setId(Long id);
    public Long getId();
}
