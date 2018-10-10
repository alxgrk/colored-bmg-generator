package de.uni.leipzig.twocolored.axiom;

import java.util.Set;

import com.google.common.collect.Sets;

import de.uni.leipzig.Util;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.ThinnessClass;
import de.uni.leipzig.model.Node;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class Axiom3 extends Axioms {

    @Override
    public boolean check(DiGraph graph, ThinnessClass alpha, ThinnessClass beta) {

        Set<Node> inAlpha = graph.inNeighboursOf(alpha);
        Set<Node> inBeta = graph.inNeighboursOf(beta);

        Set<Node> nAlpha = graph.getN2(alpha);
        Set<Node> nBeta = graph.getN2(beta);

        Set<Node> nnAlpha = graph.getN2(alpha);
        Set<Node> nnBeta = graph.getN2(beta);

        Set<Node> intersectionAlphaAndNNBeta = Sets.intersection(alpha.getNodes(), nnBeta)
                .immutableCopy();
        Set<Node> intersectionBetaAndNNAlpha = Sets.intersection(beta.getNodes(), nnAlpha)
                .immutableCopy();
        Set<Node> intersectionNAlphaAndNBeta = Sets.intersection(nAlpha, nBeta).immutableCopy();

        if (intersectionAlphaAndNNBeta.isEmpty() && intersectionBetaAndNNAlpha.isEmpty()
                && !intersectionNAlphaAndNBeta.isEmpty()) {

            if (!inAlpha.containsAll(inBeta)
                    || (!Util.equalSets(nAlpha, nBeta) && !Util.equalSets(nBeta, nAlpha))) {
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
