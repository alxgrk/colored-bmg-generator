package de.uni.leipzig.performance;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import com.google.common.base.Stopwatch;

import de.uni.leipzig.RandomTree;
import de.uni.leipzig.method.TreeCreation;
import de.uni.leipzig.model.*;
import de.uni.leipzig.ncolored.NColored;
import de.uni.leipzig.ncolored.NColored.SuperTreeMethod;

public class ThreeColoredRunner {

    final List<Integer> colors;

    final Class<?> testClass;

    final Integer leafNumber;

    final Supplier<TreeCreation> lrtMethod;

    final Supplier<NColored> nColored;

    public ThreeColoredRunner(Class<?> testClass, List<Integer> colors, Integer leafNumber,
            Supplier<TreeCreation> lrtMethod) {
        this.testClass = testClass;
        this.colors = colors;
        this.leafNumber = leafNumber;
        this.lrtMethod = lrtMethod;
        nColored = NColored::new;
    }

    public void run(SuperTreeMethod stMethod) throws IOException {
        File report = createTestReport(stMethod);

        String runInformation = colors.stream()
                .map(i -> i.toString())
                .reduce((i1, i2) -> i1 + "," + i2)
                .get();

        writeln(report, runInformation);

        int totalExceptions = 0;

        for (int i = 1; i <= 5; i++) {

            System.out.println("now: " + i + ". run of ST-Method " + stMethod.toString());

            for (Integer color : colors) {

                boolean exceptionWasThrown;
                do {
                    try {
                        runFor(report, stMethod, color);
                        exceptionWasThrown = false;
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        exceptionWasThrown = true;
                        totalExceptions++;
                    }
                } while (exceptionWasThrown);

                System.out.println("done with color number " + color);
            }

        }

        writeln(report, "Random trees that were no BMGs: " + totalExceptions);
    }

    private void runFor(File report, SuperTreeMethod stMethod, Integer color)
            throws IOException {
        Stopwatch sw = Stopwatch.createStarted();

        RandomTree randomTree = new RandomTree(2, color).numberOfLeaves(Optional.of(leafNumber));
        AdjacencyList adjList = randomTree.create();

        // TreeCreation.Method.AHO.get().inNonInteractiveMode(true).create(adjList);

        System.out.println("Created " + adjList.getChildNodes().size() + " children");

        Tree result = nColored.get()
                .stMethod(new Container<>(stMethod))
                .alwaysMinCut(true)
                .by(g -> lrtMethod.get().create(g), adjList);

        Long elapsedMicroSeconds = sw.stop().elapsed(TimeUnit.MICROSECONDS);
        String asString = String.format("%f", elapsedMicroSeconds.doubleValue() / 1000000);
        write(report, asString);

        System.out.println(result.print());

        if (!(colors.indexOf(color) == colors.size() - 1))
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

    private File createTestReport(SuperTreeMethod stMethod) throws IOException {
        File testReport = new File(ThreeColoredRunner.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath()
                + "/" + testClass.getSimpleName() + "-" + stMethod.toString()
                + ".report");

        if (testReport.exists())
            testReport.delete();

        testReport.createNewFile();

        return testReport;
    }
}
