package de.uni.leipzig.uncolored;

import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedGraph;

import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.model.edges.DiEdge;

public class DiGraphFromTripleSet {
	
	private Graph<Node, DiEdge> g = new SimpleDirectedGraph<>(DiEdge.class);
	
	public <T extends Triple> Graph<Node, DiEdge> diGraphFromTripleSet(Set<T> tripleSetR){
		
		for (T triple : tripleSetR) {
			g.addVertex(triple.getNode());
			g.addVertex(triple.getEdge().getFirst());
			g.addVertex(triple.getEdge().getSecond());
			
			g.addEdge(triple.getEdge().getFirst(), triple.getEdge().getSecond());
		}
		
		return g;
	}
}
