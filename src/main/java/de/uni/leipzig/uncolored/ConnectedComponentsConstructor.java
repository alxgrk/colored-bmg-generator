package de.uni.leipzig.uncolored;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;

import de.uni.leipzig.model.*;
import de.uni.leipzig.model.edges.DiEdge;
import de.uni.leipzig.uncolored.DepthFirstSearch.Marker;
import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED, onConstructor = @__(@VisibleForTesting))
public class ConnectedComponentsConstructor {

    private final DepthFirstSearch depthFirstSearch;

    public ConnectedComponentsConstructor() {
        this(new DepthFirstSearch());
    }

    public List<Tree> construct(Set<Triple> tripleSetR, Set<Node> leftOvers) {
        List<Tree> subTrees = new ArrayList<>();

        // iterate over remaining nodes as long, as there are some left
        while (!leftOvers.isEmpty()) {

            // store nodes contained by a connected component in a set
            Set<Node> subTree = new HashSet<>();

            leftOvers = leftOvers.stream()
                    .filter(n -> {
                        // if this is the first of the remaining nodes,
                        // add it and remove it from 'leftOvers'
                        if (subTree.isEmpty()) {
                            subTree.add(n);
                            return false;
                        }

                        // iterate over all triples in order to see, if either
                        // the first or the second value of the edge connects
                        // the current and any other node already existing in
                        // the connected component
                        for (Triple t : tripleSetR) {
                            Node first = t.getEdge().getFirst();
                            Node second = t.getEdge().getSecond();

                            // current node is connected to a node of the
                            // connected component -> remove it from 'leftOvers'
                            if (first.equals(n) && subTree.contains(second)) {
                                subTree.add(first);
                                return false;
                            }

                            // current node is connected to a node of the
                            // connected component -> remove it from 'leftOvers'
                            if (second.equals(n) && subTree.contains(first)) {
                                subTree.add(second);
                                return false;
                            }
                        }

                        // current node is not part of this connected component,
                        // leave it in 'leftOvers' for the next iteration
                        return true;
                    })
                    .collect(Collectors.toSet());

            subTrees.add(new Tree(subTree));
        }

        return subTrees;
    }

    public List<DiGraph> construct(DiGraph diGraph) {
        Marker marker = depthFirstSearch.runOn(diGraph);

        Map<Integer, List<Entry<Node, Integer>>> entriesByCount = marker.stream()
                .collect(Collectors.groupingBy(Entry::getValue));

        return entriesByCount.values()
                .stream()
                .map(el -> {
                    Set<Node> nodes = Sets.newHashSet();
                    Set<DiEdge> edges = Sets.newHashSet();

                    el.forEach(e -> {
                        Node node = e.getKey();
                        nodes.add(node);
                        diGraph.getEdges().forEach(de -> {
                            if (de.getFirst().equals(node)
                                    || de.getSecond().equals(node))
                                edges.add(de);
                        });
                    });

                    return new DiGraph(nodes, edges);
                })
                .collect(Collectors.toList());
    }

}
