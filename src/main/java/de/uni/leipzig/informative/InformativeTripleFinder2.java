package de.uni.leipzig.informative;

import java.util.*;

import org.jgrapht.alg.util.Pair;

import com.google.common.collect.Lists;

import de.uni.leipzig.informative.model.InformativeTriple;
import de.uni.leipzig.model.*;
import de.uni.leipzig.model.edges.*;

public class InformativeTripleFinder2 {

    public Pair<Set<InformativeTriple>, Set<Node>> findTriple(DiGraph graph) {

        Set<InformativeTriple> foundTripleSet = new HashSet<>();
        Set<Node> foundNodes = new HashSet<>();

        Set<DiEdge> edges = graph.getEdges();

        for (Node node1 : graph.getNodes()) {
            for (Node node2 : graph.getNodes()) {
                if (node1.equals(node2) || node1.getColor().equals(node2.getColor()))
                    continue;

                for (Node node3 : graph.getNodes()) {

                    if (node1.equals(node3) || node2.equals(node3))
                        continue;

                    Pair<Node, Node> ab = new Pair<>(node1, node2);
                    Pair<Node, Node> ac = new Pair<>(node1, node3);
                    Pair<Node, Node> bc = new Pair<>(node2, node3);
                    Pair<Node, Node> ba = new Pair<>(node2, node1);
                    Pair<Node, Node> ca = new Pair<>(node3, node1);
                    Pair<Node, Node> cb = new Pair<>(node3, node2);

                    List<Boolean> edgesExistent = checkEdges(edges,
                            Lists.newArrayList(cb, bc),
                            Lists.newArrayList(ab, ba, ac, ca));

                    if (!edgesExistent.contains(true))
                        continue;

                    if (edgesExistent.size() == 4
                            && checkX(edgesExistent.get(0), edgesExistent.get(1),
                                    edgesExistent.get(2), edgesExistent.get(3))) {

                        Edge edge = new Edge(node1, node2);
                        Node node = node3;

                        InformativeTriple foundInformativeTriple = new InformativeTriple(edge,
                                node);
                        foundTripleSet.add(foundInformativeTriple);

                        foundNodes.add(node1);
                        foundNodes.add(node2);
                        foundNodes.add(node3);
                    }

                }
            }
        }

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

    private List<Boolean> checkEdges(Set<DiEdge> edges, List<Pair<Node, Node>> forbiddenPairs,
            List<Pair<Node, Node>> necessaryPairs) {

        List<Boolean> list = Arrays.asList(new Boolean[] {
                false, false, false, false
        });

        for (DiEdge edge : edges) {
            Pair<Node, Node> pair = new Pair<>(edge.getFirst(), edge.getSecond());

            if (forbiddenPairs.contains(pair))
                return list;

            for (int i = 0; i < necessaryPairs.size(); i++) {

                if (list.get(i) == true)
                    continue;

                Pair<Node, Node> neededPair = necessaryPairs.get(i);

                if (neededPair.getFirst().equals(edge.getFirst())
                        && neededPair.getSecond().equals(edge.getSecond())) {
                    list.set(i, true);
                }

            }

        }
        return list;
    }

}
