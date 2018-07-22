package de.uni.leipzig.colored.axiom;

import java.util.Set;

import de.uni.leipzig.Util;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.EquivalenceClass;

public class Axiom2 extends Axioms {

    @Override
    public boolean check(DiGraph graph, EquivalenceClass alpha, EquivalenceClass beta) {

        Set<Node> nAlpha = graph.getN1(alpha);
        Set<Node> nnnAlpha = graph.getN3(alpha);

        if (!Util.equalSets(nnnAlpha, nAlpha)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Axiom 2";
    }
}
