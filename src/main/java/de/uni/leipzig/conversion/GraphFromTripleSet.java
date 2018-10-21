package de.uni.leipzig.conversion;

import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;

import de.uni.leipzig.model.*;
import de.uni.leipzig.model.edges.Edge;

public class GraphFromTripleSet {

    public Graph<Node, Edge> toJGraph(Set<Triple> tripleSetR) {

        Graph<Node, Edge> g = new SimpleGraph<>(Edge.class);

        for (Triple triple : tripleSetR) {
            g.addVertex(triple.getNode());
            g.addVertex(triple.getEdge().getFirst());
            g.addVertex(triple.getEdge().getSecond());

            g.addEdge(triple.getEdge().getFirst(), triple.getEdge().getSecond(),
                    triple.getEdge());
        }

        return g;
    }
}
