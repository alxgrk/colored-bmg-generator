package de.uni.leipzig.twocolored;

import static java.util.stream.Collectors.*;

import java.util.*;

import de.uni.leipzig.model.*;
import de.uni.leipzig.model.edges.DiEdge;

public class ThinnessClassFinder {

    public Set<ThinnessClass> findFrom(DiGraph graph) {

        Set<ThinnessClass> thinnessClasses = new HashSet<>();

        // erstelle listen-listen mit inneren und äußeren Nachbarn

        Map<Node, Set<Node>> outN = graph.getEdges()
                .stream()
                .collect(groupingBy(DiEdge::getFirst,
                        mapping(DiEdge::getSecond, toSet())));
        Map<Node, Set<Node>> inN = graph.getEdges()
                .stream()
                .collect(groupingBy(DiEdge::getSecond,
                        mapping(DiEdge::getFirst, toSet())));

        // finde Äquivalenzklassen

        for (Node n : graph.getNodes()) {

            if (nodeAlreadyInTc(thinnessClasses, n))
                continue;

            Set<Node> nOut = outN.getOrDefault(n, new HashSet<>());
            Set<Node> nIn = inN.getOrDefault(n, new HashSet<>());

            ThinnessClass tc = new ThinnessClass(nOut, nIn);
            tc.add(n);

            for (Node nextNode : graph.getNodes()) {

                if (n == nextNode)
                    continue;

                Set<Node> nextOut = outN.getOrDefault(nextNode, new HashSet<>());
                Set<Node> nextIn = inN.getOrDefault(nextNode, new HashSet<>());

                if (nOut.containsAll(nextOut)
                        && nextOut.containsAll(nOut)
                        && nIn.containsAll(nextIn)
                        && nextIn.containsAll(nIn)) {
                    tc.add(nextNode);
                }
            }

            thinnessClasses.add(tc);
        }

        return thinnessClasses;
    }

    private boolean nodeAlreadyInTc(Set<ThinnessClass> thinnessClasses, Node n) {
        boolean skip = false;
        for (ThinnessClass tc : thinnessClasses) {
            skip = tc.contains(n);
            if (skip)
                break;
        }
        return skip;
    }

}
