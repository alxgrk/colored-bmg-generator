package de.uni.leipzig.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import org.assertj.core.util.Sets;

import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.edges.DiEdge;

public class Parser {

	public DiGraph parse(File input) throws IOException {
		
		if (!input.exists() || !input.canRead()) {
			throw new IllegalArgumentException("File not readable!!");
		}
		
		try (BufferedReader reader = new BufferedReader(new FileReader(input));) {
			
			Set<Node> nodes = Sets.newHashSet();
			Set<DiEdge> edges = Sets.newHashSet();
			
			reader.lines()
				.filter(f -> f.startsWith("#"))
				.map(l -> new BlastGraphLine(l))
				.forEach(l -> {
					Node node1 = l.getGene1().asNode();
					Node node2 = l.getGene2().asNode();
					nodes.add(node1);
					nodes.add(node2);
					
					edges.add(new DiEdge(node1, node2));
					edges.add(new DiEdge(node2, node1));
				});
			
			return new DiGraph(nodes, edges);
		}
	}
	
}
