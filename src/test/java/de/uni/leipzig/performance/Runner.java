package de.uni.leipzig.performance;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import de.uni.leipzig.RandomTree;
import de.uni.leipzig.method.TreeCreation;
import de.uni.leipzig.model.*;
import de.uni.leipzig.ncolored.NColored;
import de.uni.leipzig.ncolored.NColored.SuperTreeMethod;
import de.uni.leipzig.twocolored.DiGraphExtractor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Runner {

    final int maxChildren;

    final int minDepth;

    final SuperTreeMethod stMethod;

    final NColored nColored;

    DiGraphExtractor diGraphExtractor = new DiGraphExtractor();

    public Runner(int maxChildren, int minDepth, SuperTreeMethod stMethod) {
        this(maxChildren, minDepth, stMethod,
                new NColored()
                        .stMethod(new Container<>(stMethod))
                        .alwaysMinCut(true));
    }

    public void run(TreeCreation lrtMethod, int color, int depth) {
        Stopwatch sw = Stopwatch.createStarted();

        RandomTree randomTree = new RandomTree(maxChildren, depth, color).minDepth(minDepth);
        AdjacencyList adjList = randomTree.create();

        String runInformation = "Color=" + color + " - Depth=" + minDepth + "-" + depth;

        try {
            if (color <= 2) {
                lrtMethod.create(adjList);
            } else {
                DiGraph graph = diGraphExtractor.extract(adjList);
                nColored.by(g -> lrtMethod.create(g), graph);
            }
        } catch (Exception e) {
            System.out.println(runInformation);
            System.err.println(e);
            return;
        }

        Long elapsedMilliSeconds = sw.stop().elapsed(TimeUnit.MILLISECONDS);
        System.out.println(runInformation);
        System.out.println("took " + (elapsedMilliSeconds.doubleValue() / 1000));
    }
}
