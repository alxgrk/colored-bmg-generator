package de.uni.leipzig.ncolored;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import de.uni.leipzig.model.Color;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Tree;

public class GeneToSpecies {
	
	List<List<Node>> Adjazenzlist = new ArrayList<>();
	
	public Tree transform(Tree geneTree){
		
		List<Node> start = new ArrayList<>();
		start.add(geneTree.getRoot());
		
		PathFinder(geneTree, start);
		
		Tree speciesTree = create();
		
		return speciesTree;
		
	}


	private Tree create() {
		
		Tree STree = new Tree();
		
		
		int longestList = 0;
		for(List<Node> list: Adjazenzlist){
			if(list.size() > longestList){
				longestList = list.size();
			}
		}
		for(int i = 0; i < longestList; i++){
			
			Set<Node> opt = new HashSet<>();
			for (List<Node> list : Adjazenzlist) {
				opt.add(list.get(i));
			}
			
			
		}
			
		return STree;
	}


	private void PathFinder(Tree subTree, List<Node> path) {
		
		List<Node> longerPath = path;
		Node step = subTree.getRoot();
		
		if(subTree.getSubTrees().isEmpty()){
			longerPath.add(step);
			Adjazenzlist.add(longerPath);
			return; 
		}
		
		step = speciationOrDublication(subTree);
		
		longerPath.add(step);
		
		for (Tree t : subTree.getSubTrees()) {
			PathFinder(t, longerPath);
		}
	}


	private Node speciationOrDublication(Tree tree) {
		
		for (Tree subTree1 : tree.getSubTrees()) {
			for (Tree subTree2 : tree.getSubTrees()) {
				
				if(subTree1.equals(subTree2))
					continue;
				
				Set<Color> colorSet1 = subTree1.getColors();
				Set<Color> colorSet2 = subTree2.getColors();
				
				List<Integer> id = new ArrayList<>(111);
				
				if(!Sets.intersection(colorSet1, colorSet2).isEmpty() && !Sets.intersection(colorSet2, colorSet1).isEmpty()){
					return Node.of("Dubli", id);
				} else {
					return Node.of("Speci", id); 
				}
			}
		}
		return null;
	}		
}
	
