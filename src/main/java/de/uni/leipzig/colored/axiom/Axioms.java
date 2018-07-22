package de.uni.leipzig.colored.axiom;

import java.util.List;

import com.google.common.collect.Lists;

import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.EquivalenceClass;

public abstract class Axioms {

	private static List<Axioms> axioms = Lists.newArrayList(new Axiom1(), new Axiom2(), new Axiom3());

	public static final boolean checkAll(DiGraph graph) {

		for (EquivalenceClass alpha : graph.getÄquivalenzKlassen()) {
			for (EquivalenceClass beta : graph.getÄquivalenzKlassen()) {

				if (alpha == beta)
					continue;

				for (Axioms a : axioms) {
					if (!a.check(graph, alpha, beta)) {
						System.out.println(a + " not fulfilled for alpha=" + alpha +" & beta=" + beta);
						return false;
					}
						
					System.out.println(a + " fulfilled for alpha=" + alpha +" & beta=" + beta);
				}

			}
		}

		return true;
	}

	abstract boolean check(DiGraph graph, EquivalenceClass alpha, EquivalenceClass beta);

}
