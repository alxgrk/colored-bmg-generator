package de.uni.leipzig.twocolored.axiom;

import java.util.Set;

import de.uni.leipzig.Util;
import de.uni.leipzig.model.*;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class Axiom2 extends Axioms {

    @Override
    public boolean check(DiGraph graph, ThinnessClass alpha, ThinnessClass beta) {

        Set<Node> nAlpha = graph.getN1(alpha);
        Set<Node> nnnAlpha = graph.getN3(alpha);

        if (!Util.properSubset(nnnAlpha, nAlpha)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Axiom 2";
    }
}
