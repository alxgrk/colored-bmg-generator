package de.uni.leipzig;

import java.io.IOException;
import java.util.List;

import de.uni.leipzig.informative.InformativeTriple;
import de.uni.leipzig.informative.InformativeTripleFinder;
import de.uni.leipzig.model.Node;

public class Main {

    public static void main(String[] args) throws IOException {

        RandomTree randomTree = new RandomTree(2, 4, 2);
        List<List<Node>> adjList = randomTree.create();

        // **** Aho Build ohne informative triple ****
//          TripleFinder tripleFinder = new TripleFinder(); 
//          List<Triple> triple = tripleFinder.findTriple(adjList);
//          
//          System.out.println(triple.toString());
//          
//          Tree result = AhoBuild.build(new HashSet<>(triple), new
//          ArrayList<>(tripleFinder.getLeaves()));
//          
//          System.out.println(result.toNewickNotation());
//         
//          System.exit(0);
        // ******************************

        // **** Äquivalenzklasse part ****
//        DiGraphExtractor extractor = new DiGraphExtractor();
//        DiGraph graph = extractor.extract(adjList);
//
//        System.out.println("Nodes: " + graph.getNodes());
//        System.out.println("Edges: " + graph.getEdges());
//
//        System.out.println("Neighbourhood: " + graph.getNeighboursByÄk());
////        System.out.println("Reachables: " + graph.getReachablesByÄk());
//        graph.getReachablesByÄk().keySet().forEach(System.out::println);
//        graph.getReachablesByÄk().values().stream()
//        	.map(Reachables::getRq)
//        	.forEach(System.out::println);
//
//        boolean axiomsFulfilled = Axioms.checkAll(graph);
//
//        if (!axiomsFulfilled)
//            throw new RuntimeException("Axioms not fulfilled!");
//
//        Tree leastResolvedTree = graph.getHasseDiagram();
//        System.out.println(leastResolvedTree);
        // ******************************
        

        // **** Aho Build mit informative triple ****
        
        InformativeTripleFinder informativeTripleFinder = new InformativeTripleFinder();
        List<InformativeTriple> informativeTriples = informativeTripleFinder.findInformativeTriples(adjList);
        System.out.println(informativeTriples);
        
        // ******************************
        
    }

}