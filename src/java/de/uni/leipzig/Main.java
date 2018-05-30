package de.uni.leipzig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Tree;
import de.uni.leipzig.model.Triple;

public class Main {

    public static void main(String[] args) throws IOException {

    	RandomTree randomTree = new RandomTree(2, 5, 2);
    	randomTree.create();
    	
    	List<List<Node>> adjList = randomTree.getAdjList();
    	
    	TripleFinder tripleFinder = new TripleFinder();
		List<Triple> triple = tripleFinder.findTriple(adjList);
    	
        System.out.println(triple.toString());
        
        Tree result = AhoBuild.build(new HashSet<>(triple), new ArrayList<>(tripleFinder.getLeaves()));
        
        System.out.println(result.toNewickNotation());
    }

}