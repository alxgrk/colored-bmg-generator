package de.uni.leipzig.performance;

import java.util.Arrays;
import java.util.function.BiFunction;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.junit.*;
import org.junit.runners.MethodSorters;

import de.uni.leipzig.manipulation.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Manipulation {

    static Integer[] LEAF_NUMBERS = new Integer[] {
            100, 500, 1000
    };

    static Double[] PERCENTAGES = new Double[] {
            50., 90., 99., 99.9, 99.999
    };

    ManipulationRunner manipulationRunner = new ManipulationRunner(Manipulation.class,
            Arrays.asList(PERCENTAGES),
            Arrays.asList(LEAF_NUMBERS));

    @Test
    public void testInsertion() throws Exception {
        BiFunction<String, String, Integer> comparator = (original, manipulated) -> {
            Integer levenshteinDistance = LevenshteinDistance.getDefaultInstance()
                    .apply(original, manipulated);
            System.out.println("Levenshtein distance: " + levenshteinDistance);
            return levenshteinDistance;
        };

        manipulationRunner.run(InsertionManipulator.class, comparator, "levenshtein");
    }

    @Test
    public void testDeletion_BracketRelation() throws Exception {
        BiFunction<String, String, Integer> comparator = (original, manipulated) -> {
            int bracketsInManipulated = StringUtils.countMatches(manipulated, '(');
            int bracketsInOriginal = StringUtils.countMatches(original, '(');
            Integer editPercentage = bracketsInManipulated * 100 / bracketsInOriginal;
            System.out.println(
                    "percentage of brackets compared to original: " + editPercentage);
            return editPercentage;
        };

        manipulationRunner.run(DeletionManipulator.class, comparator, "bracket-relation");
    }

    @Test
    public void testDeletion_Levenshtein() throws Exception {
        BiFunction<String, String, Integer> comparator = (original, manipulated) -> {
            Integer levenshteinDistance = LevenshteinDistance.getDefaultInstance()
                    .apply(original, manipulated);
            System.out.println("Levenshtein distance: " + levenshteinDistance);
            return levenshteinDistance;
        };

        manipulationRunner.run(DeletionManipulator.class, comparator, "levenshtein");
    }

}
