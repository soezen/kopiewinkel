/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package domain.interfaces;

import java.io.Serializable;

/**
 * Class that can be constrained by a Constrainer
 * @author soezen
 */
public interface Constrainable extends Serializable {
    public void setId(Long id);
    public Long getId();
}
