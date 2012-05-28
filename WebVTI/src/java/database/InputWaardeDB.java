/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.InputWaarde;

/**
 *
 * @author Soezen
 */
public class InputWaardeDB extends EntityDB<InputWaarde> {

    public InputWaardeDB() {
        clazz = InputWaarde.class;
        type = DatabaseManager.EM_INPUT_WAARDE;
    }
    
    
}
