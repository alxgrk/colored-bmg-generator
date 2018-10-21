package de.uni.leipzig.uncolored;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Stream;

import com.google.common.annotations.VisibleForTesting;

import de.uni.leipzig.model.*;
import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED, onConstructor = @__(@VisibleForTesting))
public class DepthFirstSearch {

    private final Marker marker;

    public DepthFirstSearch() {
        this(new Marker());
    }

    public Marker runOn(DiGraph diGraph) {
        int c = 0;
        marker.clear();

        for (Node v : diGraph.getNodes()) {

            if (!marker.isMarked(v)) {
                // found new connected component
                c = c + 1;
                depthFirstSearch(diGraph, marker, c, v);
            }

        }

        return marker;
    }

    private void depthFirstSearch(DiGraph diGraph, Marker marker, int c, Node v) {
        // mark node 'v' with component count 'c'
        marker.setMark(v, c);

        // get neighbours of 'v'
        diGraph.getEdges()
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
                        depthFirstSearch(diGraph, marker, c, neighbour);
                });
    }

    static class Marker {

        private Map<Node, Integer> marks = new HashMap<>();

        public Stream<Entry<Node, Integer>> stream() {
            return marks.entrySet().stream();
        }

        public void setMark(Node n, Integer c) {
            marks.put(n, c);
        }

        public void removeMark(Node n) {
            marks.remove(n);
        }

        public boolean isMarked(Node n) {
            return marks.containsKey(n);
        }

        public void clear() {
            marks.clear();
        }
    }
}
