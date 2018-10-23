package de.uni.leipzig.method;

import static de.uni.leipzig.method.TreeCreation.Method.AHO;

import java.util.Set;

import org.jgrapht.alg.util.Pair;

import com.google.common.annotations.VisibleForTesting;

import de.uni.leipzig.Util;
import de.uni.leipzig.informative.InformativeTripleFinder;
import de.uni.leipzig.informative.model.InformativeTriple;
import de.uni.leipzig.model.*;
import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED, onConstructor = @__(@VisibleForTesting))
class AhoInformative implements TreeCreation {

    private final InformativeTripleFinder informativeTripleFinder;

    private final TreeCreation aho;

    public AhoInformative() {
        this(new InformativeTripleFinder(), AHO.get());
    }

    @Override
    public TreeCreation inNonInteractiveMode(boolean mode) {
        aho.inNonInteractiveMode(mode);
        return this;
    }

    @Override
    public Tree create(AdjacencyList adjList) {
        Pair<Set<InformativeTriple>, Set<Node>> triplesAndLeaves = informativeTripleFinder
                .findTriple(adjList);
        Set<InformativeTriple> informativeTriples = triplesAndLeaves.getFirst();

        return aho.create(Util.uglyCast(informativeTriples), triplesAndLeaves.getSecond());
    }

    @Override
    public Tree create(Set<Triple> triples, DiGraph diGraph) {
        Pair<Set<InformativeTriple>, Set<Node>> triplesAndLeaves = informativeTripleFinder
                .findTriple(triples, diGraph);
        Set<InformativeTriple> informativeTriples = triplesAndLeaves.getFirst();

        return aho.create(Util.uglyCast(informativeTriples), triplesAndLeaves.getSecond());
    }

}