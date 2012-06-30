/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import domain.enums.InputVeldType;

/**
 * Exception for inputfields with invalid content.
 * 
 * @author soezen
 * @since 0.0.1
 */
public class InvalidContentException extends Exception {

    private InputVeldType type;
    private String fieldName;
    private String input;
    
    public InvalidContentException(InputVeldType inputVeldType, String fieldName, String input) {
        type = inputVeldType;
        this.input = input;
        this.fieldName = fieldName;
    }

    
    public InputVeldType getType() {
        return type;
    }
    
    public String getInput() {
        return input;
    }

    public String getFieldName() {
        return fieldName;
    }
    
    @Override
    public String getMessage() {
        return "De inhoud van veld '" + fieldName + "' is ongeldig.";
    }
    
    
    
}
