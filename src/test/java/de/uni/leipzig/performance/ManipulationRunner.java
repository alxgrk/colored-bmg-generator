package de.uni.leipzig.performance;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.*;
import java.util.*;
import java.util.function.BiFunction;

import org.jgrapht.alg.util.Pair;

import de.uni.leipzig.RandomTree;
import de.uni.leipzig.manipulation.Manipulator;
import de.uni.leipzig.model.*;
import de.uni.leipzig.uncolored.*;

public class ManipulationRunner {

    final List<Integer> leafNumbers;

    final Class<?> testClass;

    final List<Double> percentages;

    public ManipulationRunner(Class<?> testClass, List<Double> percentages,
            List<Integer> leafNumbers) {
        this.testClass = testClass;
        this.percentages = percentages;
        this.leafNumbers = leafNumbers;
    }

    public void run(Class<? extends Manipulator> manipulator,
            BiFunction<String, String, Integer> comparator, String comparatorDescription)
            throws Exception {
        File report = createTestReport(manipulator, comparatorDescription);

        String runInformation = percentages.stream()
                .map(i -> i.toString())
                .reduce((i1, i2) -> i1 + "," + i2)
                .get();

        for (Integer leafNumber : leafNumbers) {

            writeln(report, "Leaves: " + leafNumber);
            writeln(report, runInformation);

            for (int i = 1; i <= 5; i++) {

                System.out.println("now: " + i + ". run of manipulator " + manipulator
                        .getSimpleName() + " with leaf number " + leafNumber);

                RandomTree randomTree = new RandomTree(2, 2).numberOfLeaves(Optional.of(
                        leafNumber));
                AdjacencyList adjList = randomTree.create();
                Pair<Set<Triple>, Set<Node>> triplesAndLeaves = new DefaultTripleFinder()
                        .findTriple(adjList);

                String originalNewick = newAho()
                        .build(triplesAndLeaves.getFirst(), triplesAndLeaves.getSecond())
                        .toNewickNotation();
                System.out.println("Original Newick: " + originalNewick);

                for (Double percentage : percentages) {

                    runFor(manipulator, report, originalNewick,
                            new HashSet<>(triplesAndLeaves.getFirst()),
                            new HashSet<>(triplesAndLeaves.getSecond()),
                            percentage, comparator);

                    System.out.println("done with percentage " + percentage);
                }

            }

            writeln(report, "");
        }
    }

    private void runFor(Class<? extends Manipulator> manipulator, File report,
            String originalNewick, Set<Triple> triples, Set<Node> leaves,
            Double percentage, BiFunction<String, String, Integer> comparator)
            throws IOException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException {

        Manipulator manipulatorInstance = manipulator.getDeclaredConstructor(Double.class)
                .newInstance(percentage);
        manipulatorInstance.manipulate(triples, leaves);

        Tree result = newAho().build(triples, leaves);
        String manipulatedNewick = result.toNewickNotation();

        System.out.println("Manipulated Newick: " + manipulatedNewick);

        Integer editPercentage = comparator.apply(originalNewick, manipulatedNewick);
        write(report, editPercentage.toString());

        // System.out.println(result.print());

        if (!(percentages.indexOf(percentage) == percentages.size() - 1))
            write(report, ",");
        else
            writeln(report, "");
    }

    private void writeln(File dest, String line) throws IOException {
        write(dest, line.concat("\r\n"));
    }

    private void write(File dest, String string) throws IOException {
        Files.write(dest.toPath(), string.getBytes(), StandardOpenOption.APPEND);
    }

    private File createTestReport(Class<? extends Manipulator> manipulator,
            String comparatorDescription) throws IOException {
        File testReport = new File(ManipulationRunner.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath()
                + "/" + testClass.getSimpleName() + "-" + manipulator.getSimpleName() + "-"
                + comparatorDescription
                + ".report");

        if (testReport.exists())
            testReport.delete();

        testReport.createNewFile();

        return testReport;
    }

    private AhoBuild newAho() {
        AhoBuild ahoBuild = new AhoBuild();
        ahoBuild.setAlwaysMinCut(true);
        return ahoBuild;
    }
}
