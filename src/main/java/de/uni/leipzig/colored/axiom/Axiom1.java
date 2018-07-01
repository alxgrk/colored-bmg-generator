package de.uni.leipzig.colored.axiom;

import java.util.Set;

import com.google.common.collect.Sets;

import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.ÄquivalenzKlasse;

public class Axiom1 implements Axiom {

	@Override
	public boolean check(DiGraph graph) {
		
		for (ÄquivalenzKlasse alpha : graph.getÄquivalenzKlassen()) {
			for (ÄquivalenzKlasse beta : graph.getÄquivalenzKlassen()) {
				
				if (alpha == beta)
					continue;

				Set<Node> nAlpha = graph.neighboursOf(alpha);
				Set<Node> nBeta = graph.neighboursOf(beta);
				
				Set<Node> nnAlpha = graph.neighboursOf(nAlpha);
				Set<Node> nnBeta = graph.neighboursOf(nBeta);
				
				Set<Node> intersectionAlphaAndNBeta = Sets.intersection(alpha.getNodes(), nBeta).immutableCopy();
				Set<Node> intersectionBetaAndNAlpha = Sets.intersection(beta.getNodes(), nAlpha).immutableCopy();

				Set<Node> intersectionNAlphaAndNNBeta = Sets.intersection(nAlpha, nnBeta).immutableCopy();
				Set<Node> intersectionNBetaAndNNAlpha = Sets.intersection(nBeta, nnAlpha).immutableCopy();
				
				if (intersectionAlphaAndNBeta.isEmpty() && intersectionBetaAndNAlpha.isEmpty()) {
					
					if (!intersectionNAlphaAndNNBeta.isEmpty() || !intersectionNBetaAndNNAlpha.isEmpty())
						return false;
					
				}
				
			}
		}
		
		return true;
	}

}
