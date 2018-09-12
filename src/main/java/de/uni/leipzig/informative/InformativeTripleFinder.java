package de.uni.leipzig.informative;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import de.uni.leipzig.colored.DiGraphExtractor;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.ThreeNodeGraph;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.model.edges.DiEdge;
import de.uni.leipzig.uncolored.TripleFinder;

public class InformativeTripleFinder {

	private static final List<String> POSSIBILITIES = Lists.newArrayList(
			"220", "202", "022", // X1
			"321", "231", "132", "123", // X2
			"110", "101", "011", // X3
			"211", "121", "112" // X4
			);

	private TripleFinder tripleFinder = new TripleFinder();
	private DiGraphExtractor graphExtractor = new DiGraphExtractor();

	public List<InformativeTriple> findInformativeTriples(List<List<Node>> adjList) {
		List<Triple> triples = tripleFinder.findTriple(adjList);
		DiGraph graph = graphExtractor.extract(adjList);

		List<ThreeNodeGraph> subgraphs = Lists.newArrayList();

		for (Triple triple : triples) {

			ThreeNodeGraph subgraph = new ThreeNodeGraph(triple);

			for (DiEdge edge : graph.getEdges()) {

				if (triple.contains(edge.getFirst()) && triple.contains(edge.getSecond())) {
					subgraph.add(edge);
				}

			}

			String nodeFrequencyAsString = subgraph.getNodeFrequency().values().stream().map(i -> i.toString())
					.reduce("", (i, s) -> i.concat(s));

			System.out.println(nodeFrequencyAsString);

			// add node when informative
			if (POSSIBILITIES.contains(nodeFrequencyAsString))
				subgraphs.add(subgraph);
		}

		return subgraphs.stream()
			.map(g -> {
				Triple t = g.getThreeNodes();
				return new InformativeTriple(t.getEdge(), t.getNode());
			})
			.collect(Collectors.toList());
	}

}
