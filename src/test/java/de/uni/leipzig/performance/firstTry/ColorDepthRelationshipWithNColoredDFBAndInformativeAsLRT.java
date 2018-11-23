package de.uni.leipzig.performance.firstTry;

import org.junit.*;
import org.junit.runners.MethodSorters;

import de.uni.leipzig.method.TreeCreation;
import de.uni.leipzig.method.TreeCreation.Method;
import de.uni.leipzig.ncolored.NColored.SuperTreeMethod;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
public class ColorDepthRelationshipWithNColoredDFBAndInformativeAsLRT {

    static int MAX_CHILDREN = 2;

    static int[] COLORS = new int[] { 2, 3, 10 };

    TreeCreation lrtMethod = Method.AHO_INFORMATIVE.get().inNonInteractiveMode(true);

    private Runner runner = new Runner(getClass(), MAX_CHILDREN, SuperTreeMethod.AHO);

    // TWO COLORED

    @Test
    public void test_Color2_Depth3til4() throws Exception {
        int color = 2;
        int depth = 4;

        runner.run(lrtMethod, color, depth);
    }

    @Test
    public void test_Color2_Depth8() throws Exception {
        int color = 2;
        int depth = 8;

        runner.run(lrtMethod, color, depth);
    }

    @Test
    public void test_Color2_Depth12() throws Exception {
        int color = 2;
        int depth = 12;

        runner.run(lrtMethod, color, depth);

    }

    // THREE COLORED

    @Test
    public void test_Color3_Depth4() throws Exception {
        int color = 3;
        int depth = 4;

        runner.run(lrtMethod, color, depth);
    }

    @Test
    public void test_Color3_Depth8() throws Exception {
        int color = 3;
        int depth = 8;

        runner.run(lrtMethod, color, depth);
    }

    @Test
    public void test_Color3_Depth12() throws Exception {
        int color = 3;
        int depth = 12;

        runner.run(lrtMethod, color, depth);
    }

    // TEN COLORED

    @Test
    public void test_Color10_Depth4() throws Exception {
        int color = 10;
        int depth = 4;

        runner.run(lrtMethod, color, depth);
    }

    @Test
    public void test_Color10_Depth8() throws Exception {
        int color = 10;
        int depth = 8;

        runner.run(lrtMethod, color, depth);
    }

    @Test
    public void test_Color10_Depth12() throws Exception {
        int color = 10;
        int depth = 12;

        runner.run(lrtMethod, color, depth);
    }

}
