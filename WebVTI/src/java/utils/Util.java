/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.google.appengine.api.datastore.Key;
import database.DatabaseManager;
import database.OpdrachtDB;
import domain.Constraint;
import domain.interfaces.Constrained;
import domain.interfaces.Constrainer;
import java.util.List;

/**
 *
 * @author soezen
 */
public class Util {

    private static OpdrachtDB db = new OpdrachtDB();

   
    /**
     * check to see if an optieType or optie requires an optie to be selected
     * 
     * @param constrainer
     * @return
     */
    public static boolean isVisibleOrEnabled(Constrainer constrainer) {
        boolean visible = true;
        String criteria = "constrainer = " + constrainer.getId() + " and constrained >= 5000000 and constrained < 6000000";
            criteria += " and not wederkerig";
// TODO fix this
            //        List<Constraint> constraints = DatabaseManager.list(Constraint.class, "Constraints", criteria, null);
//        if (constraints == null) {
//            visible = false;
//        } else if (constraints.isEmpty()) {
//            visible = false;
//        }
        return visible;
    }

    public static String getConstraintsAsJSObject() {
        StringBuilder sbRequired = new StringBuilder("[");
        StringBuilder sbForbidden = new StringBuilder("[");

        List<Constraint> constraints = db.getConstraintsRequiredAndForbids();
        if (constraints != null) {
            for (Constraint constraint : constraints) {
                System.out.println(constraint.getType() + ": " + constraint.getConstrainer().getId() + " - " + constraint.getConstrained().getId());
                switch (constraint.getType()) {
                    case VERPLICHT:
                        sbRequired.append("[")
                                .append(constraint.getConstrainer().getId())
                                .append(",")
                                .append(constraint.getConstrained().getId())
                                .append("],");
                        break;
                    case VERBIEDT:
                        sbForbidden.append("[")
                                .append(constraint.getConstrainer().getId())
                                .append(",")
                                .append(constraint.getConstrained().getId())
                                .append("],");
                        break;
                }
            }
        }

        String requiredList = sbRequired.toString();
        if (requiredList.endsWith(",")) {
            requiredList = requiredList.substring(0, requiredList.length()-1);
        }
        String forbiddenList = sbForbidden.toString();
        if (forbiddenList.endsWith(",")) {
            forbiddenList = forbiddenList.substring(0, forbiddenList.length()-1);
        }

        return "new Object({required:" + requiredList + "],forbidden:" + forbiddenList + "]})";
    }
}
