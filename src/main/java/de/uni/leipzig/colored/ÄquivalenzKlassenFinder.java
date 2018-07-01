package de.uni.leipzig.colored;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Edge;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.ÄquivalenzKlasse;

public class ÄquivalenzKlassenFinder {

	private Set<ÄquivalenzKlasse> ÄKlassen = new HashSet<ÄquivalenzKlasse>();
	private Map<Node, List<Node>> outerN = new HashMap<>();
	private Map<Node, List<Node>> innerN = new HashMap<>();

	public Set<ÄquivalenzKlasse> findÄK(DiGraph graph) {

		// erstelle listen-listen mit inneren und äußeren Nachbarn

		for (Node n : graph.getNodes()) {

			outerN.put(n, new ArrayList<>());
			innerN.put(n, new ArrayList<>());

			for (Edge e : graph.getEdges()) {

				if (n.equals(e.getFirst())) {
					List<Node> nodeOuterN = outerN.get(n);
					nodeOuterN.add(e.getSecond());
				} else if (n.equals(e.getSecond())) {
					List<Node> nodeInnerN = innerN.get(n);
					nodeInnerN.add(e.getFirst());
				}
			}

		}

		// finde Äquivalenzklassen

		for (Node n : graph.getNodes()) {

			boolean skip = false;
			
			for (ÄquivalenzKlasse äk : ÄKlassen) {
				skip = äk.contains(n);
			}
			
			if (skip)
				continue;

			ÄquivalenzKlasse ÄK = new ÄquivalenzKlasse();
			ÄK.hinzufügen(n);

			for (Node nextNode : graph.getNodes()) {
				
				if (n == nextNode)
					continue;

				if (outerN.get(n).equals(outerN.get(nextNode)) 
						&& innerN.get(n).equals(innerN.get(nextNode))) {
					ÄK.hinzufügen(nextNode);
				}
			}

			ÄKlassen.add(ÄK);
		}

		return ÄKlassen;
	}

}
