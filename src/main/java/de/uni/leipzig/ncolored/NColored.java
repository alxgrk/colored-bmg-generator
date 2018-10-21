package de.uni.leipzig.ncolored;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import de.uni.leipzig.Util;
import de.uni.leipzig.conversion.TripleFromTree;
import de.uni.leipzig.model.*;
import de.uni.leipzig.ncolored.dengfernandezbaca.BuildST;
import de.uni.leipzig.ncolored.dengfernandezbaca.BuildST.IncompatibleProfileException;
import de.uni.leipzig.uncolored.*;
import de.uni.leipzig.user.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NColored {

    private final ConnectedComponentsConstructor componentsConstructor;

    private final BuildST buildST;

    private final AhoBuild aho;

    private final TripleFromTree tripleFromTree;

    private final UserInput treeCombinationMethod;

    public NColored() {
        this(new ConnectedComponentsConstructor(), new BuildST(), new AhoBuild(),
                new TripleFromTree(), new UserInput());
    }

    public Tree by(Function<DiGraph, Tree> creation, DiGraph diGraph) {
        // check for edges with same color
        checkColorOfEdges(diGraph);

        // determine connected components
        List<DiGraph> components = componentsConstructor.construct(diGraph);

        // are there some (what does 'some' mean? -> half of the connected component set size
        // assumed here) components with completely distinct color sets?
        checkForSomeComponentsWithDistinctColors(components);

        // iterate over all connected components and return result
        return components.stream()
                .map(cc -> {

                    // store all stTrees
                    Map<Pair<Color>, Tree> stTrees = new HashMap<>();

                    // for every two colors s,t
                    for (Color s : cc.getColors()) {
                        for (Color t : cc.getColors()) {

                            // s and t the same?
                            if (s.equals(t) || stTrees.containsKey(new Pair<>(s, t)))
                                continue;

                            // create induced subgraph for s,t
                            DiGraph stSubGraph = cc.subGraphForLabels(s, t);

                            // determine connected components
                            List<DiGraph> stComponents = componentsConstructor.construct(
                                    stSubGraph);

                            // iterate over all connected components
                            Tree stTree = stComponents.stream()
                                    .map(stCC -> {
                                        // apply tree creation method
                                        Tree result = creation.apply(stCC);

                                        // exit if result is null
                                        checkOnlyHelpNodeTree(result);

                                        return result;
                                    })
                                    // append every subtree per connected component to new root
                                    // r(s,t)
                                    .reduce(new Tree(Sets.newHashSet(Node.helpNode())),
                                            (i, subT) -> i.addSubTree(subT));
                            stTrees.put(new Pair<>(s, t), stTree);
                        }
                    }

                    return askForSuperTreeMethod(stTrees);

                })
                // append every subtree per connected component to new root r
                .reduce(new Tree(Sets.newHashSet(Node.helpNode())),
                        (i, subT) -> i.addSubTree(subT));

    }

    private Tree askForSuperTreeMethod(Map<Pair<Color>, Tree> stTrees) {

        Result<Tree> result = Result.empty();

        treeCombinationMethod.register("extract triples and run aho", () -> {

            Set<Triple> triples = tripleFromTree.extractOf(stTrees);
            Set<Node> leaves = stTrees.values()
                    .stream()
                    .flatMap(t -> t.getAllSubNodes().stream())
                    .collect(Collectors.toSet());

            Tree resultingTree = aho.build(triples, leaves);
            result.fixValue(resultingTree);
        });

        treeCombinationMethod.register("run deng & fernandez-baca", () -> {

            // run Deng - Fernandez-Baca
            try {
                result.fixValue(buildST.build(stTrees));
            } catch (IncompatibleProfileException e) {
                // tree was incompatible
                throw new RuntimeException("not a BMG");
            }

        });

        treeCombinationMethod.askWithOptions("What tree combination method should be run?");

        return result.getValue();
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

    private void checkOnlyHelpNodeTree(Tree result) {
        if (result.getAllSubNodes().size() == 1
                && result.getAllSubNodes().get(0).equals(Node.helpNode()))
            throw new RuntimeException("not a BMG");
    }

}
