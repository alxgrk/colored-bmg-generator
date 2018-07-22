package de.uni.leipzig.uncolored;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.model.edges.Edge;
import lombok.Getter;

@Getter
public class TripleFinder {

	private Set<Node> leaves = new HashSet<>();

	public List<Triple> findTriple(List<List<Node>> liste) {

		List<Triple> tripleList = new ArrayList<>();

		for (int i = 0; i < liste.size(); i++) {

			List<Node> firstList = liste.get(i);

			if (!isLeave(firstList)) {
				continue;
			}

			Node firstNode = firstList.get(0);
			// add every leave
			leaves.add(firstNode);
			
			for (int j = 0; j < liste.size(); j++) {

				List<Node> secondList = liste.get(j);

				if (!isLeave(secondList) || j == i) {
					continue;
				}
				
				Node secondNode = secondList.get(0);

				for (int k = 0; k < liste.size(); k++) {

					List<Node> thirdList = liste.get(k);

					if (isLeave(thirdList) && k != j && k != i) {

						Node thirdNode = thirdList.get(0);

						List<Integer> x = firstNode.getIds();
						List<Integer> y = secondNode.getIds();
						List<Integer> z = thirdNode.getIds();

						List<Integer> lcaXY = findLCA(x, y);
						List<Integer> lcaXYZ = findLCA(z, lcaXY);
						
						// xy is the ingroup, z is the outgroup 
						if (isTriple(lcaXY, lcaXYZ)) {
							Edge edge = new Edge(firstNode, secondNode);
							Triple tripel = new Triple(edge, thirdNode);
							tripleList.add(tripel);
						}
					}
				}
			}
		}

		return tripleList;
	}

	private boolean isTriple(List<Integer> lcaXY, List<Integer> lcaXYZ) {
		return lcaXY.size() > lcaXYZ.size();
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

	private boolean isLeave(List<Node> nodes) {
		return nodes.size() == 1;
	}

}
