/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package domain.interfaces;

import java.io.Serializable;

/**
 * klasse die door een gebruiker kan gevolgd worden
 * @author soezen
 */
public interface Trackable extends Serializable {
    public Long getId();
    public void setId(Long id);
}
