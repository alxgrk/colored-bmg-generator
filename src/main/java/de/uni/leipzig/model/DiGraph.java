package de.uni.leipzig.model;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import de.uni.leipzig.colored.ÄquivalenzKlassenFinder;
import lombok.Value;

@Value
public class DiGraph {

	Set<Node> nodes;
	Set<DiEdge> edges;
	Set<ÄquivalenzKlasse> äquivalenzKlassen;
	Map<ÄquivalenzKlasse, Neighbourhood> neighboursByÄk;
	Map<ÄquivalenzKlasse, Reachables> reachablesByÄk;
	Hierarchy hierarchy;

	public DiGraph(Set<Node> nodes, Set<DiEdge> edges) {
		this.nodes = ImmutableSet.copyOf(nodes);
		this.edges = ImmutableSet.copyOf(edges);

		this.äquivalenzKlassen = ImmutableSet.copyOf(new ÄquivalenzKlassenFinder().findÄK(this));

		this.neighboursByÄk = Maps.newHashMap();
		for (ÄquivalenzKlasse äk : äquivalenzKlassen) {
			neighboursByÄk.put(äk, new Neighbourhood(äk, edges));
		}

		this.reachablesByÄk = Maps.newHashMap();
		for (Entry<ÄquivalenzKlasse, Neighbourhood> entry : neighboursByÄk.entrySet()) {
			reachablesByÄk.put(entry.getKey(), new Reachables(entry, neighboursByÄk));
		}
		
		hierarchy = new Hierarchy(this);
	}

	public Set<Node> getN1(ÄquivalenzKlasse äk) {
		return neighboursByÄk.get(äk).getN1();
	}

	public Set<Node> getN2(ÄquivalenzKlasse äk) {
		return neighboursByÄk.get(äk).getN2();
	}

	public Set<Node> getN3(ÄquivalenzKlasse äk) {
		return neighboursByÄk.get(äk).getN3();
	}
	
	public Set<Node> inNeighboursOf(ÄquivalenzKlasse äk) {
		return neighboursByÄk.get(äk).getNIn();
	}
}
