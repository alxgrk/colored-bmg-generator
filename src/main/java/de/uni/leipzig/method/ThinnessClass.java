package de.uni.leipzig.method;

import de.uni.leipzig.model.AdjacencyList;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Reachables;
import de.uni.leipzig.model.Tree;
import de.uni.leipzig.twocolored.DiGraphExtractor;
import de.uni.leipzig.twocolored.axiom.Axioms;

class ThinnessClass implements TreeCreation {

    @Override
    public void create(AdjacencyList adjList) {
        DiGraphExtractor extractor = new DiGraphExtractor();
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
        System.out.println(leastResolvedTree);
        System.out.println(leastResolvedTree.print());
    }

}