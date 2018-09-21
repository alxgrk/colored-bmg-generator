package de.uni.leipzig.model;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import de.uni.leipzig.colored.EquivalenceClassFinder;
import de.uni.leipzig.model.edges.DiEdge;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiGraph {

    final Set<Node> nodes;

    final Set<DiEdge> edges;

    Set<EquivalenceClass> equivalenceClasses;

    Map<EquivalenceClass, Neighbourhood> neighboursByEc;

    Map<EquivalenceClass, Reachables> reachablesByEc;

    public DiGraph(Set<Node> nodes, Set<DiEdge> edges) {
        this.nodes = ImmutableSet.copyOf(nodes);
        this.edges = ImmutableSet.copyOf(edges);
    }

    public Set<EquivalenceClass> getEquivalenceClasses() {
        if (equivalenceClasses == null) {
            EquivalenceClassFinder ecFinder = new EquivalenceClassFinder();
            this.equivalenceClasses = ImmutableSet.copyOf(ecFinder.findFrom(this));
        }

        return equivalenceClasses;
    }

    public Map<EquivalenceClass, Neighbourhood> getNeighboursByEc() {
        if (neighboursByEc == null) {
            this.neighboursByEc = Maps.asMap(getEquivalenceClasses(),
                    ec -> new Neighbourhood(ec, edges));
        }

        return neighboursByEc;
    }

    public Set<Node> getN1(EquivalenceClass ec) {
        return getNeighboursByEc().get(ec).getN1();
    }

    public Set<Node> getN2(EquivalenceClass ec) {
        return getNeighboursByEc().get(ec).getN2();
    }

    public Set<Node> getN3(EquivalenceClass ec) {
        return getNeighboursByEc().get(ec).getN3();
    }

    public Set<Node> inNeighboursOf(EquivalenceClass ec) {
        return getNeighboursByEc().get(ec).getNIn();
    }

    public Map<EquivalenceClass, Reachables> getReachablesByEc() {
        if (reachablesByEc == null) {
            this.reachablesByEc = Maps.newHashMap();
            for (Entry<EquivalenceClass, Neighbourhood> entry : getNeighboursByEc().entrySet()) {
                reachablesByEc.put(entry.getKey(), new Reachables(entry, getNeighboursByEc()));
            }
        }

        return reachablesByEc;
    }

    public Tree getHasseDiagram() {
        Hierarchy hierarchy = new Hierarchy(getReachablesByEc());
        return hierarchy.toHasseTree();
    }
}
