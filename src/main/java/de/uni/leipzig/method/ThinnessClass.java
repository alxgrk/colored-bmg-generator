package de.uni.leipzig.method;

import com.google.common.annotations.VisibleForTesting;

import de.uni.leipzig.model.*;
import de.uni.leipzig.twocolored.DiGraphExtractor;
import de.uni.leipzig.twocolored.axiom.Axioms;
import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED, onConstructor = @__(@VisibleForTesting))
class ThinnessClass implements TreeCreation {

    private final DiGraphExtractor extractor;

    private boolean inPrintMode = true;

    public ThinnessClass() {
        this(new DiGraphExtractor());
    }

    @Override
    public TreeCreation inNonInteractiveMode(boolean mode) {
        return this;
    }

    @Override
    public TreeCreation inPrintMode(boolean mode) {
        inPrintMode = mode;
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

        if (inPrintMode)
            System.out.println(leastResolvedTree.print());

        return leastResolvedTree;
    }

}