package de.uni.leipzig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Tree;

public class RandomTree2 {
	
    private int maxLabel;
	private int maxChildren;


    public RandomTree2(int maxChildren, int maxLabel){
		this.maxChildren = maxChildren;
		this.maxLabel = maxLabel;
		
		createRandomTree(true);
    }
    
	private Tree createRandomTree(Boolean shouldHaveChildren) {
				
		List<Node> nodeList = new ArrayList<>();
		
		for (int i = 0; i < randomChildren()/2; i++) {
			ArrayList<Integer> id = new ArrayList<>();
			id.add(0);
			Node node = Node.of(randomLabel(), id);
			nodeList.add(node);
		}
		
		if (shouldHaveChildren == true) {
			
			List<Tree> subTreeList = new ArrayList<>();
			
			for (int i = 0; i < randomChildren()/2; i++) {
                subTreeList.add(createRandomTree(shouldHaveChildren));
			}
        }	
    	List<Tree> subTreeList = new ArrayList<>();
    	Tree tree = new Tree(subTreeList, nodeList);
        	
		return tree;
    }
    
    private int randomChildren() {
    	Random rand = new Random();
        return rand.nextInt(maxChildren);
	}

	private int randomLabel() {
        Random rand = new Random();
        return rand.nextInt(maxLabel);
    }

}
