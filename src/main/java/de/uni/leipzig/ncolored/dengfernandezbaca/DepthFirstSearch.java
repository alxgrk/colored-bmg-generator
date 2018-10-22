package de.uni.leipzig.ncolored.dengfernandezbaca;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import com.google.common.annotations.VisibleForTesting;

import de.uni.leipzig.model.Tree;
import de.uni.leipzig.model.TreeGraph;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED, onConstructor = @__(@VisibleForTesting))
public class DepthFirstSearch {

    private final Marker marker;

    public DepthFirstSearch() {
        this(new Marker());
    }

    public Marker runOn(TreeGraph treeGraph) {
        int c = 0;
        marker.clear();

        for (Tree v : treeGraph.getTrees()) {

            if (!marker.isMarked(v)) {
                // found new connected component
                c = c + 1;
                depthFirstSearch(treeGraph, marker, c, v);
            }

        }

        return marker;
    }

    private void depthFirstSearch(TreeGraph treeGraph, Marker marker, int c, Tree v) {
        // mark Tree 'v' with component count 'c'
        marker.setMark(v, c);

        // get neighbours of 'v'
        treeGraph.getEdges()
                .stream()
                .filter(e -> e.getFirst().equals(v) || e.getSecond().equals(v))
                .map(e -> {
                    // consider incoming AND outgoing neighbours
                    if (e.getFirst().equals(v))
                        return e.getSecond();
                    else
                        return e.getFirst();
                })
                .forEach(neighbour -> {
                    if (!marker.isMarked(neighbour))
                        depthFirstSearch(treeGraph, marker, c, neighbour);
                });
    }

    static class Marker {

        private Map<Tree, Integer> marks = new HashMap<>();

        public Stream<Entry<Tree, Integer>> stream() {
            return marks.entrySet().stream();
        }

        public void setMark(Tree n, Integer c) {
            marks.put(n, c);
        }

        public void removeMark(Tree n) {
            marks.remove(n);
        }

        public boolean isMarked(Tree n) {
            return marks.containsKey(n);
        }

        public void clear() {
            marks.clear();
        }
    }
}
