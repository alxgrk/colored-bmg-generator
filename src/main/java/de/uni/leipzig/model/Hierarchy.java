package de.uni.leipzig.model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Hierarchy {
	Set<Set<Node>> sets;

	public Hierarchy(DiGraph graph) {
		this.sets = new HashSet<>();

		Set<Node> x = graph.getNodes();
		sets.add(x);
		for (ÄquivalenzKlasse äk : graph.getReachablesByÄk().keySet()) {
			sets.add(äk.getNodes());
		}
		for (Reachables r : graph.getReachablesByÄk().values()) {
			sets.add(r.getRq());
		}
		for (Node n : x) {
			sets.add(Sets.newHashSet(n));
		}

		System.out.println("Hierarchy set: " + sets);
	}

	public void build() {
		Set<Tree> trees = new HashSet<>();
		
		Set<Tree> leafs = sets.stream()
				.filter(s -> s.size() == 1)
				.map(Lists::newArrayList)
				.map(Tree::new)
				.collect(Collectors.toSet());
		
		sets.removeAll(leafs);
		
		for (Tree leaf : leafs) {
			
			Optional<Set<Node>> smallestSetContainingLeaf = sets.stream()
				.filter(s -> sets.containsAll(leaf.getNodes()))
				.min((s1, s2) -> Integer.compare(s1.size(), s2.size()));
			
			smallestSetContainingLeaf.ifPresent(s -> {
				Optional<Tree> optional = trees.stream()
					.filter(t -> s.containsAll(t.getNodes()))
					.findFirst();
				
				if (optional.isPresent()) {
					optional.get().addSubTree(leaf);
				} else {
					Tree tree = new Tree(Lists.newArrayList(s));
					tree.addSubTree(leaf);
					trees.add(tree);
				}
			});
			
		}
	}
}
