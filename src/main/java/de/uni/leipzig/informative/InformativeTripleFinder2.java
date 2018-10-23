package de.uni.leipzig.informative;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.alg.util.Pair;

import com.google.common.collect.Sets;

import de.uni.leipzig.informative.model.InformativeTriple;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.edges.DiEdge;
import de.uni.leipzig.model.edges.Edge;

public class InformativeTripleFinder2 {

    public Pair<Set<InformativeTriple>, Set<Node>> findTriple(DiGraph graph) {
        
    	Set<InformativeTriple> foundTripleSet = new HashSet<>();
    	Set<Node> foundNodes = new HashSet<>();
    	
    	Set<DiEdge> edges = graph.getEdges();
    	
    	for (Node node1: graph.getNodes()) {
    		for (Node node2: graph.getNodes()) {
    			if(node1.equals(node2))
    				continue;
    			
    			for (Node node3: graph.getNodes()) {
    				if(node1.equals(node3) || node2.equals(node3))
        				continue;
        			boolean ab = checkEdge(edges, node1, node2);
        			boolean ac = checkEdge(edges, node1, node3);
        			boolean bc = checkEdge(edges, node2, node3);
        			boolean ba = checkEdge(edges, node2, node1);
        			boolean ca = checkEdge(edges, node3, node1);
        			boolean cb = checkEdge(edges, node3, node2);
        			
        			if(cb || bc)
        				continue;
        			
        			if(checkX(ab, ba, ac, ca) == true){
        				Edge edge = new Edge(node1, node2);
        				Node node = node3;
        				InformativeTriple foundInformativeTriple = new InformativeTriple(edge, node);
        				foundTripleSet.add(foundInformativeTriple);
        				foundNodes.addAll(Sets.newHashSet(node1, node2, node3));
        			}
    			}
    		}
		}
    	
    	Pair<Set<InformativeTriple>, Set<Node>> pair = new Pair<Set<InformativeTriple>, Set<Node>>(foundTripleSet, foundNodes);
    	
        return pair;
    }

    private boolean checkX(boolean ab, boolean ba, boolean ac, boolean ca) {
		
    	//X1
    	if(ab && ba && !ac && !ca ){
			return true;
		}
    	//X2
		else if(ab && ba && !ac && ca ){
			return true;
		}
    	//X3
		else if(ab && !ba && !ac && !ca ){
			return true;
		}
    	//X4
		else if(ab && !ba && !ac && ca ){
			return true;
		}
		return false;
	}

	private boolean checkEdge(Set<DiEdge> edges, Node node1, Node node2) {
		for (DiEdge edge : edges) {
			Node first = edge.getFirst();
			Node second = edge.getSecond();
			if(first.equals(node1) && second.equals(node2)){
				return true;
			}
		}
		return false;
	}

}
