package de.uni.leipzig.ncolored.dengfernandezbaca;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import de.uni.leipzig.model.Color;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Pair;
import de.uni.leipzig.model.Tree;
import de.uni.leipzig.model.TreeGraph;
import de.uni.leipzig.model.edges.TreeEdge;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DFBBuildST {

	private final ConnectedComponentsConstructor componentsConstructor;
	
	public DFBBuildST() {
		this(new ConnectedComponentsConstructor());
	}
	
    public Tree build(Map<Pair<Color>, Tree> inputU) throws IncompatibleProfileException {

        // convert map
        Set<Tree> allTrees = Sets.newHashSet(inputU.values());
        Set<Tree> allTreesCopy = Sets.newHashSet(inputU.values());

        return build(allTrees, allTreesCopy, false);
    }

    public Tree build(Set<Tree> subTreesWithUAsRoot, Set<Tree> allTrees, boolean checkValidity) throws IncompatibleProfileException {

    	// check validity
    	if (checkValidity)
    		checkValiditiy(subTreesWithUAsRoot, allTrees);
    	
        Node rU = Node.helpNode();
    	Tree rTree = new Tree(rU);

        // determine distinct color number
        Set<Color> colorSet = subTreesWithUAsRoot.stream()
			.flatMap(t -> t.getColors().stream())
			.collect(Collectors.toSet());
        long colorNumber = colorSet.size();

        // for one-colored profiles
        if (colorNumber == 1) {
        	rU = Node.of(colorSet.iterator().next().toString(), Lists.newArrayList());
        	return new Tree(rU);
        }

        // for two-colored profiles
        if (colorNumber == 2){
        	Node a = Node.of(colorSet.iterator().next().toString(), Lists.newArrayList());
        	Node b = Node.of(colorSet.iterator().next().toString(), Lists.newArrayList());
        	rTree.addSubTree(new Tree(a));
        	rTree.addSubTree(new Tree(b));
			return rTree;
        }
        
        // for each tree in map that consist of only one leaf
        for (Tree t : allTrees) {
        	
        	Map<Node, List<Tree>> rootsU = subTreesWithUAsRoot.stream()
        		.collect(Collectors.groupingBy(Tree::getRoot));
        	SetView<Node> vSet = Sets.intersection(rootsU.keySet(), Sets.newHashSet(t.getAllNodes()));
			if (vSet.size() == 1) {
        		Node v = vSet.iterator().next();
        		Tree vTree = rootsU.get(v).get(0);
				List<Tree> childrenOfV = vTree.getSubTrees();
        		
        		childrenOfV.forEach(subTreesWithUAsRoot::add);
        		subTreesWithUAsRoot.remove(vTree);
        	}
        	
        }

        // create GpU
        TreeGraph gpu = createGpU(subTreesWithUAsRoot);
        
        // connected components of inputU
        List<TreeGraph> components = componentsConstructor.construct(gpu);

        if (components.size() == 1)
            throw new IncompatibleProfileException();

        for (TreeGraph c : components) {

            // recursive call
            Tree tJ = build(c.getTrees(), allTrees, true); 

            // if no exception was thrown, tJ is compatible
            rTree.addSubTree(tJ);
        }

        return rTree;
    }

    private TreeGraph createGpU(Set<Tree> subTreesWithUAsRoot) {
    	Set<Tree> vertices = Sets.newHashSet(subTreesWithUAsRoot);
    	Set<TreeEdge> edges = Sets.newHashSet();
    	
    	for (Tree v1 : vertices) {
    		for (Tree v2 : vertices) {
    			
    			if (v1.equals(v2))
    				continue;
    			
    			if (!Sets.intersection(v1.getColors(), v2.getColors()).isEmpty() 
    					&& !Sets.intersection(v2.getColors(), v1.getColors()).isEmpty()) {
    				edges.add(new TreeEdge(v1, v2));
    			}
    			
    		}
		}
    	
    	return new TreeGraph(vertices, edges);
	}

	private void checkValiditiy(Set<Tree> subTreesWithUAsRoot, Set<Tree> allTrees) {
		
        for (Tree t : allTrees) {
        	
        	Map<Node, List<Tree>> rootsU = subTreesWithUAsRoot.stream()
        		.collect(Collectors.groupingBy(Tree::getRoot));
        	Set<Node> vTi = Sets.newHashSet(t.getAllNodes());
			SetView<Node> uIs = Sets.intersection(rootsU.keySet(), vTi);
        	
        	if(uIs.size() >= 2) {
        		for (Node v : vTi) {
        			
        		}
        	}
        	
        }
	}

	@SuppressWarnings("serial")
	public class IncompatibleProfileException extends Exception {
        public IncompatibleProfileException() {
            super("Profile incompatible.");
        }
    }

}
