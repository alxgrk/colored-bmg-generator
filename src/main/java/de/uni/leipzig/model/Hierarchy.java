package de.uni.leipzig.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uni.leipzig.Util;
import de.uni.leipzig.model.edges.SetDiEdge;
import lombok.Value;

@Value
public class Hierarchy {
    Set<Set<Node>> sets;

    public Hierarchy(Map<EquivalenceClass, Reachables> reachablesByÄk) {
        ImmutableSet.Builder<Set<Node>> setBuilder = ImmutableSet.builder();

        reachablesByÄk.values()
                .stream()
                .map(Reachables::getRq)
                .filter(rq -> !rq.isEmpty())
                .forEach(rq -> setBuilder.add(ImmutableSet.copyOf(rq)));

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

        if (remainingSets.size() == 1) {
            Tree root = new Tree(Lists.newArrayList(remainingSets).get(0));

            leafsAsTree.forEach(root::addSubTree);

            return root;
        }

        if (!leafsAsTree.isEmpty()) {
            leafsAsTree = leafsAsTree.stream()
                    .map(t -> {
                        Predicate<? super SetDiEdge> treeEqualFirstOfEdge = e -> Util.equalSets(e
                                .getFirst(), t.getNodes());

                        SetDiEdge edgeWithLeafAsFirst = finalEdges.stream()
                                .filter(treeEqualFirstOfEdge)
                                .findFirst()
                                .get();

                        finalEdges.removeIf(treeEqualFirstOfEdge);

                        Set<Node> newLeafSet = edgeWithLeafAsFirst.getSecond();
                        Tree newLeaf = new Tree(newLeafSet);
                        newLeaf.addSubTree(t);
                        remainingSets.removeIf(s -> Util.equalSets(s, newLeafSet));

                        return newLeaf;
                    })
                    .collect(Collectors.toSet());
        } else {

            Set<Set<Node>> leafs = remainingSets.stream()
                    .filter(s -> finalEdges.stream()
                            .noneMatch(e -> e.getSecond() == s))
                    .collect(Collectors.toSet());

            if (leafs.isEmpty())
                throw new IncorrectHierarchyException("there must be at least one leaf set");

            leafsAsTree = leafs.stream()
                    .peek(remainingSets::remove)
                    .map(Tree::new)
                    .collect(Collectors.toSet());
        }

        return recursive(finalEdges, remainingSets, leafsAsTree);
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
