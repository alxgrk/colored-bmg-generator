package de.uni.leipzig.performance;

import java.util.List;

import org.junit.*;
import org.junit.runners.MethodSorters;

import com.google.common.collect.Lists;

import de.uni.leipzig.method.TreeCreation;
import de.uni.leipzig.method.TreeCreation.Method;
import de.uni.leipzig.model.Pair;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ColorDepthRelationshipWithThinnessAsLRT {

    static int MAX_CHILDREN = 2;

    static int MIN_DEPTH = 3;

    static int[] COLORS = new int[] { 2, 3, 10 };

    static List<Pair<Integer>> DEPTHS_WITH_MIN = Lists.newArrayList(
            new Pair<>(MIN_DEPTH, 4),
            new Pair<>(MIN_DEPTH, 8),
            new Pair<>(MIN_DEPTH, 12));

    TreeCreation lrtMethod = Method.THINNESS_CLASS.get().inNonInteractiveMode(true);

    private Runner runner;

    @Before
    public void setUp() throws Exception {
        runner = new Runner(MAX_CHILDREN, MIN_DEPTH);
    }

    // TWO COLORED

    @Test
    public void test_Color2_Depth3til4() throws Exception {
        int color = 2;
        int depth = 4;

        runner.run(adj -> lrtMethod.create(adj), color, depth);
    }

    @Test
    public void test_Color2_Depth8() throws Exception {
        int color = 2;
        int depth = 8;

        runner.run(adj -> lrtMethod.create(adj), color, depth);
    }

    @Test
    public void test_Color2_Depth12() throws Exception {
        int color = 2;
        int depth = 12;

        runner.run(adj -> lrtMethod.create(adj), color, depth);

    }

    // THREE COLORED

    @Test
    public void test_Color3_Depth4() throws Exception {
        int color = 3;
        int depth = 4;

        runner.run(adj -> lrtMethod.create(adj), color, depth);
    }

    @Test
    public void test_Color3_Depth8() throws Exception {
        int color = 3;
        int depth = 8;

        runner.run(adj -> lrtMethod.create(adj), color, depth);
    }

    @Test
    public void test_Color3_Depth12() throws Exception {
        int color = 3;
        int depth = 12;

        runner.run(adj -> lrtMethod.create(adj), color, depth);
    }

    // TEN COLORED

    @Test
    public void test_Color10_Depth4() throws Exception {
        int color = 10;
        int depth = 4;

        runner.run(adj -> lrtMethod.create(adj), color, depth);
    }

    @Test
    public void test_Color10_Depth8() throws Exception {
        int color = 10;
        int depth = 8;

        runner.run(adj -> lrtMethod.create(adj), color, depth);
    }

    @Test
    public void test_Color10_Depth12() throws Exception {
        int color = 10;
        int depth = 12;

        runner.run(adj -> lrtMethod.create(adj), color, depth);
    }

}
