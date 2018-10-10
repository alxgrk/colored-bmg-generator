package de.uni.leipzig.twocolored;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.ThinnessClass;
import de.uni.leipzig.model.edges.DiEdge;

public class ThinnessClassFinder {

    private Set<ThinnessClass> thinnessClasses = new HashSet<ThinnessClass>();

    private Map<Node, List<Node>> outerN = new HashMap<>();

    private Map<Node, List<Node>> innerN = new HashMap<>();

    public Set<ThinnessClass> findFrom(DiGraph graph) {

        // erstelle listen-listen mit inneren und äußeren Nachbarn

        for (Node n : graph.getNodes()) {

            outerN.put(n, new ArrayList<>());
            innerN.put(n, new ArrayList<>());

            for (DiEdge e : graph.getEdges()) {

                if (n.equals(e.getFirst())) {
                    List<Node> nodeOuterN = outerN.get(n);
                    nodeOuterN.add(e.getSecond());
                } else if (n.equals(e.getSecond())) {
                    List<Node> nodeInnerN = innerN.get(n);
                    nodeInnerN.add(e.getFirst());
                }
            }

        }

        // finde Äquivalenzklassen

        for (Node n : graph.getNodes()) {

            if (nodeAlreadyInTc(n))
                continue;

            ThinnessClass tc = new ThinnessClass();
            tc.add(n);

            for (Node nextNode : graph.getNodes()) {

                if (n == nextNode)
                    continue;

                if (outerN.get(n).containsAll(outerN.get(nextNode)) && outerN.get(nextNode)
                        .containsAll(outerN.get(n))
                        && innerN.get(n).containsAll(innerN.get(nextNode)) && innerN.get(nextNode)
                                .containsAll(innerN.get(n))) {
                    tc.add(nextNode);
                }
            }

            thinnessClasses.add(tc);
        }

        return thinnessClasses;
    }

    private boolean nodeAlreadyInTc(Node n) {
        boolean skip = false;
        for (ThinnessClass tc : thinnessClasses) {
            skip = tc.contains(n);
            if (skip)
                break;
        }
        return skip;
    }

}
