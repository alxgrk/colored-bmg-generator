package de.uni.leipzig.performance;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.google.common.base.Stopwatch;

import de.uni.leipzig.RandomTree;
import de.uni.leipzig.model.AdjacencyList;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Runner {

    final int maxChildren;

    final int minDepth;

    public void run(Consumer<AdjacencyList> creation, int color, int depth) {
        Stopwatch sw = Stopwatch.createStarted();

        RandomTree randomTree = new RandomTree(maxChildren, depth, color).minDepth(minDepth);
        AdjacencyList adjList = randomTree.create();

        System.out.println("Color=" + color + " - Depth=" + minDepth + "-" + depth);
        try {
            creation.accept(adjList);
        } catch (Exception e) {
            System.err.println(e);
            return;
        }

        Long elapsedMilliSeconds = sw.stop().elapsed(TimeUnit.MILLISECONDS);
        System.out.println("took " + (elapsedMilliSeconds.doubleValue() / 1000));
    }
}
