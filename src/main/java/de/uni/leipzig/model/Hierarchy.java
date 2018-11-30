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

        // find root node (set with largest size)
        HashSet<Set<Node>> remainingSets = Sets.newHashSet(sets);
        Optional<Set<Node>> rootSet = remainingSets.stream()
                .max(Comparator.comparing(Set::size));

        if (!rootSet.isPresent())
            throw new IncorrectHierarchyException("unable to determine root set");

        Tree finalTree = recursiveTopDown(finalEdges, rootSet.get());

        return finalTree;
    }

    private Tree recursiveTopDown(Set<SetDiEdge> finalEdges, Set<Node> parentNodeSet) {

        // find all edges that have this node set as target
        Predicate<? super Set<Node>> filter = t -> Util.equalSets(t, parentNodeSet);

        Set<Set<Node>> nodesPointingToTarget = Util.filterAndReduce(finalEdges,
                SetDiEdge::getSecond, filter, SetDiEdge::getFirst);

        // if parent node set is a leave, return its nodes as a tree
        if (nodesPointingToTarget.isEmpty())
            return Util.nodeSetToLeafTree(parentNodeSet);

        // get all nodes not contained by one of the subsets (all leaves)
        Set<Set<Node>> leaves = nodesPointingToTarget.stream()
                .reduce(parentNodeSet, Sets::difference)
                .stream()
                .map(n -> Sets.newHashSet(n))
                .collect(Collectors.toSet());
        leaves.forEach(nodesPointingToTarget::add);

        // if there is still only one child, this is an incorrect tree
        if (nodesPointingToTarget.size() == 1)
            throw new IncorrectHierarchyException(
                    "every tree in the hierarchy must have zero or at least 2 children");

        // create new tree
        Tree parentTree = new Tree();

        // run recursively for all pointing sets
        for (Set<Node> pointingNode : nodesPointingToTarget) {
            Tree child = recursiveTopDown(finalEdges, pointingNode);
            parentTree.addSubTree(child);
        }

        return parentTree;
    }

    private Set<SetDiEdge> getTransitiveClosure(Set<Set<Node>> sets) {
        Set<SetDiEdge> edges = new HashSet<>();

        for (Set<Node> set : sets) {
            for (Set<Node> another : sets) {

                if (set == another)
                    continue;

                if (another.size() > set.size() && another.containsAll(set))
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
