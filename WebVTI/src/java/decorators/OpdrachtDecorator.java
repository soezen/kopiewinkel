/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package decorators;

import java.io.Serializable;

/**
 *
 * @author soezen
 */
public class OpdrachtDecorator implements Serializable {
    public Long opdrachtType;

    public Long getOpdrachtType() {
        return opdrachtType;
    }

    public void setOpdrachtType(Long opdrachtType) {
        this.opdrachtType = opdrachtType;
    }
    
}
