package de.uni.leipzig.colored.axiom;

import java.util.List;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;

import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.EquivalenceClass;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class Axioms {

    @Getter(value = AccessLevel.PROTECTED, onMethod = @__(@VisibleForTesting))
    private static List<Axioms> axioms = ImmutableList.of(new Axiom1(), new Axiom2(), new Axiom3());

    public static final boolean checkAll(DiGraph graph) {

        for (EquivalenceClass alpha : graph.getEquivalenceClasses()) {
            for (EquivalenceClass beta : graph.getEquivalenceClasses()) {

                if (alpha == beta)
                    continue;

                for (Axioms a : axioms) {
                    if (!a.check(graph, alpha, beta)) {
                        System.out.println(a + " not fulfilled for alpha=" + alpha + " & beta="
                                + beta);
                        return false;
                    }

                    System.out.println(a + " fulfilled for alpha=" + alpha + " & beta=" + beta);
                }

            }
        }

        return true;
    }

    abstract boolean check(DiGraph graph, EquivalenceClass alpha, EquivalenceClass beta);

}
