package de.uni.leipzig.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import de.uni.leipzig.Util;
import lombok.NonNull;
import lombok.Value;

@Value
public class Tree {

    List<Tree> subTrees;

    List<Node> leafs;

    public Tree(@NonNull Collection<Node> leafs) {
        List<Node> leafsAsList = Lists.newArrayList(leafs);
        leafsAsList.sort(Util.nodeByPathComparator);

        if (leafs.size() < 1)
            throw new IllegalArgumentException("Unable to create tree without any node.");

        this.leafs = leafsAsList;
        this.subTrees = new ArrayList<>();
    }

    public Tree(@NonNull Tree... subtrees) {
        if (subtrees.length < 1)
            throw new IllegalArgumentException("Unable to create tree without any node.");

        this.leafs = new ArrayList<>();
        this.subTrees = Lists.newArrayList(subtrees);
    }

    public List<Node> getNodes() {
        List<Node> nodes = new ArrayList<>();

        nodes.addAll(leafs);

        subTrees.forEach(t -> {
            nodes.addAll(t.getNodes());
        });

        return nodes;
    }

    public Tree addSubTree(@NonNull Tree t) {
        leafs.removeAll(t.getNodes());
        subTrees.add(t);

        return this;
    }

    public String toNewickNotation() {
        if (subTrees.isEmpty() && leafs.size() == 1)
            return leafs.get(0).toString();

        String nodes = leafs.stream()
                .filter(n -> !n.isHelpNode())
                .map(Node::toString)
                .reduce("(", (u, n) -> u + n + ",");

        String result = subTrees.stream()
                .map(Tree::toNewickNotation)
                .reduce(nodes, (i, s) -> i + s + ",");

        return result
                .substring(0, result.length() - 1)
                .concat(")");
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        print(sb, "", true);
        return sb.toString();
    }

    private void print(StringBuilder sb, String prefix, boolean isTail) {
        String nodes = leafs.stream()
                .filter(n -> !n.isHelpNode())
                .map(Node::toString)
                .reduce("", (u, n) -> u + n + ",");
        nodes = !nodes.isEmpty() ? nodes.substring(0, nodes.length() - 1) : nodes;

        sb.append(prefix)
                .append(isTail ? "└── " : "├── ")
                .append(nodes.isEmpty() ? "*" : nodes)
                .append("\n");
        for (int i = 0; i < subTrees.size() - 1; i++) {
            subTrees.get(i).print(sb, prefix + (isTail ? "    " : "│   "), false);
        }
        if (subTrees.size() > 0) {
            subTrees.get(subTrees.size() - 1)
                    .print(sb, prefix + (isTail ? "    " : "│   "), true);
        }
    }

    @Override
    public String toString() {
        return toNewickNotation();
    }

}
