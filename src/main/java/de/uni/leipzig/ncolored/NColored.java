package de.uni.leipzig.ncolored;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.zalando.fauxpas.ThrowingRunnable;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;

import de.uni.leipzig.Util;
import de.uni.leipzig.conversion.TripleFromTree;
import de.uni.leipzig.model.Color;
import de.uni.leipzig.model.Container;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Tree;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.ncolored.dengfernandezbaca.DFBBuildST;
import de.uni.leipzig.ncolored.dengfernandezbaca.DFBBuildST.IncompatibleProfileException;
import de.uni.leipzig.uncolored.AhoBuild;
import de.uni.leipzig.uncolored.ConnectedComponentsConstructor;
import de.uni.leipzig.user.Result;
import de.uni.leipzig.user.UserInput;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED, onConstructor = @__(@VisibleForTesting))
public class NColored {

    private final ConnectedComponentsConstructor componentsConstructor;

    private final DFBBuildST dfbBuildST;

    private final AhoBuild aho;

    private final TripleFromTree tripleFromTree;

    private final UserInput treeCombinationMethod;

    public NColored() {
        this(new ConnectedComponentsConstructor(), new DFBBuildST(), new AhoBuild(),
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

        // remember SuperTree method
        Container<SuperTreeMethod> stMethod = Container.empty();

        // iterate over all connected components and return result
        return components.stream()
                .map(cc -> {

                    // store all stTrees
                    Map<Set<Color>, Tree> stTrees = new HashMap<>();

                    // for every two colors s,t
                    for (Color s : cc.getColors()) {
                        for (Color t : cc.getColors()) {

                            // s and t the same?
                            if (s.equals(t) || stTrees.containsKey(Sets.newHashSet(s, t)))
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
                                    .reduce(new Tree(Node.helpNode()),
                                            (i, subT) -> i.addSubTree(subT));
                            stTrees.put(Sets.newHashSet(s, t), stTree);
                        }
                    }

                    return askForSuperTreeMethod(stTrees, stMethod);

                })
                // append every subtree per connected component to new root r
                .reduce(new Tree(Node.helpNode()),
                        (i, subT) -> i.addSubTree(subT));

    }

    private Tree askForSuperTreeMethod(Map<Set<Color>, Tree> stTrees,
            Container<SuperTreeMethod> stMethod) {

        Result<Tree> result = Result.empty();

        ThrowingRunnable<Exception> ahoAction = () -> {

            stMethod.setValue(SuperTreeMethod.AHO);

            // TODO has problems with AHO INFORMATIVE as tc...
            Set<Triple> triples = tripleFromTree.extractOf(stTrees);
            Set<Node> leaves = stTrees.values()
                    .stream()
                    .flatMap(t -> t.getLeafs().stream())
                    .collect(Collectors.toSet());

            Tree resultingTree = aho.build(triples, leaves);
            result.fixValue(resultingTree);
        };

        ThrowingRunnable<Exception> dfbAction = () -> {

            stMethod.setValue(SuperTreeMethod.DENG_FERNANDEZ_BACA);

            // run Deng - Fernandez-Baca
            try {
                result.fixValue(dfbBuildST.build(stTrees));
            } catch (IncompatibleProfileException e) {
                // tree was incompatible
                throw new RuntimeException("not a BMG", e);
            }

        };

        if (!stMethod.isEmpty()) {
            if (stMethod.getValue().equals(SuperTreeMethod.AHO))
                ahoAction.run();
            if (stMethod.getValue().equals(SuperTreeMethod.DENG_FERNANDEZ_BACA))
                dfbAction.run();
        } else {
            treeCombinationMethod.clear();
            treeCombinationMethod.register("extract triples and run aho", ahoAction);
            treeCombinationMethod.register("run deng & fernandez-baca", dfbAction);
            treeCombinationMethod.askWithOptions("What tree combination method should be run?");
        }

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
        if (result.getAllNodes().size() == 1
                && result.getAllNodes().get(0).equals(Node.helpNode()))
            throw new RuntimeException("not a BMG");
    }

    private enum SuperTreeMethod {
        AHO,
        DENG_FERNANDEZ_BACA;
    }

}
