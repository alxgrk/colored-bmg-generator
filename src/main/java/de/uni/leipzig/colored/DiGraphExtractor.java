package de.uni.leipzig.colored;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.edges.DiEdge;

public class DiGraphExtractor {

    public DiGraph extract(List<List<Node>> adjList) {

        Set<Node> nodes = new HashSet<>();
        Set<DiEdge> edges = new HashSet<>();

        for (int i = 0; i < adjList.size(); i++) {

            List<Node> firstList = adjList.get(i);

            if (!isLeave(firstList)) {
                continue;
            }

            Node firstNode = firstList.get(0);
            // add every leave
            nodes.add(firstNode);

            for (int j = 0; j < adjList.size(); j++) {

                List<Node> secondList = adjList.get(j);

                if (!isLeave(secondList) || j == i) {
                    continue;
                }

                Node secondNode = secondList.get(0);

                for (int k = 0; k < adjList.size(); k++) {

                    List<Node> thirdList = adjList.get(k);

                    if (isLeave(thirdList) && k != j && k != i) {

                        Node thirdNode = thirdList.get(0);

                        if (firstNode.getLabel() != secondNode.getLabel() &&
                                (firstNode.getLabel() == thirdNode.getLabel() || secondNode
                                        .getLabel() == thirdNode.getLabel())) {

                            List<Integer> x = firstNode.getIds();
                            List<Integer> y = secondNode.getIds();
                            List<Integer> z = thirdNode.getIds();

                            List<Integer> lcaXY = findLCA(x, y);
                            List<Integer> lcaXZ = findLCA(x, z);

                            // xy is the ingroup, z is the outgroup
                            if (lcaXY.size() > lcaXZ.size()) {

                                DiEdge edge;

                                if (firstNode.getLabel() == thirdNode.getLabel()) {
                                    edge = new DiEdge(secondNode, firstNode);
                                } else {
                                    edge = new DiEdge(firstNode, secondNode);
                                }
                                edges.add(edge);
                            }
                        }
                    }
                }
            }
        }
        return new DiGraph(nodes, edges);
    }

    private boolean isLeave(List<Node> nodes) {
        return nodes.size() == 1;
    }

    private List<Integer> findLCA(List<Integer> a, List<Integer> b) {

        List<Integer> ancester = new ArrayList<>();

        int v = 0;

        while (a.get(v) == b.get(v)) {

            ancester.add(a.get(v));
            if (v == a.size() - 1 || v == b.size() - 1) {
                return ancester;
            }
            v++;
        }
        return ancester;
    }
}
