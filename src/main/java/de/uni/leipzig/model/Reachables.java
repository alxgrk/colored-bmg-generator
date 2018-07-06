package de.uni.leipzig.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Sets;

import lombok.Value;

@Value
public class Reachables {
	Set<Node> r;
	Set<Node> q;
	Set<Node> rq;

	public Reachables(Entry<ÄquivalenzKlasse, Neighbourhood> entry, Map<ÄquivalenzKlasse, Neighbourhood> allN) {
		this.r = Sets.union(entry.getValue().getN1(), entry.getValue().getN2()).immutableCopy();
		this.q = calculateQ(entry, allN);
		this.rq = Sets.union(r, q).immutableCopy();
	}

	private Set<Node> calculateQ(Entry<ÄquivalenzKlasse, Neighbourhood> alpha,
			Map<ÄquivalenzKlasse, Neighbourhood> allN) {
		Set<Node> q = new HashSet<>();

		for (Entry<ÄquivalenzKlasse, Neighbourhood> beta : allN.entrySet()) {

			if (beta.getKey() == alpha.getKey())
				continue;

			if (equalInNeighbours(alpha, beta) 
					&& alpha.getValue().getN1().containsAll(beta.getValue().getN1())) {
				q.addAll(beta.getKey().getNodes());
			}

		}

		return q;
	}

	private boolean equalInNeighbours(Entry<ÄquivalenzKlasse, Neighbourhood> entry,
			Entry<ÄquivalenzKlasse, Neighbourhood> e) {
		return e.getValue().getNIn().containsAll(entry.getValue().getNIn())
				& entry.getValue().getNIn().containsAll(e.getValue().getNIn());
	}
}
