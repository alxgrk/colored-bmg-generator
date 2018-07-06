package de.uni.leipzig.model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import lombok.Value;

@Value
public class Neighbourhood {
	Set<Node> n1;
	Set<Node> n2;
	Set<Node> n3;
	Set<Node> nIn;
	
	public Neighbourhood(ÄquivalenzKlasse äk, Set<DiEdge> edges){
		n1 = neighboursCalc(äk, edges);
		n2 = neighboursCalc(n1, edges);
		n3 = neighboursCalc(n2, edges);
		nIn = inNeighboursOf(äk, edges);
	}
	
	private Set<Node> neighboursCalc(ÄquivalenzKlasse äk, Set<DiEdge> edges) {
		return neighboursCalc(äk.getNodes(), edges);
	}

	private Set<Node> neighboursCalc(Set<Node> äk, Set<DiEdge> edges) {
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
	
	private Set<Node> inNeighboursOf(ÄquivalenzKlasse äk, Set<DiEdge> edges) {
		Set<Node> neighbours = new HashSet<>();

		Optional<Node> any = äk.getNodes().stream().findAny();
		any.ifPresent(n -> {

			for (DiEdge diEdge : edges) {

				if (diEdge.getSecond() == n)
					neighbours.add(diEdge.getFirst());
			}
		});
		
		return neighbours;
	}
}
