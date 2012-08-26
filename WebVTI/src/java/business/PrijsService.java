/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import database.ConstraintDB;
import database.OptieDB;
import database.PrijsDB;
import database.PrijsFormuleDB;
import domain.Optie;
import domain.Prijs;
import domain.PrijsFormule;
import domain.constraints.FormuleConstraint;
import domain.constraints.OptiePrijsConstraint;
import domain.constraints.OptieTypePrijsConstraint;
import domain.constraints.PrijsConstraint;
import domain.enums.PrijsType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import utils.PrijsCalculator;

/**
 *
 * @author Soezen
 */
public class PrijsService {

    private PrijsDB prijsdb = new PrijsDB();
    private PrijsFormuleDB formuledb = new PrijsFormuleDB();
    private ConstraintDB constraintdb = new ConstraintDB();
    private OptieDB optiedb = new OptieDB();

    public BigDecimal calculatePrijs(List<Long> optieIds) {
        BigDecimal prijs = BigDecimal.ZERO;
        List<Optie> opties = optiedb.list(optieIds);

        // calculate base price of opdracht
        List<Prijs> opdrachtPrijzen = prijsdb.list("e.type = :type", new Object[][]{
                    new Object[]{"type", PrijsType.OPDRACHT},
                    new Object[]{"today", new Date()}
                });
        for (Prijs opdrachtPrijs : opdrachtPrijzen) {
            System.out.println("OpdrachtPrijs: " + opdrachtPrijs.getId() + " - " + opdrachtPrijs.getConditie());
            // TODO SUR check conditie prijs
            if (opdrachtPrijs.isApplicable(opties)) {
                // opdracht prijs cannot have formule
                prijs = prijs.add(opdrachtPrijs.getBedrag());
            }
        }

        // add all costs for selected opties
        List<OptiePrijsConstraint> optieConstraints = (List<OptiePrijsConstraint>) constraintdb.list(OptiePrijsConstraint.class,
                "e.constrainer member of :optieIds", new Object[][]{
                    new Object[]{ "optieIds", optieIds }
                });
        prijs = prijs.add(calculatePrijs(optieConstraints, opties));

        // add all costs for selected optietypes
        List<OptieTypePrijsConstraint> optieTypeConstraints = (List<OptieTypePrijsConstraint>) constraintdb.list(OptieTypePrijsConstraint.class,
                "e.constrainer member of :optieTypeIds", new Object[][] {
                    new Object[]{ "optieTypeIds", getOptieTypeIds(opties) }
                });
        prijs = prijs.add(calculatePrijs(optieTypeConstraints, opties));

        return prijs;
    }

    public BigDecimal calculatePrijs(List<? extends PrijsConstraint> constraints, List<Optie> opties) {
        BigDecimal prijs = BigDecimal.ZERO;
        for (PrijsConstraint opc : constraints) {
            Prijs optiePrijs = prijsdb.getWithId(opc.getPrijsId());
            if (optiePrijs.isApplicable(opties)) {
                if (!optiePrijs.hasFormule()) {
                    prijs = prijs.add(optiePrijs.getBedrag());
                } else {
                    List<FormuleConstraint> formuleConstraints = (List<FormuleConstraint>) constraintdb.list(FormuleConstraint.class, "e.constrainer = :prijsId", new Object[][]{
                                    new Object[]{"prijsId", optiePrijs.getId()}
                                });
                    if (formuleConstraints.isEmpty() || formuleConstraints.size() > 1) {
                        throw new IllegalStateException("DB has illegal state: more than one FormuleConstraint found for prijs: " + optiePrijs.getId());
                    }
                    PrijsFormule formule = formuledb.getWithId(formuleConstraints.get(0).getFormuleId());
                    prijs = prijs.add(PrijsCalculator.calculatePrijs(getDefaultPrijs(opc.getConstrainer(), constraints), formule.getFormule()));
                }
            }
        }
        return prijs;
    }

    public Prijs getDefaultPrijs(Long optieId, List<? extends PrijsConstraint> prijsConstraints) {
        Prijs defaultPrijs = null;

        for (PrijsConstraint pc : prijsConstraints) {
            if (pc.getConstrainer().equals(optieId) && pc.isStandaard()) {
                defaultPrijs = prijsdb.getWithId(pc.getPrijsId());
            }
        }

        return defaultPrijs;
    }

    private List<Long> getOptieTypeIds(List<Optie> opties) {
        List<Long> ids = new ArrayList<Long>();
        
        for (Optie optie : opties) {
            Long id = optie.getOptieType().getId();
            if (!ids.contains(id)) {
                ids.add(id);
            }
        }
        
        return ids;
    }
}
