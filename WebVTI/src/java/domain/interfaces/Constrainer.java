/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package domain.interfaces;

import java.io.Serializable;

/**
 * Interface for classes that can constrain other classes
 * 
 * @author soezen
 */
public interface Constrainer extends Serializable {
    public Long getId();
    public void setId(Long id);
}
