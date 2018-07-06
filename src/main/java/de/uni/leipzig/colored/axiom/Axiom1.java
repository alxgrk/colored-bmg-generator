package de.uni.leipzig.colored.axiom;

import java.util.Set;

import com.google.common.collect.Sets;

import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.ÄquivalenzKlasse;

public class Axiom1 extends Axioms {

	@Override
	public boolean check(DiGraph graph, ÄquivalenzKlasse alpha, ÄquivalenzKlasse beta) {

		Set<Node> nAlpha = graph.getN1(alpha);
		Set<Node> nBeta = graph.getN1(beta);

		Set<Node> nnAlpha = graph.getN2(alpha);
		Set<Node> nnBeta = graph.getN2(beta);

		Set<Node> intersectionAlphaAndNBeta = Sets.intersection(alpha.getNodes(), nBeta).immutableCopy();
		Set<Node> intersectionBetaAndNAlpha = Sets.intersection(beta.getNodes(), nAlpha).immutableCopy();

		Set<Node> intersectionNAlphaAndNNBeta = Sets.intersection(nAlpha, nnBeta).immutableCopy();
		Set<Node> intersectionNBetaAndNNAlpha = Sets.intersection(nBeta, nnAlpha).immutableCopy();

		if (intersectionAlphaAndNBeta.isEmpty() && intersectionBetaAndNAlpha.isEmpty()) {

			if (!intersectionNAlphaAndNNBeta.isEmpty() || !intersectionNBetaAndNNAlpha.isEmpty())
				return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "Axiom 1";
	}
	
}
