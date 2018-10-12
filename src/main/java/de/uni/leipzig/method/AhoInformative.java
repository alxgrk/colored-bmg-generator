package de.uni.leipzig.method;

import static de.uni.leipzig.method.TreeCreation.Method.AHO;

import java.util.Set;

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
    public void create(AdjacencyList adjList) {
        Set<InformativeTriple> informativeTriples = informativeTripleFinder
                .findTriple(adjList);

        aho.create(Util.uglyCast(informativeTriples), informativeTripleFinder.getLeaves());
    }

    @Override
    public void create(Set<Triple> triples, DiGraph diGraph) {
        Set<InformativeTriple> informativeTriples = informativeTripleFinder
                .findTriple(triples, diGraph);

        aho.create(Util.uglyCast(informativeTriples), informativeTripleFinder.getLeaves());
    }
}