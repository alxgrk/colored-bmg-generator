package de.uni.leipzig.model;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uni.leipzig.model.edges.DiEdge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ThreeNodeGraph {

	@Getter
	private final Triple threeNodes;

	private Set<DiEdge> edges = Sets.newHashSet();

	@Getter
	private Map<Node, Integer> nodeFrequency = Maps.newHashMap();

	public ThreeNodeGraph(Triple triple) {
		threeNodes = triple;

		nodeFrequency.put(triple.getEdge().getFirst(), 0);
		nodeFrequency.put(triple.getEdge().getSecond(), 0);
		nodeFrequency.put(triple.getNode(), 0);
	}

	public void add(DiEdge edge) {

		incrementCount(edge.getFirst());
		incrementCount(edge.getSecond());

		edges.add(edge);
	}

	private void incrementCount(Node node) {
		nodeFrequency.put(node, nodeFrequency.get(node) + 1);
	}

}
