package de.uni.leipzig.method;

import static de.uni.leipzig.method.TreeCreation.Method.AHO;

import java.util.*;

import org.jgrapht.alg.util.Pair;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;

import de.uni.leipzig.Util;
import de.uni.leipzig.informative.InformativeTripleFinder2;
import de.uni.leipzig.informative.model.InformativeTriple;
import de.uni.leipzig.model.*;
import de.uni.leipzig.twocolored.DiGraphExtractor;
import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED, onConstructor = @__(@VisibleForTesting))
class AhoInformative implements TreeCreation {

    private final InformativeTripleFinder2 informativeTripleFinder;

    private final DiGraphExtractor diGraphExtractor;

    private final TreeCreation aho;

    public AhoInformative() {
        this(new InformativeTripleFinder2(), new DiGraphExtractor(), AHO.get());
    }

    @Override
    public TreeCreation inNonInteractiveMode(boolean mode) {
        aho.inNonInteractiveMode(mode);
        return this;
    }

    @Override
    public TreeCreation inPrintMode(boolean mode) {
        aho.inPrintMode(mode);
        return this;
    }

    @Override
    public Tree create(AdjacencyList adjList) {
        DiGraph diGraph = diGraphExtractor.extract(adjList);
        return create(diGraph);
    }

    @Override
    public Tree create(DiGraph diGraph) {

        Set<Node> nodes = diGraph.getNodes();
        if (nodes.size() < 3) {
            if (nodes.size() == 2) {
                Iterator<Node> it = nodes.iterator();
                Tree a = new Tree(it.next());
                Tree b = new Tree(it.next());
                return new Tree(Lists.newArrayList(a, b));
            }
            if (nodes.size() == 1) {
                return new Tree(nodes.iterator().next());
            }
            if (nodes.size() == 0) {
                return new Tree(Node.helpNode());
            }
        }

        Pair<Set<InformativeTriple>, Set<Node>> triplesAndLeaves = informativeTripleFinder
                .findTriple(diGraph);
        Set<InformativeTriple> informativeTriples = triplesAndLeaves.getFirst();

        return aho.create(Util.uglyCast(informativeTriples), triplesAndLeaves.getSecond());
    }

}