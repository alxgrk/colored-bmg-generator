package de.uni.leipzig.ncolored.dengfernandezbaca;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;

import de.uni.leipzig.model.Tree;
import de.uni.leipzig.model.TreeGraph;
import de.uni.leipzig.model.edges.TreeEdge;
import de.uni.leipzig.ncolored.dengfernandezbaca.DepthFirstSearch.Marker;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED, onConstructor = @__(@VisibleForTesting))
public class ConnectedComponentsConstructor {

    private final DepthFirstSearch depthFirstSearch;

    public ConnectedComponentsConstructor() {
        this(new DepthFirstSearch());
    }

    public List<TreeGraph> construct(TreeGraph treeGraph) {
        Marker marker = depthFirstSearch.runOn(treeGraph);

        Map<Integer, List<Entry<Tree, Integer>>> entriesByCount = marker.stream()
                .collect(Collectors.groupingBy(Entry::getValue));
        marker.clear();

        return entriesByCount.values()
                .stream()
                .map(el -> {
                    Set<Tree> nodes = Sets.newHashSet();
                    Set<TreeEdge> edges = Sets.newHashSet();

                    el.forEach(e -> {
                        Tree node = e.getKey();
                        nodes.add(node);
                        treeGraph.getEdges().forEach(de -> {
                            if (de.getFirst().equals(node)
                                    || de.getSecond().equals(node))
                                edges.add(de);
                        });
                    });

                    return new TreeGraph(nodes, edges);
                })
                .collect(Collectors.toList());
    }

}
