package de.uni.leipzig.informative;

import java.util.*;
import java.util.stream.Collectors;

import org.jgrapht.alg.util.Pair;

import de.uni.leipzig.informative.model.InformativeTriple;
import de.uni.leipzig.model.*;
import de.uni.leipzig.model.edges.*;
import de.uni.leipzig.uncolored.DefaultTripleFinder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InformativeTripleFinder2 {

    private final DefaultTripleFinder defaultTripleFinder;

    public InformativeTripleFinder2() {
        this(new DefaultTripleFinder());
    }

    public Pair<Set<InformativeTriple>, Set<Node>> findTriple(AdjacencyList list) {

        Set<Node> foundNodes = new HashSet<>();

        Set<Triple> triples = defaultTripleFinder.findTriple(list).getFirst();
        Set<DiEdge> edges = triples.stream()
                .map(t -> (DiEdge) t.getEdge())
                .collect(Collectors.toSet());

        Set<InformativeTriple> foundTripleSet = triples.stream()
                .map(t -> {
                    Node node1 = t.getEdge().getFirst();
                    Node node2 = t.getEdge().getSecond();
                    Node node3 = t.getNode();

                    boolean[] edgesExistent = checkEdges(edges, node1, node2, node3);

                    if (edgesExistent.length == 4
                            && checkX(edgesExistent[0], edgesExistent[1],
                                    edgesExistent[2], edgesExistent[3])) {

                        foundNodes.add(node1);
                        foundNodes.add(node2);
                        foundNodes.add(node3);

                        return new InformativeTriple(new Edge(node1, node2), node3);
                    }

                    return null;
                })
                .filter(t -> t != null)
                .collect(Collectors.toSet());

        Pair<Set<InformativeTriple>, Set<Node>> pair = new Pair<>(foundTripleSet, foundNodes);

        return pair;
    }

    public Pair<Set<InformativeTriple>, Set<Node>> findTriple(DiGraph graph) {

        Set<Node> foundNodes = new HashSet<>();

        Set<Node> nodes = graph.getNodes();
        Set<DiEdge> edges = new HashSet<>(graph.getEdges());

        Set<InformativeTriple> foundTripleSet = graph.getEdges()
                .stream()
                .flatMap(e -> {
                    Node node1 = e.getFirst();
                    Node node2 = e.getFirst();

                    return nodes.stream()
                            .filter(n -> n.getColor().equals(node1.getColor()) || n.getColor()
                                    .equals(node2.getColor()))
                            .filter(n -> !node1.equals(n) && !node2.equals(n))
                            .map(n -> new DefaultTriple(e, n));

                })
                .map(t -> {
                    Node node1 = t.getEdge().getFirst();
                    Node node2 = t.getEdge().getSecond();
                    Node node3 = t.getNode();

                    boolean[] edgesExistent = checkEdges(edges, node1, node2, node3);

                    if (edgesExistent.length == 4
                            && checkX(edgesExistent[0], edgesExistent[1],
                                    edgesExistent[2], edgesExistent[3])) {

                        foundNodes.add(node1);
                        foundNodes.add(node2);
                        foundNodes.add(node3);

                        return new InformativeTriple(new Edge(node1, node2), node3);
                    }

                    return null;
                })
                .filter(t -> t != null)
                .collect(Collectors.toSet());

        Pair<Set<InformativeTriple>, Set<Node>> pair = new Pair<>(foundTripleSet, foundNodes);

        return pair;
    }

    private boolean checkX(boolean ab, boolean ba, boolean ac, boolean ca) {

        // X1
        if (ab && ba && !ac && !ca) {
            return true;
        }
        // X2
        else if (ab && ba && !ac && ca) {
            return true;
        }
        // X3
        else if (ab && !ba && !ac && !ca) {
            return true;
        }
        // X4
        else if (ab && !ba && !ac && ca) {
            return true;
        }
        return false;
    }

    private boolean[] checkEdges(Set<DiEdge> edges, Node node1, Node node2, Node node3) {

        boolean[] bools = new boolean[] {
                false, false, false, false
        };

        // forbidden pairs
        // cb
        DiEdge forbiddenEdge1 = new DiEdge(node3, node2);
        if (edges.contains(forbiddenEdge1)) {
            edges.remove(forbiddenEdge1);
            return bools;
        }
        // bc
        DiEdge forbiddenEdge2 = new DiEdge(node2, node3);
        if (edges.contains(forbiddenEdge2)) {
            edges.remove(forbiddenEdge2);
            return bools;
        }

        // necessary pairs
        // ab (0)
        if (bools[0] == false) {
            DiEdge e = new DiEdge(node1, node2);
            if (edges.contains(e)) {
                edges.remove(e);
                bools[0] = true;
            }
        }
        // ba (1)
        if (bools[1] == false) {
            DiEdge e = new DiEdge(node2, node1);
            if (edges.contains(e)) {
                edges.remove(e);
                bools[1] = true;
            }
        }
        // ac (2)
        if (bools[2] == false) {
            DiEdge e = new DiEdge(node1, node3);
            if (edges.contains(e)) {
                edges.remove(e);
                bools[2] = true;
            }
        }
        // ca (3)
        if (bools[3] == false) {
            DiEdge e = new DiEdge(node3, node1);
            if (edges.contains(e)) {
                edges.remove(e);
                bools[3] = true;
            }
        }

        return bools;
    }

}
