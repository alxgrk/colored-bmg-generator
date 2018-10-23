package de.uni.leipzig.method;

import java.util.Set;

import org.jgrapht.alg.util.Pair;

import com.google.common.annotations.VisibleForTesting;

import static de.uni.leipzig.method.TreeCreation.Method.AHO;

import de.uni.leipzig.Util;
import de.uni.leipzig.informative.InformativeTripleFinder2;
import de.uni.leipzig.informative.model.InformativeTriple;
import de.uni.leipzig.model.AdjacencyList;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Tree;
import de.uni.leipzig.twocolored.DiGraphExtractor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

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
    public Tree create(AdjacencyList adjList) {
    	DiGraph diGraph = diGraphExtractor.extract(adjList);
        Pair<Set<InformativeTriple>, Set<Node>> triplesAndLeaves = informativeTripleFinder
                .findTriple(diGraph);
        Set<InformativeTriple> informativeTriples = triplesAndLeaves.getFirst();

        return aho.create(Util.uglyCast(informativeTriples), triplesAndLeaves.getSecond());
    }

    @Override
    public Tree create(DiGraph diGraph) {
        Pair<Set<InformativeTriple>, Set<Node>> triplesAndLeaves = informativeTripleFinder
                .findTriple(diGraph);
        Set<InformativeTriple> informativeTriples = triplesAndLeaves.getFirst();

        return aho.create(Util.uglyCast(informativeTriples), triplesAndLeaves.getSecond());
    }

}