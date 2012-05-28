/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import domain.*;
import domain.constraints.Constraint;
import domain.enums.ConstraintType;
import domain.enums.OpdrachtStatus;
import java.util.ArrayList;
import java.util.List;
import utils.GlobalValues;

/**
 *
 * @author soezen
 */
public class OpdrachtDB extends PriviligedEntityDB<Opdracht> {

    public OpdrachtDB() {
        clazz = Opdracht.class;
        type = DatabaseManager.EM_OPDRACHT;
    }
    
    public List<Doelgroep> getDoelgroepenHuidigSchooljaar() {
        String criteria = "id in (select doelgroep from doelgroepleerlingen where schooljaar = " + GlobalValues.HUIDIG_SCHOOLJAAR + ")";
//        return DatabaseManager.list(Doelgroep.class, "Doelgroepen", criteria, "graad, naam");
        return null;
    }

    public List<Gebruiker> getOpdrachtGevers(OpdrachtType opdrachtType) {
        List<Gebruiker> gebruikers = new ArrayList<Gebruiker>();
//
//        if (opdrachtType != null) {
//            try {
//                Session ssn = DatabaseManager.getSession();
//                ssn.beginTransaction().begin();
//                String sub = "(select gebruiker from rechten where constrained = " + opdrachtType.getId() + ")";
//                SQLQuery sql = ssn.createSQLQuery("select g.* from gebruikers g join gebruikertypes gt on g.gebruikertype = gt.id "
//                        + "where (gt.standaard and g.id not in " + sub + " and gt.id not in " + sub + ") "
//                        + "or (not gt.standaard and (g.id in " + sub + " or (gt.id in " + sub + ")))");
//                sql.addEntity(Gebruiker.class);
//                gebruikers = sql.list();
//            } catch (HibernateException e) {
//                e.printStackTrace();
//            }
//        }

        return gebruikers;
    }

    public List<OpdrachtTypeInput> getInputVelden(OpdrachtType opdrachtType, Gebruiker gebruiker) {
        List<OpdrachtTypeInput> velden = new ArrayList<OpdrachtTypeInput>();
        if (opdrachtType != null && gebruiker != null) {
            String criteria = "OpdrachtType = " + opdrachtType.getId() + " and zichtbaar";
            String order = "volgorde";
//            List<OpdrachtTypeInput> zichtbaarVelden = DatabaseManager.list(OpdrachtTypeInput.class, "OpdrachtTypeInput", criteria, order);

            criteria = "OpdrachtType = " + opdrachtType.getId();
   //         List<OpdrachtTypeInput> rechtVelden = DatabaseManager.listWithRights(gebruiker, OpdrachtTypeInput.class, "OpdrachtTypeInput", "InputVeld", criteria, order);

            boolean done = false;
            int i = 0;
            int j = 0;
            OpdrachtTypeInput recht = null;
            OpdrachtTypeInput zicht = null;
            while (!done) {
//                if (i < rechtVelden.size() && recht == null) {
//                    recht = rechtVelden.get(i);
//                    i++;
//                }
//                if (j < zichtbaarVelden.size() && zicht == null) {
//                    zicht = zichtbaarVelden.get(j);
//                    j++;
//                }

                if (zicht != null && recht != null) {
                    if (zicht.getVolgorde() > recht.getVolgorde()) {
                        velden.add(recht);
                        recht = null;
                    } else if (zicht.getVolgorde() < recht.getVolgorde()) {
                        velden.add(zicht);
                        zicht = null;
                    } else {
                        velden.add(zicht);
                        zicht = null;
                        recht = null;
                    }
                } else if (zicht != null) {
                    velden.add(zicht);
                    zicht = null;
                } else if (recht != null) {
                    velden.add(recht);
                    recht = null;
                } else {
                    done = true;
                }
            }
        }

        return velden;
    }

    /**
     * Get list of opties or optietypes (constrainables) who are constrained by
     * <code>constrainer</code>.
     *
     * @param constrainer
     * @return
     */
    public List<Constraint> getConstraintsRequiredAndForbids() {
        String criteria = "type in ('" + ConstraintType.VERPLICHT + "', '" + ConstraintType.VERBIEDT + "')";
//        return DatabaseManager.list(Constraint.class, "Constraints", criteria, null);
        return null;
    }

//    // TODO mapping opdracht incomplete: missing doelgroepen en opties
//    public Opdracht save(Opdracht opdracht) {
////        Opdracht o = (Opdracht) DatabaseManager.save(opdracht);
//        Opdracht o = (Opdracht) DatabaseManager.persist(opdracht);
//
//        if (o != null) {
//            // is still necessary?
//            for (InputWaarde waarde : o.getInputWaarden()) {
////                DatabaseManager.save(waarde);
//                persist(waarde);
//            }
//        }
//
//        return o;
//    }


    public List<Opdracht> getAfTeWerkenOpdrachten(Gebruiker gebruiker) {
        String criteria = "status in ('" + OpdrachtStatus.AANGEVRAAGD + "', '" + OpdrachtStatus.IN_BEHANDELING + "')";
//        return DatabaseManager.listWithRights(gebruiker, Opdracht.class, "opdrachten", "id", criteria, "AanmaakDatum");
        return null;
    }

    public List<OpdrachtType> getActieveOpdrachtTypes(Gebruiker gebruiker) {
        String criteria = "van <= NOW() and (tot is null or tot >= NOW())";
  //      return DatabaseManager.listWithRights(gebruiker, OpdrachtType.class, "opdrachttypes", "id", criteria, null);
        return null;
    }
}
