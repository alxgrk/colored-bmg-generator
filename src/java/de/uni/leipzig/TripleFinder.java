package de.uni.leipzig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uni.leipzig.model.Edge;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Triple;
import lombok.Getter;

@Getter
public class TripleFinder {

	private Set<Node> leaves = new HashSet<>();
	
	public List<Triple> findTriple(List<List<Node>> liste) {

		List<Triple> tripleList = new ArrayList<>();
		
		for (int i = 0; i < liste.size(); i++) {
			if (liste.get(i).size() != 1) {
				continue;
			}

			for (int j = 0; j < liste.size(); j++) {
				if (liste.get(j).size() != 1 || j == i) {
					continue;
				}

				for (int k = 0; k < liste.size(); k++) {
					if (liste.get(k).size() == 1 && k != j && k != i) {

						Node firstNode = liste.get(i).get(0);
						Node secondNode = liste.get(j).get(0);
						Node thirdNode = liste.get(k).get(0);
						
						List<Integer> x = firstNode.getIds();
						List<Integer> y = secondNode.getIds();
						List<Integer> z = thirdNode.getIds();

						List<Integer> lcaXY = findLCA(x, y);
						List<Integer> lcaXYZ = findLCA(z, lcaXY);

						if (lcaXY.size() > lcaXYZ.size()) {
							Edge edge = new Edge(firstNode, secondNode);
							Triple tripel = new Triple(edge, thirdNode);
							tripleList.add(tripel);
							
							leaves.add(firstNode);
						}
					}
				}
			}
		}
		
		return tripleList;
	}

	public List<Integer> findLCA(List<Integer> a, List<Integer> b) {

		List<Integer> ancester = new ArrayList<>();

		int v = 0;

		while (a.get(v) == b.get(v)) {

			ancester.add(a.get(v));
			if (v == a.size() - 1 || v == b.size() - 1) {
				return ancester;
			}
			v++;
		}
		return ancester;
	}

}
