package de.uni.leipzig.ncolored;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import de.uni.leipzig.Util;
import de.uni.leipzig.method.TreeCreation;
import de.uni.leipzig.model.*;
import de.uni.leipzig.uncolored.ConnectedComponentsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NColored {

    private final ConnectedComponentsConstructor componentsConstructor;

    public NColored() {
        this(new ConnectedComponentsConstructor());
    }

    public void by(TreeCreation tc, DiGraph diGraph) {
        // check for edges with same color
        checkColorOfEdges(diGraph);

        // determine connected components
        List<DiGraph> components = componentsConstructor.construct(diGraph);

        // are there some (what does 'some' mean? -> half of the connected component set size
        // assumed here) components with completely distinct color sets?
        checkForSomeComponentsWithDistinctColors(components);

        // iterate over all connected components
        for (DiGraph cc : components) {

            // for every two colors s,t
            for (Color s : cc.getColors()) {
                for (Color t : cc.getColors()) {

                    // s and t the same?
                    if (s.equals(t))
                        continue;

                    // create induced subgraph for s,t
                    DiGraph stSubGraph = cc.subGraphForLabels(s, t);

                    // determine connected components

                    // iterate over all connected components
                    {
                        // apply tc

                        // exit if result is null
                    }

                    // append every subtree per connected component to new root r(s,t)
                }
            }

            // run Deng - Fernandez-Baca

            // exit if result is null
        }

        // append every subtree per color to new root r

        // return r
    }

    private void checkColorOfEdges(DiGraph diGraph) {
        diGraph.getEdges()
                .stream()
                .filter(e -> e.getFirst().getColor().equals(e.getSecond().getColor()))
                .findAny()
                .ifPresent(e -> {
                    throw new RuntimeException("not a BMG");
                });
    }

    private void checkForSomeComponentsWithDistinctColors(List<DiGraph> components) {
        final AtomicInteger componentsWithDistinctColorSets = new AtomicInteger(0);
        final int limit = components.size() / 2;
        components.stream()
                .reduce((c1, c2) -> {
                    if (!Util.equalSets(c1.getColors(), c2.getColors())) {
                        if (componentsWithDistinctColorSets.incrementAndGet() > limit) {
                            throw new RuntimeException("not a BMG");
                        }
                    }
                    return c2;
                });
    }

}
