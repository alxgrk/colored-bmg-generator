package de.uni.leipzig.conversion;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.*;

import de.uni.leipzig.Util;
import de.uni.leipzig.model.*;
import de.uni.leipzig.model.edges.Edge;

public class TripleFromTree {

    private Multimap<Node, List<Node>> parentLeafSetsPerLeaf = LinkedListMultimap.create();

    public Set<Triple> extractOf(Map<Pair<Color>, Tree> stTrees) {
        return stTrees.values()
                .stream()
                .flatMap(t -> treeToTriples(t).stream())
                .collect(Collectors.toSet());
    }

    public Set<Triple> treeToTriples(Tree tree) {

        List<Node> allLeafs = tree.getLeafs();
        allLeafs.forEach(n -> parentLeafSetsPerLeaf.put(n, allLeafs));
        fillMultiMap(tree);

        Set<Triple> triples = Sets.newHashSet();

        parentLeafSetsPerLeaf.asMap().forEach((a, va) -> {

            parentLeafSetsPerLeaf.asMap().forEach((b, vb) -> {
                if (a.equals(b))
                    return;

                if (a.getColor().equals(b.getColor()))
                    return;

                Optional<List<Node>> lcaOpt = findLca(va, vb);

                parentLeafSetsPerLeaf.asMap().forEach((c, vc) -> {
                    if (a.equals(c) || b.equals(c))
                        return;

                    // TODO is this correct?
                    // currently excludes all same-colored / all-distinct-colored triples
                    if ((a.getColor().equals(c.getColor()) && b.getColor().equals(c.getColor()))
                            || (!a.getColor().equals(c.getColor()) && !b.getColor()
                                    .equals(c.getColor())))
                        return;

                    lcaOpt.ifPresent(lca -> {
                        for (List<Node> cParent : vc) {
                            if (Util.properSubset(lca, cParent))
                                triples.add(new DefaultTriple(new Edge(a, b), c));
                        }
                    });
                });
            });

        });

        parentLeafSetsPerLeaf.clear();

        return triples;
    }

    private void fillMultiMap(Tree tree) {

        for (Tree subTree : tree.getSubTrees()) {
            List<Node> leafs = subTree.getLeafs();

            // skip leaf trees
            if (leafs.size() > 1) {
                for (Node node : leafs) {
                    parentLeafSetsPerLeaf.put(node, leafs);
                }
            }

            fillMultiMap(subTree);
        }
    }

    private Optional<List<Node>> findLca(Collection<List<Node>> va, Collection<List<Node>> vb) {
        Set<List<Node>> parentLeafSetsA = Sets.newHashSet(va);
        Set<List<Node>> parentLeafSetsB = Sets.newHashSet(vb);

        Optional<List<Node>> lca = Sets.intersection(parentLeafSetsA, parentLeafSetsB)
                .stream()
                .min((l1, l2) -> Integer.compare(l1.size(), l2.size()));

        return lca;
    }
}
