package de.alxgrk.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import lombok.Value;

@Value
public class Tree {

    List<Tree> subTrees;

    List<Node> leafs;

    public Tree(List<Node> leafs) {
        if (leafs.size() < 1)
            throw new IllegalArgumentException("Unable to create tree without any node.");

        this.leafs = leafs;
        this.subTrees = new ArrayList<>();
    }

    public Tree(Tree... subtrees) {
        if (subtrees.length < 1)
            throw new IllegalArgumentException("Unable to create tree without any node.");

        this.leafs = new ArrayList<>();
        this.subTrees = Arrays.asList(subtrees);
    }

    public Tree(TreeSet<Node> leafs) {
        this(new ArrayList<>(leafs));
    }

    public List<Node> getNodes() {
        List<Node> nodes = new ArrayList<>();

        nodes.addAll(leafs);

        subTrees.forEach(t -> {
            nodes.addAll(t.getNodes());
        });

        return nodes;
    }

    public Tree addSubTree(Tree t) {
        subTrees.add(t);
        return this;
    }

    public String toNewickNotation() {
        if (subTrees.isEmpty() && leafs.size() == 1)
            return leafs.get(0).toString();

        String nodes = leafs.stream()
                .filter(Node::isVisible)
                .map(Node::toString)
                .reduce("(", (u, n) -> u + n + ",");

        String result = subTrees.stream()
                .map(Tree::toNewickNotation)
                .reduce(nodes, (i, s) -> i + s + ",");

        return result
                .substring(0, result.length() - 1)
                .concat(")");
    }

    @Override
    public String toString() {
        return toNewickNotation();
    }

}
