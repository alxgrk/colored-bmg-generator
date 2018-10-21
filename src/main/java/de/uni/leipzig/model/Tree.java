package de.uni.leipzig.model;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import lombok.*;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class Tree implements Comparable<Tree> {

    Node root;

    List<Tree> subTrees;

    public Tree(@NonNull Node root) {
        this(root, new ArrayList<>());
    }

    public Tree(@NonNull Tree... subtrees) {
        this(Node.helpNode(), subtrees);
    }

    public Tree(@NonNull List<Tree> subtrees) {
        this(Node.helpNode(), subtrees);
    }

    public Tree(@NonNull Node root, @NonNull Tree... subtrees) {
        this(root, Lists.newArrayList(subtrees));
    }

    public Tree(@NonNull Node root, @NonNull List<Tree> treeList) {
        this.root = root;
        this.subTrees = treeList;
        if (!subTrees.isEmpty())
            Collections.sort(this.subTrees);
    }

    public List<Node> getDirectChildren() {
        return subTrees.stream()
                .map(Tree::getRoot)
                .collect(Collectors.toList());
    }

    public List<Node> getLeafs() {
        List<Node> leafs = new ArrayList<>();

        leafs.add(root);
        leafs.addAll(subTrees.stream()
                .flatMap(t -> t.getLeafs().stream())
                .collect(Collectors.toList()));

        return leafs.stream()
                .filter(n -> !n.isHelpNode())
                .collect(Collectors.toList());
    }

    public List<Node> getAllNodes() {
        List<Node> nodes = new ArrayList<>();

        nodes.add(root);

        subTrees.forEach(t -> {
            nodes.addAll(t.getAllNodes());
        });

        return nodes;
    }

    public Tree addSubTree(@NonNull Tree t) {

        subTrees.removeIf(st -> t.getLeafs().containsAll(st.getLeafs()));

        subTrees.add(t);
        Collections.sort(this.subTrees);

        return this;
    }

    public String toNewickNotation() {
        if (subTrees.isEmpty())
            return root.toString();

        StringBuilder sb = new StringBuilder("(");

        if (!root.isHelpNode())
            sb.append(root.toString()).append(",");

        sb = subTrees.stream()
                .map(Tree::toNewickNotation)
                .reduce(sb,
                        (i, s) -> i.append(s).append(","),
                        (sb1, sb2) -> sb1.append(sb2));

        return sb.deleteCharAt(sb.length() - 1)
                .append(")")
                .toString();
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        print(sb, "", true);
        return sb.toString();
    }

    private void print(StringBuilder sb, String prefix, boolean isTail) {
        String node = root.toString();

        sb.append(prefix)
                .append(isTail ? "└── " : "├── ")
                .append(node)
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

    @Override
    public int compareTo(Tree o) {
        List<Node> thisNodes = this.getLeafs();
        List<Node> otherNodes = o.getLeafs();

        if (thisNodes.isEmpty())
            return 1;
        if (otherNodes.isEmpty())
            return -1;

        return thisNodes.get(0).compareTo(otherNodes.get(0));
    }

}
