package de.uni.leipzig.method;

import com.google.common.annotations.VisibleForTesting;

import de.uni.leipzig.model.*;
import de.uni.leipzig.twocolored.DiGraphExtractor2;
import de.uni.leipzig.twocolored.axiom.Axioms;
import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED, onConstructor = @__(@VisibleForTesting))
class ThinnessClass implements TreeCreation {

    private final DiGraphExtractor2 extractor;

    public ThinnessClass() {
        this(new DiGraphExtractor2());
    }

    @Override
    public TreeCreation inNonInteractiveMode(boolean mode) {
        return this;
    }

    @Override
    public Tree create(AdjacencyList adjList) {
        DiGraph graph = extractor.extract(adjList);
        return create(graph);
    }

    @Override
    public Tree create(DiGraph graph) {

        // initialization of all fields
        graph.getNodes();
        graph.getEdges();
        graph.getNeighboursByTc();
        graph.getReachablesByTc();

        boolean axiomsFulfilled = Axioms.checkAll(graph);

        if (!axiomsFulfilled)
            throw new RuntimeException("Axioms not fulfilled!");

        Tree leastResolvedTree = graph.getHasseDiagram();

        System.out.println(leastResolvedTree.print());

        return leastResolvedTree;
    }

}