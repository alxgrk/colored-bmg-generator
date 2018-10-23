package de.uni.leipzig.model;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.*;

import de.uni.leipzig.Util;
import de.uni.leipzig.model.edges.SetDiEdge;
import lombok.Value;

@Value
public class Hierarchy {
    Set<Set<Node>> sets;

    public Hierarchy(Map<ThinnessClass, Reachables> reachablesByTc) {
        ImmutableSet.Builder<Set<Node>> setBuilder = ImmutableSet.builder();

        reachablesByTc.values()
                .stream()
                .map(Reachables::getRq)
                .filter(rq -> !rq.isEmpty())
                .forEach(rq -> setBuilder.add(ImmutableSet.copyOf(rq)));

        ImmutableSet.Builder<Node> parentSetBuilder = ImmutableSet.builder();
        reachablesByTc.keySet()
                .forEach(tc -> {
                    parentSetBuilder.addAll(tc.getNodes());
                });
        setBuilder.add(parentSetBuilder.build());

        sets = setBuilder.build();
    }

    public Tree toHasseTree() {

        // determine the transitive closure ("Transitive Hülle")
        Set<SetDiEdge> edges = getTransitiveClosure(sets);

        // count the in-going edges for each set
        Map<Set<Node>, Integer> inNeighboursCount = countIngoingNeighbours(edges);

        // group by first node set
        Map<Set<Node>, List<SetDiEdge>> groupedByFirstNodeSet = edges.stream()
                .collect(Collectors.groupingBy(SetDiEdge::getFirst));

        // only use edges for a node set, that point to its direct parent
        Set<SetDiEdge> finalEdges = filterByInNeighbourCount(inNeighboursCount,
                groupedByFirstNodeSet);

        Tree finalTree = recursive(finalEdges, Sets.newHashSet(sets), new HashSet<>());

        return finalTree;
    }

    private Tree recursive(Set<SetDiEdge> finalEdges, Set<Set<Node>> remainingSets,
            Set<Tree> leafsAsTree) {

        // special case
        if (finalEdges.size() == 0 && remainingSets.size() == 0)
            return leafsAsTree.iterator().next();

        // special case: see DiGraphTest.testFailingHierarchy
        if (finalEdges.size() == 1 && leafsAsTree.size() > 1) {
            return leafsAsTree.stream()
                    .filter(l -> {
                        Set<Node> rootNodes = finalEdges.iterator().next().getSecond();

                        return Util.equalSets(l.getLeafs(), rootNodes);
                    })
                    .findFirst()
                    .orElseThrow(() -> new IncorrectHierarchyException(
                            "last edge pointed to non existing node set"));
        }

        if (remainingSets.size() == 1) {
            Tree finalTree = Util.nodeSetToLeafTree(Lists.newArrayList(remainingSets).get(0));

            leafsAsTree.forEach(finalTree::addSubTree);

            return finalTree;
        }

        if (!leafsAsTree.isEmpty()) {
            Set<Tree> tmpTrees = Sets.newHashSet(leafsAsTree);

            leafsAsTree.stream()
                    .filter(t -> edgeWithLeafAsFirst(finalEdges, t, false).isPresent())
                    .forEach(t -> {
                        Set<Node> newLeafSet = edgeWithLeafAsFirst(finalEdges, t, true)
                                .get()
                                .getSecond();

                        Optional<Tree> existingTmpTree = tmpTrees.stream()
                                .filter(tmp -> Util.equalSets(tmp.getLeafs(), newLeafSet))
                                .findFirst();

                        Tree newLeaf;
                        if (existingTmpTree.isPresent())
                            newLeaf = existingTmpTree.get();
                        else {
                            newLeaf = Util.nodeSetToLeafTree(newLeafSet);
                            tmpTrees.add(newLeaf);
                            remainingSets.removeIf(s -> Util.equalSets(s, newLeafSet));
                        }
                        newLeaf.addSubTree(t);
                        tmpTrees.remove(t);
                    });

            leafsAsTree = tmpTrees;
        } else {

            // get all sets, where this set IS NEVER the second component of an edges - this means,
            // that there are only outgoing edges in the transitive closure
            Set<Set<Node>> leafs = remainingSets.stream()
                    .filter(s -> finalEdges.stream()
                            .noneMatch(e -> e.getSecond() == s))
                    .collect(Collectors.toSet());

            if (leafs.isEmpty())
                throw new IncorrectHierarchyException("there must be at least one leaf set");

            leafsAsTree = leafs.stream()
                    .peek(remainingSets::remove)
                    .map(Util::nodeSetToLeafTree)
                    .collect(Collectors.toSet());
        }

        return recursive(finalEdges, remainingSets, leafsAsTree);
    }

    private Optional<SetDiEdge> edgeWithLeafAsFirst(Set<SetDiEdge> finalEdges, Tree t,
            boolean andRemove) {
        Predicate<? super SetDiEdge> treeEqualFirstOfEdge = e -> Util.equalSets(e
                .getFirst(), t.getLeafs());

        Optional<SetDiEdge> first = finalEdges.stream()
                .filter(treeEqualFirstOfEdge)
                .findFirst();

        if (andRemove)
            finalEdges.removeIf(treeEqualFirstOfEdge);

        return first;
    }

    private Set<SetDiEdge> getTransitiveClosure(Set<Set<Node>> sets) {
        Set<SetDiEdge> edges = new HashSet<>();

        for (Set<Node> set : sets) {
            for (Set<Node> another : sets) {

                if (set == another)
                    continue;

                if (Util.properSubset(set, another))
                    edges.add(new SetDiEdge(set, another));
            }
        }

        return edges;
    }

    private Map<Set<Node>, Integer> countIngoingNeighbours(Set<SetDiEdge> edges) {
        Map<Set<Node>, Integer> inNeighboursCount = new HashMap<>();

        for (SetDiEdge setDiEdge : edges) {
            Set<Node> withInNeighbour = setDiEdge.getSecond();
            inNeighboursCount.compute(withInNeighbour, (k, v) -> v == null ? 1 : ++v);
        }

        return inNeighboursCount;
    }

    private Set<SetDiEdge> filterByInNeighbourCount(Map<Set<Node>, Integer> inNeighboursCount,
            Map<Set<Node>, List<SetDiEdge>> groupedByFirstNodeSet) {
        return groupedByFirstNodeSet.entrySet()
                .stream()
                .map(e -> {
                    List<SetDiEdge> possibleParents = e.getValue();

                    // find the parent with the lowest number of in-going edges
                    Optional<Set<Node>> possibleDirectParent = possibleParents.stream()
                            .map(SetDiEdge::getSecond)
                            .min((s1, s2) -> inNeighboursCount.get(s1)
                                    .compareTo(inNeighboursCount.get(s2)));

                    // quit, when there wasn't exactly one parent
                    Set<Node> directParent = possibleDirectParent.orElseThrow(
                            () -> new IncorrectHierarchyException(
                                    "node set in hierachy without direct parent"));

                    return new SetDiEdge(e.getKey(), directParent);
                })
                .collect(Collectors.toSet());
    }

    static class IncorrectHierarchyException extends RuntimeException {

        public IncorrectHierarchyException(String reason) {
            super(reason);
        }

    }
}
