package de.uni.leipzig.model;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import de.uni.leipzig.colored.EquivalenceClassFinder;
import de.uni.leipzig.model.edges.DiEdge;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class DiGraph {

    Set<Node> nodes;

    Set<DiEdge> edges;

    Set<EquivalenceClass> äquivalenzKlassen;

    Map<EquivalenceClass, Neighbourhood> neighboursByÄk;

    Map<EquivalenceClass, Reachables> reachablesByÄk;

    Hierarchy hierarchy;

    public DiGraph(Set<Node> nodes, Set<DiEdge> edges) {
        this.nodes = ImmutableSet.copyOf(nodes);
        this.edges = ImmutableSet.copyOf(edges);

        this.äquivalenzKlassen = ImmutableSet.copyOf(new EquivalenceClassFinder().findFrom(this));

        this.neighboursByÄk = Maps.newHashMap();
        for (EquivalenceClass äk : äquivalenzKlassen) {
            neighboursByÄk.put(äk, new Neighbourhood(äk, edges));
        }

        this.reachablesByÄk = Maps.newHashMap();
        for (Entry<EquivalenceClass, Neighbourhood> entry : neighboursByÄk.entrySet()) {
            reachablesByÄk.put(entry.getKey(), new Reachables(entry, neighboursByÄk));
        }

        hierarchy = new Hierarchy(reachablesByÄk);
    }

    public Tree getHasseDiagram() {
        return hierarchy.toHasseTree();
    }

    public Set<Node> getN1(EquivalenceClass äk) {
        return neighboursByÄk.get(äk).getN1();
    }

    public Set<Node> getN2(EquivalenceClass äk) {
        return neighboursByÄk.get(äk).getN2();
    }

    public Set<Node> getN3(EquivalenceClass äk) {
        return neighboursByÄk.get(äk).getN3();
    }

    public Set<Node> inNeighboursOf(EquivalenceClass äk) {
        return neighboursByÄk.get(äk).getNIn();
    }
}
