package de.uni.leipzig.method;

import com.google.common.annotations.VisibleForTesting;

import de.uni.leipzig.model.AdjacencyList;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Reachables;
import de.uni.leipzig.model.Tree;
import de.uni.leipzig.twocolored.DiGraphExtractor2;
import de.uni.leipzig.twocolored.axiom.Axioms;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED, onConstructor = @__(@VisibleForTesting))
class ThinnessClass implements TreeCreation {

    private final DiGraphExtractor2 extractor;

    public ThinnessClass() {
        this(new DiGraphExtractor2());
    }

    @Override
    public void create(AdjacencyList adjList) {
        DiGraph graph = extractor.extract(adjList);
        create(graph);
    }

    @Override
    public void create(DiGraph graph) {

        System.out.println("Nodes: " + graph.getNodes());
        System.out.println("Edges: " + graph.getEdges());

        System.out.println("Neighbourhood: " + graph.getNeighboursByTc());
        graph.getReachablesByTc()
                .values()
                .stream()
                .map(Reachables::getRq)
                .forEach(rq -> System.out.println("RQ: " + rq));

        boolean axiomsFulfilled = Axioms.checkAll(graph);

        if (!axiomsFulfilled)
            throw new RuntimeException("Axioms not fulfilled!");

        Tree leastResolvedTree = graph.getHasseDiagram();
        System.out.println(leastResolvedTree.toNewickNotation());
        System.out.println(leastResolvedTree.print());
    }

}