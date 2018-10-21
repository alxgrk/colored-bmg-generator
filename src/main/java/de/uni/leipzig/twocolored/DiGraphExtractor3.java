package de.uni.leipzig.twocolored;

import java.util.*;

import de.uni.leipzig.model.*;
import de.uni.leipzig.model.edges.DiEdge;

public class DiGraphExtractor3 {

    private Set<DiEdge> dgEdges;

    private Set<Node> dgNodes;

    private Node reference;

    private Node x;

    private Node y;

    public DiGraph diGraphfromTree(Tree tree) {
        Tree reducedTree = extractAndReduce(tree);
        extractRemained(reducedTree);

        DiGraph graph = new DiGraph(dgNodes, dgEdges);

        return graph;
    }

    private void extractRemained(Tree tree) {
        List<Tree> SubTreeList = tree.getSubTrees();
        List<Node> nodeList = tree.getAllNodes();

        if (reference == null) {
            if (!nodeList.isEmpty()) {
                for (Node node : nodeList) {
                    reference = node;
                    for (Tree subTree : SubTreeList) {
                        extractRemained(subTree);
                    }
                }
            }
            for (Tree subTree : SubTreeList) {
                extractRemained(subTree);
            }
        }

        if (x == null) {
            if (!nodeList.isEmpty()) {
                for (int i = 0; i < nodeList.size(); i++) {
                    x = nodeList.get(i);
                    for (int j = 0; j < nodeList.size(); j++) {
                        if (i == j)
                            continue;
                        y = nodeList.get(j);
                        checkAndExtract(reference, x, y);
                    }
                }
            }
            for (Tree subTree : SubTreeList) {
                extractRemained(subTree);
            }
        }
        if (y == null) {
            if (!nodeList.isEmpty()) {
                for (Node node : nodeList) {
                    y = node;
                    checkAndExtract(reference, x, y);
                }
            }
            for (Tree subTree : SubTreeList) {
                extractRemained(subTree);
            }
        }
    }

    private Tree extractAndReduce(Tree tree) {
        List<Tree> SubTreeList = tree.getSubTrees();
        List<Node> nodeList = tree.getAllNodes();

        List<Node> reducedNodeList = extractEdgesInSubTree(nodeList);

        if (!SubTreeList.isEmpty()) {
            for (Tree subTree : SubTreeList) {
                extractAndReduce(subTree);
            }
        }
        Tree reducedTree = new Tree(SubTreeList);

        return reducedTree;

    }

    private List<Node> extractEdgesInSubTree(List<Node> leaves) {
        for (int i = 0; i < leaves.size(); i++) {
            Node refNode = leaves.get(i);
            for (int j = 0; j < leaves.size(); j++) {
                if (i == j)
                    continue;
                Node x = leaves.get(j);
                for (int k = 0; k < leaves.size(); k++) {
                    Node y = leaves.get(k);
                    if (k == i || k == j || x.getColor().equals(y.getColor()))
                        continue;
                    checkAndExtract(refNode, x, y);
                    leaves.remove(refNode);
                }
            }
        }
        return leaves;
    }

    private void checkAndExtract(Node reference, Node x, Node y) {
        if (x.getColor().equals(reference)) {
            dgEdges.add(new DiEdge(y, x));
        } else {
            dgEdges.add(new DiEdge(x, y));
        }
    }
}
