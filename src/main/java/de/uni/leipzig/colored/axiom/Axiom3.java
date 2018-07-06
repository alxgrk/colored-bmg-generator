package de.uni.leipzig.colored.axiom;

import java.util.Set;

import com.google.common.collect.Sets;

import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.ÄquivalenzKlasse;

public class Axiom3 extends Axioms {

	@Override
	public boolean check(DiGraph graph, ÄquivalenzKlasse alpha, ÄquivalenzKlasse beta) {

		Set<Node> inAlpha = graph.inNeighboursOf(alpha);
		Set<Node> inBeta = graph.inNeighboursOf(beta);

		Set<Node> nAlpha = graph.getN2(alpha);
		Set<Node> nBeta = graph.getN2(beta);

		Set<Node> nnAlpha = graph.getN2(alpha);
		Set<Node> nnBeta = graph.getN2(beta);

		Set<Node> intersectionAlphaAndNNBeta = Sets.intersection(alpha.getNodes(), nnBeta).immutableCopy();
		Set<Node> intersectionBetaAndNNAlpha = Sets.intersection(beta.getNodes(), nnAlpha).immutableCopy();
		Set<Node> intersectionNAlphaAndNBeta = Sets.intersection(nAlpha, nBeta).immutableCopy();

		if (intersectionAlphaAndNNBeta.isEmpty() && intersectionBetaAndNNAlpha.isEmpty()
				&& !intersectionNAlphaAndNBeta.isEmpty()) {

			if (!inAlpha.containsAll(inBeta)
					|| (!Sets.difference(nAlpha, nBeta).isEmpty() && !Sets.difference(nBeta, nAlpha).isEmpty())) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		return "Axiom 3";
	}
}
