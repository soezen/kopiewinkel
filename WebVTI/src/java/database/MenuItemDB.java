/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.MenuItem;

/**
 *
 * @author soezen
 */
public class MenuItemDB extends PriviligedEntityDB<MenuItem> {

    public MenuItemDB() {
        clazz = MenuItem.class;
        type = DatabaseManager.EM_MENU_ITEM;
    }

    
}
