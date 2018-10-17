package de.uni.leipzig.uncolored;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.alg.flow.GusfieldGomoryHuCutTree;
import org.jgrapht.graph.SimpleGraph;

import de.uni.leipzig.model.*;
import de.uni.leipzig.model.edges.Edge;

public class DiGraphFromTripleSet {

    private final ConnectedComponentsConstructor components = new ConnectedComponentsConstructor();

    private Graph<Node, Edge> g = new SimpleGraph<>(Edge.class);

    public List<Tree> create(Set<Triple> tripleSetR, Set<Node> leaveSetL) {

        // note that the GusfieldGomoryHuCutTree requires the graph to be undirected
        Graph<Node, Edge> graph = diGraphFromTripleSet(tripleSetR);
        GusfieldGomoryHuCutTree<Node, Edge> minCutGraph = new GusfieldGomoryHuCutTree<>(graph);
        minCutGraph.calculateMinCut();
        Set<Node> sink = minCutGraph.getSinkPartition();
        Set<Node> source = minCutGraph.getSourcePartition();

        Set<Triple> cutTripleSet = new HashSet<>();

        for (Triple triple : tripleSetR) {
            if (sink.contains(triple.getEdge().getFirst()) && source.contains(triple.getEdge()
                    .getSecond())
                    || source.contains(triple.getEdge().getFirst()) && sink.contains(triple
                            .getEdge()
                            .getSecond())) {
                continue;
            } else {
                cutTripleSet.add(triple);
            }
        }

        return components.construct(cutTripleSet, leaveSetL);
    }

    private Graph<Node, Edge> diGraphFromTripleSet(Set<Triple> tripleSetR) {

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
