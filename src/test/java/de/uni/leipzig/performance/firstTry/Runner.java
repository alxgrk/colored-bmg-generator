package de.uni.leipzig.performance.firstTry;

import java.io.*;
import java.nio.file.*;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import de.uni.leipzig.RandomTree;
import de.uni.leipzig.method.TreeCreation;
import de.uni.leipzig.model.*;
import de.uni.leipzig.ncolored.NColored;
import de.uni.leipzig.ncolored.NColored.SuperTreeMethod;
import de.uni.leipzig.twocolored.DiGraphExtractor;

public class Runner {

    final int maxChildren;

    final NColored nColored;

    final File testReport;

    DiGraphExtractor diGraphExtractor = new DiGraphExtractor();

    public Runner(Class<?> testClass, int maxChildren, SuperTreeMethod stMethod) {
        try {
            this.maxChildren = maxChildren;
            this.nColored = new NColored()
                    .stMethod(new Container<>(stMethod))
                    .alwaysMinCut(true);
            this.testReport = new File(Runner.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath()
                    + "/" + testClass.getSimpleName() + ".report");

            if (!testReport.exists()) {
                testReport.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run(TreeCreation lrtMethod, int color, int depth) throws IOException {
        Stopwatch sw = Stopwatch.createStarted();

        RandomTree randomTree = new RandomTree(maxChildren, color).maxDepth(Optional.of(depth));
        AdjacencyList adjList = randomTree.create();

        String runInformation = "Color=" + color + " - MaxDepth=" + depth;

        try {
            if (color <= 2) {
                lrtMethod.create(adjList);
            } else {
                DiGraph graph = diGraphExtractor.extract(adjList);
                nColored.by(g -> lrtMethod.create(g), graph);
            }
        } catch (Exception e) {
            write(runInformation);
            write(e.getMessage());
            return;
        }

        Long elapsedMilliSeconds = sw.stop().elapsed(TimeUnit.MILLISECONDS);
        write(runInformation);
        write("took " + (elapsedMilliSeconds.doubleValue() / 1000));
    }

    private void write(String line) throws IOException {
        Files.write(testReport.toPath(), line.concat("\n").getBytes(), StandardOpenOption.APPEND);
    }
}
