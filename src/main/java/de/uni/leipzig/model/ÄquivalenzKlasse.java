package de.uni.leipzig.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.ToString;

 @Getter
 @ToString
public class ÄquivalenzKlasse {
	
	private Set<Node> nodes = new HashSet<>();
	
	public void hinzufügen(Node node){
		nodes.add(node);
	}
	
	public boolean contains(Node node){
		return nodes.contains(node);
	}
	
}
