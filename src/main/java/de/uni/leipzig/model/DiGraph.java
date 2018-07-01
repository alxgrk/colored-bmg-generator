package de.uni.leipzig.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import de.uni.leipzig.colored.ÄquivalenzKlassenFinder;
import lombok.Value;

@Value
public class DiGraph {

	private final Set<Node> nodes;

	private final Set<DiEdge> edges;

	private final Set<ÄquivalenzKlasse> äquivalenzKlassen;
	
	private final Map<ÄquivalenzKlasse, Set<Node>> neighboursByÄk;
	
	private final Map<ÄquivalenzKlasse, Set<Node>> neighboursOfNeighboursByÄk;

	public DiGraph(Set<Node> nodes, Set<DiEdge> edges) {
		this.nodes = ImmutableSet.copyOf(nodes);
		this.edges = ImmutableSet.copyOf(edges);

		this.äquivalenzKlassen = ImmutableSet.copyOf(new ÄquivalenzKlassenFinder().findÄK(this));
		
		// TODO
		this.neighboursByÄk = Maps.newHashMap();
		this.neighboursOfNeighboursByÄk = Maps.newHashMap();
	}

	public Set<Node> neighboursOf(ÄquivalenzKlasse äk) {
		return neighboursOf(äk.getNodes());
	}

	public Set<Node> neighboursOf(Set<Node> äk) {
		Set<Node> neighbours = new HashSet<>();
		
		Optional<Node> any = äk.stream().findAny();
		any.ifPresent(n -> {
			
			for (DiEdge diEdge : edges) {
				
				if (diEdge.getFirst() == n)
					neighbours.add(diEdge.getSecond());
				
			}
			
		});
		
		return neighbours;
	}

}
