package de.uni.leipzig.twocolored.axiom;

import java.util.List;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;

import de.uni.leipzig.model.*;
import lombok.*;

public abstract class Axioms {

    @Getter(value = AccessLevel.PROTECTED, onMethod = @__(@VisibleForTesting))
    private static List<Axioms> axioms = ImmutableList.of(new Axiom1(), new Axiom2(), new Axiom3());

    public static final boolean checkAll(DiGraph graph) {

        for (ThinnessClass alpha : graph.getThinnessClasses()) {
            for (ThinnessClass beta : graph.getThinnessClasses()) {

                if (alpha == beta)
                    continue;

                for (Axioms a : axioms) {
                    if (!a.check(graph, alpha, beta)) {
                        return false;
                    }
                }

            }
        }

        return true;
    }

    abstract boolean check(DiGraph graph, ThinnessClass alpha, ThinnessClass beta);

}
