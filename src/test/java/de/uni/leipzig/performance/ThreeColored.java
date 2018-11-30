package de.uni.leipzig.performance;

import java.util.Arrays;
import java.util.function.Supplier;

import org.junit.*;
import org.junit.runners.MethodSorters;

import de.uni.leipzig.method.TreeCreation;
import de.uni.leipzig.method.TreeCreation.Method;
import de.uni.leipzig.ncolored.NColored.SuperTreeMethod;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ThreeColored {

    private static final int LEAF_NUMBER = 500;

    static Integer[] COLORS = new Integer[] {
            3, 5, 10, 20
    };

    Supplier<TreeCreation> lrtMethod = () -> Method.THINNESS_CLASS.get()
            .inNonInteractiveMode(true)
            .inPrintMode(false);

    ThreeColoredRunner threeColoredRunner = new ThreeColoredRunner(ThreeColored.class,
            Arrays.asList(COLORS), LEAF_NUMBER, lrtMethod);

    @Test
    public void testAho() throws Exception {
        threeColoredRunner.run(SuperTreeMethod.AHO);
    }

    @Test
    public void testDfb() throws Exception {
        // FIXME always failing ?
        threeColoredRunner.run(SuperTreeMethod.DENG_FERNANDEZ_BACA);
    }

}
