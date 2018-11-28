package de.uni.leipzig.twocolored;

import static de.uni.leipzig.Util.*;

import java.util.*;

import de.uni.leipzig.Util.LcaForTwoNodes;
import de.uni.leipzig.model.*;
import de.uni.leipzig.model.edges.DiEdge;

public class DiGraphExtractor {

    public DiGraph extract(AdjacencyList adjList) {

        Set<DiEdge> edges = new HashSet<>();
        Set<Node> children = adjList.getChildNodes();

        for (Node firstNode : children) {

            List<LcaForTwoNodes> lcaCandidates = children.stream()
                    .filter(n -> !firstNode.equals(n)
                            && !firstNode.getColor().equals(n.getColor()))
                    .map(n -> new LcaForTwoNodes(firstNode, n, findLCA(firstNode, n).size()))
                    .collect(maxList(Comparator.comparing(LcaForTwoNodes::getLcaPath)));

            if (lcaCandidates.size() > 1)
                lcaCandidates.stream()
                        .map(lca -> new DiEdge(lca.getA(), lca.getB()))
                        .forEach(edges::add);
        }

        return new DiGraph(children, edges);

    }

}
