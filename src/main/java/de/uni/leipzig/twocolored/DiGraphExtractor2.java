package de.uni.leipzig.twocolored;

import java.util.*;

import de.uni.leipzig.model.*;
import de.uni.leipzig.model.edges.DiEdge;

public class DiGraphExtractor2 {

    private Set<Node> dgNodes = new HashSet<>();

    private Set<DiEdge> dgEdges = new HashSet<>();

    private List<Set<Node>> LabelSets = new ArrayList<>();

    private Set<Node> nodes = new HashSet<>();

    private Set<DiEdge> edges = new HashSet<>();

    public DiGraph extract(AdjacencyList adjList) {

        // sort all Leaves to seperated lists
        outerloop: for (List<Node> list : adjList) {

            if (!isLeave(list))
                continue;

            Node leave = list.get(0);
            Color label = leave.getColor();

            dgNodes.add(leave);

            if (LabelSets.isEmpty()) {
                LabelSets.add(new HashSet<>());
                LabelSets.get(0).add(leave);
                continue;
            }

            for (Set<Node> set : LabelSets) {
                for (Node n : set) {
                    if (n.getColor().equals(label)) {
                        set.add(leave);
                        continue outerloop;
                    }
                }
            }
            LabelSets.add(new HashSet<>());
            LabelSets.get(LabelSets.size() - 1).add(leave);
        }

        // printer
        for (Set<Node> set : LabelSets) {
            System.out.println("EIN NEUES SET !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            for (Node n : set) {
                System.out.println(n);
            }
        }

        // find for each leave the lca's of every other label with the lowest
        // ancesterorder
        for (Set<Node> setA : LabelSets) {
            System.out.println("start neues Set");
            for (Node node1 : setA) {
                System.out.println("node1: " + node1);
                for (Set<Node> setB : LabelSets) {

                    if (setA == setB)
                        continue;

                    Set<Node> lowestLCAs = new HashSet<>(findLowestLCAs(node1, setB));
                    System.out.println("list of lcas: " + lowestLCAs);
                    for (Node node2 : lowestLCAs) {
                        dgEdges.add(new DiEdge(node1, node2));
                        // System.out.println("all DiEdges: " + dgEdges);
                    }
                }
            }
        }
        System.out.println(dgEdges);
        return new DiGraph(dgNodes, dgEdges);
    }

    private Set<Node> findLowestLCAs(Node node1, Set<Node> setB) {

        Set<Node> LCAs = new HashSet<>();
        int bar = 0;

        if (setB.size() <= 1) {
            return LCAs;
        }

        for (Node node2 : setB) {

            char[] first = node1.getPath().toCharArray();
            char[] second = node2.getPath().toCharArray();

            int minLength = Math.min(first.length, second.length);

            int counter = 0;
            for (int i = 0; i < minLength; i++) {
                if (first[i] == second[i]) {
                    counter++;
                } else {
                    break;
                }
            }
            if (counter > bar) {
                LCAs.clear();
                LCAs.add(node2);
                bar = counter;
            } else if (counter == bar) {
                LCAs.add(node2);
            }
        }
        return LCAs;
    }

    private boolean isLeave(List<Node> nodes) {
        return nodes.size() == 1;
    }
}
