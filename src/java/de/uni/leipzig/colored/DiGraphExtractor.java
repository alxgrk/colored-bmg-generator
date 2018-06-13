package de.uni.leipzig.colored;

import java.util.ArrayList;
import java.util.List;

import de.uni.leipzig.model.DiEdge;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Edge;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Triple;
import lombok.Getter;

@Getter
public class DiGraphExtractor {
	
	private DiGraph graph = new DiGraph();
	
	public void extract(List<List<Node>> adjList){
		
		for (int i = 0; i < adjList.size(); i++) {

			List<Node> firstList = adjList.get(i);

			if (!isLeave(firstList)) {
				continue;
			}

			Node firstNode = firstList.get(0);
			// add every leave
			graph.addNode(firstNode);
			
			for (int j = 0; j < adjList.size(); j++) {

				List<Node> secondList = adjList.get(j);

				if (!isLeave(secondList) || j == i) {
					continue;
				}
				
				Node secondNode = secondList.get(0);

				for (int k = 0; k < adjList.size(); k++) {

					List<Node> thirdList = adjList.get(k);

					if (isLeave(thirdList) && k != j && k != i) {

						Node thirdNode = thirdList.get(0);

						if(firstNode.getLabel() != secondNode.getLabel() && 
								( firstNode.getLabel() == thirdNode.getLabel() || secondNode.getLabel() == thirdNode.getLabel() ) ){
							
							List<Integer> x = firstNode.getIds();
							List<Integer> y = secondNode.getIds();
							List<Integer> z = thirdNode.getIds();
	
							List<Integer> lcaXY = findLCA(x, y);
							List<Integer> lcaXZ = findLCA(x, z);
							
							// xy is the ingroup, z is the outgroup 
							if (lcaXY.size() > lcaXZ.size() ) {
								
								DiEdge edge;
								
								if(firstNode.getLabel() == thirdNode.getLabel()){
									edge = new DiEdge(secondNode, firstNode);
								} else {
									edge = new DiEdge(firstNode, secondNode);
								}
								graph.addEdge(edge);
							}
						}
					}
				}
			}
		}
	}

	private boolean isLeave(List<Node> nodes) {
		return nodes.size() == 1;
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


