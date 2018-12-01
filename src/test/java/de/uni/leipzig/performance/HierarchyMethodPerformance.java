package de.uni.leipzig.performance;

import java.util.Arrays;
import java.util.function.Supplier;

import org.junit.*;
import org.junit.runners.MethodSorters;

import de.uni.leipzig.method.TreeCreation;
import de.uni.leipzig.method.TreeCreation.Method;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HierarchyMethodPerformance {

    @Test
    public void testHierarchy_100() throws Exception {
        Supplier<TreeCreation> thinnessClass = () -> Method.THINNESS_CLASS.get()
                .inNonInteractiveMode(true)
                .inPrintMode(false);

        TwoColoredRunner twoColoredRunner = new TwoColoredRunner(HierarchyMethodPerformance.class,
                Arrays.asList(100), 1);
        twoColoredRunner.run(thinnessClass);
    }

    @Test
    public void testHierarchy_500() throws Exception {
        Supplier<TreeCreation> thinnessClass = () -> Method.THINNESS_CLASS.get()
                .inNonInteractiveMode(true)
                .inPrintMode(false);

        TwoColoredRunner twoColoredRunner = new TwoColoredRunner(HierarchyMethodPerformance.class,
                Arrays.asList(500), 1);
        twoColoredRunner.run(thinnessClass);
    }

    @Test
    public void testHierarchy_1000() throws Exception {
        Supplier<TreeCreation> thinnessClass = () -> Method.THINNESS_CLASS.get()
                .inNonInteractiveMode(true)
                .inPrintMode(false);

        TwoColoredRunner twoColoredRunner = new TwoColoredRunner(HierarchyMethodPerformance.class,
                Arrays.asList(1000), 1);
        twoColoredRunner.run(thinnessClass);
    }
}
