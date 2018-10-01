package de.uni.leipzig.informative.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import de.uni.leipzig.model.DefaultTriple;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.model.edges.DiEdge;
import de.uni.leipzig.model.edges.Edge;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class ThreeNodeGraph {

    private final Triple triple;

    private Set<DiEdge> edges = Sets.newHashSet();

    private Map<Node, Integer> nodeFrequency = new HashMap<>(3);

    /**
     * Helper to create a three node graph without a triple, which will look like this: <br>
     * -> one,two|three
     * 
     * @param one
     * @param two
     * @param three
     */
    public ThreeNodeGraph(Node one, Node two, Node three) {
        this(new DefaultTriple(new Edge(one, two), three));
    }

    public ThreeNodeGraph(@NonNull Triple triple) {
        this.triple = triple;

        nodeFrequency.put(triple.getEdge().getFirst(), 0);
        nodeFrequency.put(triple.getEdge().getSecond(), 0);
        nodeFrequency.put(triple.getNode(), 0);
    }

    /**
     * Adds the edge to the set, if and only if the nodes of this edge are within the
     * {@link ThreeNodeGraph}s nodes. Return true in this case, false otherwise.
     * 
     * @param edge
     * @return
     */
    public boolean add(DiEdge edge) {

        boolean isContained = triple.contains(edge.getFirst())
                && triple.contains(edge.getSecond());

        if (isContained) {
            incrementCount(edge.getFirst());
            incrementCount(edge.getSecond());

            edges.add(edge);
        }

        return isContained;
    }

    private void incrementCount(Node node) {
        nodeFrequency.put(node, nodeFrequency.get(node) + 1);
    }

}
