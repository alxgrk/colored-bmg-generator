package de.uni.leipzig;

import java.io.IOException;
import java.util.List;

import de.uni.leipzig.colored.DiGraphExtractor;
import de.uni.leipzig.colored.axiom.Axioms;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Tree;

public class Main {

    public static void main(String[] args) throws IOException {

        RandomTree randomTree = new RandomTree(2, 5, 2);
        List<List<Node>> adjList = randomTree.create();

        /*
         * TripleFinder tripleFinder = new TripleFinder(); List<Triple> triple =
         * tripleFinder.findTriple(adjList);
         * 
         * System.out.println(triple.toString());
         * 
         * Tree result = AhoBuild.build(new HashSet<>(triple), new
         * ArrayList<>(tripleFinder.getLeaves()));
         * 
         * System.out.println(result.toNewickNotation());
         */

        DiGraphExtractor extractor = new DiGraphExtractor();

        DiGraph graph = extractor.extract(adjList);

        System.out.println("Nodes: " + graph.getNodes());
        System.out.println("Edges: " + graph.getEdges());

        System.out.println("Neighbourhood: " + graph.getNeighboursByÄk());
        System.out.println("Reachables: " + graph.getReachablesByÄk());

        boolean axiomsFulfilled = Axioms.checkAll(graph);

        if (!axiomsFulfilled)
            throw new RuntimeException("Axioms not fulfilled!");

        Tree leastResolvedTree = graph.getHasseDiagram();
        System.out.println(leastResolvedTree);
    }

}