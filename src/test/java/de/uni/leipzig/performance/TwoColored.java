package de.uni.leipzig.performance;

import java.util.Arrays;

import org.junit.*;
import org.junit.runners.MethodSorters;

import de.uni.leipzig.method.TreeCreation;
import de.uni.leipzig.method.TreeCreation.Method;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TwoColored {

    static Integer[] LEAF_NUMBERS = new Integer[] {
            10, 20, 40, 80// ,160, 320, 640 , 1280, 2560
    };

    Runner runner = new Runner(TwoColored.class, Arrays.asList(LEAF_NUMBERS));

    @Test
    public void testHierarchy() throws Exception {
        TreeCreation thinnessClass = Method.THINNESS_CLASS.get()
                .inNonInteractiveMode(true)
                .inPrintMode(false);

        runner.run(thinnessClass);
    }

    @Test
    public void testInfAho() throws Exception {
        TreeCreation infAho = Method.AHO_INFORMATIVE.get()
                .inNonInteractiveMode(true)
                .inPrintMode(false);

        runner.run(infAho);
    }

}
