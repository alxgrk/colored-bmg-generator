package de.uni.leipzig.model;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.collect.*;

import de.uni.leipzig.model.edges.DiEdge;
import de.uni.leipzig.twocolored.ThinnessClassFinder;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiGraph {

    final Set<Node> nodes;

    final Set<DiEdge> edges;

    final Set<Color> colors;

    Set<ThinnessClass> thinnessClasses;

    Map<ThinnessClass, Neighbourhood> neighboursByTc;

    Map<ThinnessClass, Reachables> reachablesByTc;

    public DiGraph(Set<Node> nodes, Set<DiEdge> edges) {
        this.nodes = ImmutableSet.copyOf(nodes);
        this.edges = ImmutableSet.copyOf(edges);

        this.colors = this.nodes.stream()
                .map(Node::getColor)
                .collect(Collectors.toSet());
    }

    public Set<ThinnessClass> getThinnessClasses() {
        if (thinnessClasses == null) {
            ThinnessClassFinder tcFinder = new ThinnessClassFinder();
            this.thinnessClasses = ImmutableSet.copyOf(tcFinder.findFrom(this));
        }

        return thinnessClasses;
    }

    public Map<ThinnessClass, Neighbourhood> getNeighboursByTc() {
        if (neighboursByTc == null) {
            this.neighboursByTc = Maps.asMap(getThinnessClasses(),
                    tc -> new Neighbourhood(tc, edges));
        }

        return neighboursByTc;
    }

    public Set<Node> getN1(ThinnessClass tc) {
        return getNeighboursByTc().get(tc).getN1();
    }

    public Set<Node> getN2(ThinnessClass tc) {
        return getNeighboursByTc().get(tc).getN2();
    }

    public Set<Node> getN3(ThinnessClass tc) {
        return getNeighboursByTc().get(tc).getN3();
    }

    public Set<Node> inNeighboursOf(ThinnessClass tc) {
        return getNeighboursByTc().get(tc).getNIn();
    }

    public Map<ThinnessClass, Reachables> getReachablesByTc() {
        if (reachablesByTc == null) {
            this.reachablesByTc = Maps.newHashMap();
            for (Entry<ThinnessClass, Neighbourhood> entry : getNeighboursByTc().entrySet()) {
                reachablesByTc.put(entry.getKey(), new Reachables(entry, getNeighboursByTc()));
            }
        }

        return reachablesByTc;
    }

    public Tree getHasseDiagram() {
        Hierarchy hierarchy = new Hierarchy(getReachablesByTc());
        System.out.println("Hierarchy sets: " + hierarchy.getSets());
        return hierarchy.toHasseTree();
    }

    public DiGraph subGraphForLabels(Color s, Color t) {
        Set<Node> stNodes = nodes.stream()
                .filter(n -> n.getColor().equals(s) || n.getColor().equals(t))
                .collect(Collectors.toSet());

        Set<DiEdge> stEdges = edges.stream()
                .filter(e -> (e.getFirst().getColor().equals(s)
                        || e.getFirst().getColor().equals(t))
                        && (e.getSecond().getColor().equals(s)
                                || e.getSecond().getColor().equals(t)))
                .collect(Collectors.toSet());

        return new DiGraph(stNodes, stEdges);
    }

    @Override
    public String toString() {
        return nodes + " === " + edges;
    }
}
