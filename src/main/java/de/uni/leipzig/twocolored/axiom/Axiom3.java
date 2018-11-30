package de.uni.leipzig.twocolored.axiom;

import java.util.Set;

import com.google.common.collect.Sets;

import de.uni.leipzig.Util;
import de.uni.leipzig.model.*;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class Axiom3 extends Axioms {

    @Override
    public boolean check(DiGraph graph, ThinnessClass alpha, ThinnessClass beta) {

        Set<Node> nAlpha = graph.getN1(alpha);
        Set<Node> nBeta = graph.getN1(beta);

        Set<Node> nnAlpha = graph.getN2(alpha);
        Set<Node> nnBeta = graph.getN2(beta);

        Set<Node> intersectionAlphaAndNNBeta = Sets.intersection(alpha.getNodes(), nnBeta);
        Set<Node> intersectionBetaAndNNAlpha = Sets.intersection(beta.getNodes(), nnAlpha);
        Set<Node> intersectionNAlphaAndNBeta = Sets.intersection(nAlpha, nBeta);

        if (intersectionAlphaAndNNBeta.isEmpty() && intersectionBetaAndNNAlpha.isEmpty()
                && !intersectionNAlphaAndNBeta.isEmpty()) {

            Set<Node> inAlpha = graph.inNeighboursOf(alpha);
            Set<Node> inBeta = graph.inNeighboursOf(beta);

            if (!Util.equalSets(inAlpha, inBeta)
                    || (!Util.properSubset(nAlpha, nBeta) && !Util.properSubset(nBeta, nAlpha))) {
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
