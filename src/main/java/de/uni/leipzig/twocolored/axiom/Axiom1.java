package de.uni.leipzig.twocolored.axiom;

import java.util.Set;

import com.google.common.collect.Sets;

import de.uni.leipzig.model.*;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class Axiom1 extends Axioms {

    @Override
    public boolean check(DiGraph graph, ThinnessClass alpha, ThinnessClass beta) {

        Set<Node> nAlpha = graph.getN1(alpha);
        Set<Node> nBeta = graph.getN1(beta);

        Set<Node> intersectionAlphaAndNBeta = Sets.intersection(alpha.getNodes(), nBeta);
        Set<Node> intersectionBetaAndNAlpha = Sets.intersection(beta.getNodes(), nAlpha);

        if (intersectionAlphaAndNBeta.isEmpty() && intersectionBetaAndNAlpha.isEmpty()) {

            Set<Node> nnAlpha = graph.getN2(alpha);
            Set<Node> nnBeta = graph.getN2(beta);

            Set<Node> intersectionNAlphaAndNNBeta = Sets.intersection(nAlpha, nnBeta);
            Set<Node> intersectionNBetaAndNNAlpha = Sets.intersection(nBeta, nnAlpha);

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
