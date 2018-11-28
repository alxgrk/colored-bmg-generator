package de.uni.leipzig.uncolored;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.*;

import com.google.common.annotations.VisibleForTesting;

import de.uni.leipzig.model.*;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.edges.AbstractPair;

public class DepthFirstSearch {

    public Marker runOn(Set<Triple> tripleSetR, Set<Node> leftOvers) {
        return runOn(leftOvers, tripleSetR.stream()
                .map(t -> (AbstractPair<Node>) t.getEdge())
                .collect(Collectors.toSet()), new Marker());
    }

    public Marker runOn(DiGraph diGraph) {
        return runOn(diGraph.getNodes(), diGraph.getEdges(), new Marker());
    }

    @VisibleForTesting
    protected Marker runOn(Set<Node> nodes, Set<? extends AbstractPair<Node>> edges,
            Marker marker) {
        int c = 0;
        marker.clear();

        for (Node v : nodes) {

            if (!marker.isMarked(v)) {
                // found new connected component
                c = c + 1;
                depthFirstSearch(edges, marker, c, v);
            }

        }

        return marker;
    }

    private void depthFirstSearch(Set<? extends AbstractPair<Node>> edges, Marker marker, int c,
            Node v) {
        // mark node 'v' with component count 'c'
        marker.setMark(v, c);

        // get neighbours of 'v'
        edges.stream()
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
                        depthFirstSearch(edges, marker, c, neighbour);
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
