/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.google.appengine.api.datastore.Key;
import database.*;
import domain.*;
import domain.constraints.Constraint;
import domain.enums.ConstraintType;
import domain.enums.InputVeldType;
import domain.interfaces.Constrainable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author soezen
 */
public class Validator {

    public static HashMap<String, String> getErrors(Opdracht opdracht) {
        InputVeldDB ivdb = new InputVeldDB();
        GebruikerDB gdb = new GebruikerDB();
        OpdrachtTypeDB otdb = new OpdrachtTypeDB();
        HashMap<String, String> errors = new HashMap<String, String>();

        if (opdracht == null) {
            errors.put("opdracht", "Geen opdracht opgegeven");
            return errors;
        }

        // rechten gebruiker op opdrachtgever
        if (isAllowed(gdb.get(opdracht.getOpdrachtgever()), otdb.get(opdracht.getOpdrachtType()).getKey())) {
            // validate all input values
            for (OpdrachtTypeInput input : otdb.get(opdracht.getOpdrachtType()).getInputVelden()) {
                InputVeld veld = ivdb.get(input.getInputVeld());

                if (veld.getType() == InputVeldType.VAST && "Klassen".equals(veld.getNaam())) {
                    // klassen zijn verplicht
                    if (input.isVerplicht()) {
                        if (opdracht.getDoelgroepen() == null) {
                            errors.put("Klassen", "Er moet minstens één klas geselecteerd zijn");
                        } else if (opdracht.getDoelgroepen().isEmpty()) {
                            errors.put("Klassen", "Er moet minstens één klas geselecteerd zijn");
                        }
                    }
                } else if (veld.getType() != InputVeldType.VAST) {
                    // TODO
               //     InputWaarde waarde = opdracht.getInputWaardeFor(veld);
                    InputWaarde waarde = null;
                    if (input.isVerplicht()) {
                        if (waarde == null) {
                            errors.put(veld.getNaam(), veld.getNaam() + " is verplicht in te vullen");
                        } else if (waarde.getWaarde() == null) {
                            errors.put(veld.getNaam(), veld.getNaam() + " is verplicht in te vullen");
                        } else if ("".equals(waarde.getWaarde())) {
                            errors.put(veld.getNaam(), veld.getNaam() + " is verplicht in te vullen");
                        } else {
                            checkType(errors, veld, waarde);
                        }
                    } else if (waarde != null) {
                        checkType(errors, veld, waarde);
                    }
                }
            }

            checkStaticFields(errors, opdracht);
            checkSelectedOpties(errors, opdracht);

        } else {
            errors.put("opdrachtType", "Opdrachtgever heeft niet voldoende rechten om een opdracht van dit type aan te maken");
        }

        return errors;
    }

    public static void checkSelectedOpties(HashMap<String, String> errors, Opdracht opdracht) {
        OpdrachtDB db = new OpdrachtDB();
        GebruikerDB gdb = new GebruikerDB();
        OptieDB odb = new OptieDB();
        List<Constraint> constraints = db.getConstraintsRequiredAndForbids();
        if (opdracht.getOpties() != null) {
            for (Key key : opdracht.getOpties()) {
                Optie optie = odb.get(key);
                if (!isAllowed(gdb.get(opdracht.getOpdrachtgever()), optie.getKey())) {
                    errors.put(optie.getNaam(), "Opdrachtgever heeft niet genoeg rechten om optie " + optie.getNaam() + " te selecteren");
                } else if (!isAllowed(gdb.get(opdracht.getOpdrachtgever()), optie.getOptieType().getKey())) {
                    errors.put(optie.getNaam(), "Opdrachtgever heeft niet genoeg rechten om opties van het type " + optie.getOptieType().getNaam() + " te selecteren");
                } else {
                    checkConstraints(constraints, optie, opdracht, errors);
                }
            }
        }
    }

    public static void checkConstraints(List<Constraint> constraints, Optie optie, Opdracht opdracht, HashMap<String, String> errors) {
        EnumMap<ConstraintType, List<Constrainable>> constrainables = new EnumMap<ConstraintType, List<Constrainable>>(ConstraintType.class);

        // TODO refactor
//        for (Constraint constraint : constraints) {
//            if (constraint.getConstrainer().equals(optie)) {
//                List<Constrainable> l = constrainables.get(constraint.getType());
//                if (l == null) {
//                    l = new ArrayList<Constrainable>();
//                    constrainables.put(constraint.getType(), l);
//                }
//                l.add(constraint.getConstrained());
//            }
//        }

//        for (Entry<ConstraintType, List<Constrainable>> entry : constrainables.entrySet()) {
//            switch (entry.getKey()) {
//                case VERPLICHT:
//                    for (Constrainable con : entry.getValue()) {
//                        if (!contains(opdracht.getOpties(), con)) {
//                            errors.put(optie.getNaam(), "Niet alle benodigde opties voor optie " + optie.getNaam() + " zijn geselecteerd");
//                        }
//                    }
//                    break;
//                case VERBIEDT:
//                    for (Constrainable con: entry.getValue()) {
//                        if (contains(opdracht.getOpties(), con)) {
//                            errors.put(optie.getNaam(), "Bepaalde opties zijn geselecteerd die niet in combinatie mogen met optie " + optie.getNaam());
//                        }
//                    }
//                    break;
//            }
//        }

    }

    public static boolean contains(List<Optie> opties, Constrainable constrainable) {
        boolean contains = false;

        if (constrainable instanceof Optie) {
            contains = opties.contains(constrainable);
        } else if (constrainable instanceof OptieType) {
            OptieType type = (OptieType) constrainable;
            for (Optie o:type.getOpties()) {
                contains = contains || opties.contains(o);
            }
        }

        return contains;
    }

    public static void checkStaticFields(HashMap<String, String> errors, Opdracht opdracht) {
        if (opdracht.getOpdrachtgever() == null) {
            errors.put("Opdrachtgever", "Opdrachtgever moet ingevuld zijn");
        }

        if (opdracht.getAanmaakDatum() == null) {
            errors.put("AanmaakDatum", "AanmaakDatum moet ingevuld zijn");
        }

        if (opdracht.getOpdrachtType() == null) {
            errors.put("OpdrachtType", "OpdrachtType moet ingevuld zijn");
        }

        if (opdracht.getBestand() == null) {
            errors.put("Bestand", "Bestand moet ingevuld zijn");
        } else if (opdracht.getBestand().length() > 200 || opdracht.getBestand().length() <= 0) {
            errors.put("Bestand", "Bestand moet ingevuld zijn en mag niet langer dan 200 karakters");
        }

        if (opdracht.getAantal() < 0) {
            errors.put("Aantal", "Aantal moet groter zijn dan nul");
        }

        if (opdracht.getStatus() == null) {
            errors.put("Status", "Status moet een geldige waarde hebben");
        }
    }

    public static void checkType(HashMap<String, String> errors, InputVeld veld, InputWaarde waarde) {
        // check limits, check type
        switch (veld.getType()) {
            case DATUM:
                if (!isDatum(waarde.getWaarde())) {
                    errors.put(veld.getNaam(), veld.getNaam() + " moet een geldige datum zijn (DD-MM-YYYY)");
                }
                break;
            case EMAIL:
                if (!isEmail(waarde.getWaarde())) {
                    errors.put(veld.getNaam(), veld.getNaam() + " moet een geldig emailadres zijn");
                }
                break;
            case GETAL:
                if (!isGetal(waarde.getWaarde())) {
                    errors.put(veld.getNaam(), veld.getNaam() + " moet een geldig getal zijn");
                } else {
                    if (!isWithinLimits(waarde)) {
                        errors.put(veld.getNaam(), veld.getNaam() + " ");
                    }
                }
                break;
            case TEKST:
                if (!isWithinLimits(waarde)) {
                    errors.put(veld.getNaam(), veld.getNaam() + " heeft een ongeldige lengte");
                }
                break;
            default:
                throw new IllegalArgumentException("ongeldig InputVeldType: " + veld.getType());
        }
    }

    public static boolean isEmail(String email) {
        boolean correct = false;

        if (email.contains("@")) {
            try {
                InternetAddress.parse(email, true);
                if (!email.contains("..")) {
                    correct = true;
                }
            } catch (AddressException e) {
                correct = false;
            }
        } else {
            correct = false;
        }

        return correct;
    }

    public static boolean isGetal(String waarde) {
        boolean correct = false;

        try {
            Integer.parseInt(waarde);
            correct = true;
        } catch (NumberFormatException e) {
            correct = false;
        }

        return correct;
    }

    public static boolean isWithinLimits(InputWaarde waarde) {
        boolean correct = false;
        InputVeldDB ivdb = new InputVeldDB();
        InputVeld veld = ivdb.get(waarde.getInputVeld());
        int max = veld.getMax();
        int min = veld.getMin();

        if (veld.getType() == InputVeldType.GETAL) {
            try {
                int intWaarde = Integer.parseInt(waarde.getWaarde());
                if ((intWaarde >= min || intWaarde <= max)
                        && max != -1) {
                    correct = true;
                } else if (max == -1) {
                    if (intWaarde >= min) {
                        correct = true;
                    }
                }
            } catch (NumberFormatException e) {
                correct = false;
            }
        } else if (veld.getType() == InputVeldType.TEKST) {
            int lengte = waarde.getWaarde().length();
            if (lengte >= min && lengte <= max) {
                correct = true;
            } else if (max == -1) {
                if (lengte >= min) {
                    correct = true;
                }
            }
        }
        return correct;
    }

    public static boolean isDatum(String waarde) {
        boolean isDatum = false;

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        formatter.setLenient(false);

        try {
            formatter.parse(waarde);
            isDatum = true;
        } catch (ParseException e) {
            isDatum = false;
        }

        return isDatum;
    }

    public static boolean isAllowed(Gebruiker gebruiker, Key constrained) {
        boolean isAllowed = gebruiker.getGebruikerType().isStandaard()
                ^ (gebruiker.getRechten().contains(constrained)
                || gebruiker.getGebruikerType().getRechten().contains(constrained));
        // TODO
//        if (constrained instanceof Optie && !isAllowed) {
//            Optie optie = (Optie) constrained;
//            isAllowed = isAllowed(gebruiker, optie.getOptieType()) || isAllowed;
//        }
        return isAllowed;
    }
}
