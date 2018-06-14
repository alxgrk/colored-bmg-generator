package de.uni.leipzig.model;


import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class DiGraph {
	
	private Set<Node> nodes = new HashSet<>();
	private Set<DiEdge> edges = new HashSet<>();
	
	public void addNode(Node node) {
		nodes.add(node);
	}

	public void addEdge(DiEdge edge) {
		edges.add(edge);
	}
	
	
}
