package de.uni.leipzig.uncolored;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;

import de.uni.leipzig.Util;
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
        Marker marker = depthFirstSearch.runOn(tripleSetR, leftOvers);

        Map<Integer, List<Entry<Node, Integer>>> entriesByCount = marker.stream()
                .collect(Collectors.groupingBy(Entry::getValue));
        marker.clear();

        return entriesByCount.values()
                .stream()
                .map(el -> el.stream()
                        .map(e -> e.getKey())
                        .collect(Collectors.toSet()))
                .map(es -> new Tree(Util.nodeSetToLeafTree(es)))
                .collect(Collectors.toList());

    }

    public List<DiGraph> construct(DiGraph diGraph) {
        Marker marker = depthFirstSearch.runOn(diGraph);

        Map<Integer, List<Entry<Node, Integer>>> entriesByCount = marker.stream()
                .collect(Collectors.groupingBy(Entry::getValue));
        marker.clear();

        return entriesByCount.values()
                .stream()
                .map(el -> {
                    Set<Node> nodes = Sets.newHashSet();
                    Set<DiEdge> edges = Sets.newHashSet();

                    el.forEach(e -> {
                        Node node = e.getKey();
                        nodes.add(node);
                        diGraph.getEdges()
                                .stream()
                                .filter(de -> de.getFirst().equals(node)
                                        || de.getSecond().equals(node))
                                .forEach(edges::add);
                    });

                    return new DiGraph(nodes, edges);
                })
                .collect(Collectors.toList());
    }

}
