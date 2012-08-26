package utils;

import domain.Optie;
import domain.OptieType;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Soezen
 */
@RunWith(Parameterized.class)
public class ExpressionValidatorTest {

    private String expressie;
    private boolean valid;
    private List<Optie> opties;

    public ExpressionValidatorTest(String expressie, boolean valid) {
        this.expressie = expressie;
        this.valid = valid;
        opties = new ArrayList<Optie>();
        opties.add(createOptie(1));
        opties.add(createOptie(3));
    }
    
    private static Optie createOptie(long id) {
        Optie optie = new Optie();
        optie.setId(id);
        optie.setOptieType(createOptieType(id));
        return optie;
    }
    
    private static OptieType createOptieType(long id) {
        OptieType optieType = new OptieType();
        optieType.setId(id);
        return optieType;
    }

    @Parameterized.Parameters
    public static List<Object[]> getExpressions() {
        List<Object[]> parameters = new ArrayList<Object[]>();
        parameters.add(createNewTestCase("O-1", true));
        parameters.add(createNewTestCase("O-2", false));
        parameters.add(createNewTestCase("OT-1", true));
        parameters.add(createNewTestCase("OT-2", false));
        parameters.add(createNewTestCase("NOT O-2", true));
        parameters.add(createNewTestCase("NOT OT-2", true));
        parameters.add(createNewTestCase("(O-1)", true));
        parameters.add(createNewTestCase("(OT-1)", true));
        parameters.add(createNewTestCase("(NOT O-2)", true));
        parameters.add(createNewTestCase("(NOT OT-2)", true));
        parameters.add(createNewTestCase("NOT (O-2)", true));
        parameters.add(createNewTestCase("NOT (OT-2)", true));
        parameters.add(createNewTestCase("O-1 AND O-3", true));
        parameters.add(createNewTestCase("NOT O-2 AND O-3", true));
        parameters.add(createNewTestCase("(O-1) AND NOT O-2", true));
        parameters.add(createNewTestCase("NOT (O-4) AND (NOT O-2)", true));
        parameters.add(createNewTestCase("(((((O-1)))))", true));
        parameters.add(createNewTestCase("(((((O-1))))", false));
        parameters.add(createNewTestCase("NOT (O-4 AND O-3)", true));
        parameters.add(createNewTestCase("O-2 OR O-1", true));
        parameters.add(createNewTestCase("O-2 OR O-4", false));
        return parameters;
    }

    private static Object[] createNewTestCase(String expressie, boolean result) {
        return new Object[]{expressie, result};
    }

    @Test
    public void validate() {
        try {
            boolean actual = ExpressionValidator.validate(expressie, opties);
            Assert.assertEquals(valid, actual);
        } catch (IllegalArgumentException e) {
            if (valid) {
                e.printStackTrace();
            }
            Assert.assertFalse(valid);
        }
    }
}
