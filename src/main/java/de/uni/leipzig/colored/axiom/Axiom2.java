package de.uni.leipzig.colored.axiom;

import java.util.Set;

import com.google.common.collect.Sets;

import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.ÄquivalenzKlasse;

public class Axiom2 extends Axioms {

	@Override
	public boolean check(DiGraph graph, ÄquivalenzKlasse alpha, ÄquivalenzKlasse beta) {

		Set<Node> nAlpha = graph.getN1(alpha);
		Set<Node> nnnAlpha = graph.getN3(alpha);

		if (!Sets.difference(nnnAlpha, nAlpha).isEmpty()) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "Axiom 2";
	}
}
