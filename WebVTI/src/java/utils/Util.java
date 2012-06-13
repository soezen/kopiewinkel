/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import database.ConstraintDB;
import database.OpdrachtDB;
import database.OptieDB;
import domain.Optie;
import domain.constraints.ConnectionConstraint;
import domain.constraints.Constraint;
import domain.interfaces.Constrainer;
import java.util.ArrayList;
import java.util.Iterator;
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
     * TODO fix name
     * @param constrainer
     * @return true if the constrainer should be hidden
     */
    public static boolean isVisibleOrEnabled(Constrainer constrainer) {
        // get all constraints where param is the constrainer of
        // and of which the constrained is of type optie
        // and which is not wederkerig
        ConstraintDB db = new ConstraintDB();
        OptieDB odb = new OptieDB();
        
        List<Long> optieIds = new ArrayList<Long>();
        
        for (Optie o : odb.list()) {
            optieIds.add(o.getId());
        }
        
        // TODO move to service layer
        List<ConnectionConstraint> conns = (List<ConnectionConstraint>) db.list(ConnectionConstraint.class, 
                "e.constrainer = :constrainer and e.wederkerig = :wederkerig and e.constrained member of :optieIds", new Object[][] {
           new Object[] { "constrainer", constrainer.getId() },
           new Object[] { "wederkerig", false },
           new Object[] { "optieIds", optieIds }
        });
        
        return !conns.isEmpty();
    }

    public static String getConstraintsAsJSObject() {
        StringBuilder sbRequired = new StringBuilder("[");
        StringBuilder sbForbidden = new StringBuilder("[");

        List<Constraint> constraints = db.getConstraintsRequiredAndForbids();
        if (constraints != null) {
            for (Constraint constraint : constraints) {
                // TODO fix
//                System.out.println(constraint.getType() + ": " + constraint.getConstrainer().getId() + " - " + constraint.getConstrained().getId());
//                switch (constraint.getType()) {
//                    case VERPLICHT:
//                        sbRequired.append("[")
//                                .append(constraint.getConstrainer().getId())
//                                .append(",")
//                                .append(constraint.getConstrained().getId())
//                                .append("],");
//                        break;
//                    case VERBIEDT:
//                        sbForbidden.append("[")
//                                .append(constraint.getConstrainer().getId())
//                                .append(",")
//                                .append(constraint.getConstrained().getId())
//                                .append("],");
//                        break;
//                }
            }
        }
        
        String requiredList = sbRequired.toString();
        if (requiredList.endsWith(",")) {
            requiredList = requiredList.substring(0, requiredList.length() - 1);
        }
        String forbiddenList = sbForbidden.toString();
        if (forbiddenList.endsWith(",")) {
            forbiddenList = forbiddenList.substring(0, forbiddenList.length() - 1);
        }

        return "new Object({required:" + requiredList + "],forbidden:" + forbiddenList + "]})";
    }
}
