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

public class Runner {

    final List<Integer> leafNumbers;

    final Class<?> testClass;

    public Runner(Class<?> testClass, List<Integer> leafNumbers) {
        this.testClass = testClass;
        this.leafNumbers = leafNumbers;
    }

    public void run(Supplier<TreeCreation> lrtMethod) throws IOException {
        TreeCreation init = lrtMethod.get();
        File report = createTestReport(init);

        String runInformation = leafNumbers.stream()
                .map(i -> i.toString())
                .reduce((i1, i2) -> i1 + "," + i2)
                .get();

        writeln(report, runInformation);

        for (int i = 1; i <= 5; i++) {

            System.out.println("now: " + i + ". run of LRT-Method " + init.getClass()
                    .getSimpleName());

            for (Integer leafNumber : leafNumbers) {

                boolean exceptionWasThrown;
                do {
                    try {
                        runFor(report, lrtMethod.get(), leafNumber);
                        exceptionWasThrown = false;
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        exceptionWasThrown = true;
                    }
                } while (exceptionWasThrown);

                System.out.println("done with leaf number " + leafNumber);
            }

        }
    }

    private void runFor(File report, TreeCreation lrtMethod, Integer leafNumber)
            throws IOException {
        Stopwatch sw = Stopwatch.createStarted();

        RandomTree randomTree = new RandomTree(2, 2).numberOfLeaves(Optional.of(leafNumber));
        AdjacencyList adjList = randomTree.create();

        // TreeCreation.Method.AHO.get().inNonInteractiveMode(true).create(adjList);

        System.out.println("Created " + adjList.getChildNodes().size() + " children");

        Tree result = lrtMethod.create(adjList);

        Long elapsedMicroSeconds = sw.stop().elapsed(TimeUnit.MICROSECONDS);
        String asString = String.format("%f", elapsedMicroSeconds.doubleValue() / 1000000);
        write(report, asString);

        System.out.println(result.print());

        if (!(leafNumbers.indexOf(leafNumber) == leafNumbers.size() - 1))
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

    private File createTestReport(TreeCreation lrtMethod) throws IOException {
        File testReport = new File(Runner.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath()
                + "/" + testClass.getSimpleName() + "-" + lrtMethod.getClass().getSimpleName()
                + ".report");

        if (testReport.exists())
            testReport.delete();

        testReport.createNewFile();

        return testReport;
    }
}
