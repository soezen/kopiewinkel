/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import database.ConstraintDB;
import database.OptieDB;
import domain.Optie;
import domain.constraints.ConnectionConstraint;
import domain.constraints.Constraint;
import domain.interfaces.Constrainer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author soezen
 */
public class Util {

    /**
     * check to see if an optieType or optie requires an optie to be selected
     *
     * TODO fix name
     *
     * @param constrainer
     * @return true if the constrainer should be hidden
     */
    public static boolean isVisibleOrEnabled(Constrainer constrainer) {
        // get all constraints where param is the constrainer of
        // and of which the constrained is of type optie
        // and which is not wederkerig
        ConstraintDB db = new ConstraintDB();
      
        // TODO move to service layer
        List<ConnectionConstraint> conns = (List<ConnectionConstraint>) db.list(ConnectionConstraint.class,
                "e.constrainer = :constrainer and e.wederkerig = :wederkerig and e.constrainedOptie = :isOptie", new Object[][]{
                    new Object[]{"constrainer", constrainer.getId()},
                    new Object[]{"wederkerig", false},
                    new Object[]{"isOptie", true}
                });

        return !conns.isEmpty();
    }

    public static String getConstraintsAsJSObject() {
        StringBuilder sbRequired = new StringBuilder("[");
        StringBuilder sbForbidden = new StringBuilder("[");
        ConstraintDB csdb = new ConstraintDB();

        List<ConnectionConstraint> constraints = (List<ConnectionConstraint>) csdb.list(ConnectionConstraint.class);
        for (ConnectionConstraint constraint : constraints) {
            if (constraint.isVerplicht()) {
                sbRequired.append("['")
                        .append((constraint.isConstrainerOptie()) ? "OP" : "OT")
                        .append(constraint.getConstrainer()).append("','")
                        .append((constraint.isConstrainedOptie()) ? "OP" : "OT")
                        .append(constraint.getConstrained()).append("'],");
                if (constraint.isWederkerig()) {
                    sbRequired.append("['")
                            .append((constraint.isConstrainedOptie()) ? "OP" : "OT")
                            .append(constraint.getConstrained()).append("','")
                            .append((constraint.isConstrainerOptie()) ? "OP" : "OT")
                            .append(constraint.getConstrainer()).append("'],");
                }
            } else {
                sbForbidden.append("['")
                        .append((constraint.isConstrainerOptie()) ? "OP" : "OT")
                        .append(constraint.getConstrainer()).append("','")
                        .append((constraint.isConstrainedOptie()) ? "OP" : "OT")
                        .append(constraint.getConstrained()).append("'],['")
                        .append((constraint.isConstrainedOptie()) ? "OP" : "OT")
                        .append(constraint.getConstrained()).append("','")
                        .append((constraint.isConstrainerOptie()) ? "OP" : "OT")
                        .append(constraint.getConstrainer()).append("'],");
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
