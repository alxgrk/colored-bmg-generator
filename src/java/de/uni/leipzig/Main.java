package de.uni.leipzig;

import java.io.IOException;
import java.util.List;

import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Triple;

public class Main {

    public static void main(String[] args) throws IOException {

    	RandomTree randomTree = new RandomTree(4, 10, 2);
    	randomTree.create();
    	
    	List<List<Node>> adjList = randomTree.getAdjList();
    	
    	List<Triple> tripel = new TripleFinder().findTripel(adjList);
    	
        System.out.println(tripel.toString());
    }

}