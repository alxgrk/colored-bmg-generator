package de.uni.leipzig.ncolored.dengfernandezbaca;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.collect.*;
import com.google.common.collect.Sets.SetView;

import de.uni.leipzig.Util;
import de.uni.leipzig.model.*;
import de.uni.leipzig.model.edges.TreeEdge;
import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DFBBuildST {

    private final ConnectedComponentsConstructor componentsConstructor;

    public DFBBuildST() {
        this(new ConnectedComponentsConstructor());
    }

    public Tree build(Map<Set<Color>, Tree> inputU) throws IncompatibleProfileException {

        if (inputU.size() == 1)
            return inputU.values().iterator().next();

        // convert map
        Set<Tree> allTrees = Sets.newHashSet(inputU.values());
        Set<Tree> subTreesWithUAsRoot = Sets.newHashSet(inputU.values());

        return build(subTreesWithUAsRoot, allTrees, true);
    }

    public Tree build(Set<Tree> subTreesWithUAsRoot, Set<Tree> allTrees, boolean checkValidity)
            throws IncompatibleProfileException {

        // compute help map 'rootsU'
        Map<Node, Tree> rootsU = mapToRootNode(subTreesWithUAsRoot);

        // check validity
        if (checkValidity)
            checkValiditiy(subTreesWithUAsRoot, allTrees, rootsU);

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
        if (colorNumber == 2) {
            Iterator<Color> it = colorSet.iterator();
            Node a = Node.of(it.next().toString(), Lists.newArrayList());
            Node b = Node.of(it.next().toString(), Lists.newArrayList());
            rTree.addSubTree(new Tree(a));
            rTree.addSubTree(new Tree(b));
            return rTree;
        }

        // for each tree in map that consist of only one leaf
        for (Tree t : allTrees) {

            SetView<Node> vSet = Sets.intersection(rootsU.keySet(),
                    Sets.newHashSet(t.getAllNodes()));
            if (vSet.size() == 1) {
                Node v = vSet.iterator().next();
                Tree vTree = rootsU.get(v);
                List<Tree> childrenOfV = vTree.getSubTrees();

                rootsU.remove(v);
                Set<Tree> newSubTrees = Sets.newHashSet(rootsU.values());
                newSubTrees.addAll(childrenOfV);
                subTreesWithUAsRoot = newSubTrees;
                rootsU = mapToRootNode(subTreesWithUAsRoot);
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
            Tree tJ = build(c.getTrees(), allTrees, false);

            // if no exception was thrown, tJ is compatible
            rTree.addSubTree(tJ);
        }

        return rTree;
    }

    private Map<Node, Tree> mapToRootNode(Set<Tree> trees) {
        return trees.stream()
                .collect(Collectors.groupingBy(
                        Tree::getRoot,
                        Collectors.collectingAndThen(Collectors.toList(), l -> l.get(0))));
    }

    private TreeGraph createGpU(Set<Tree> subTreesWithUAsRoot) {
        Set<Tree> vertices = Sets.newHashSet(subTreesWithUAsRoot);
        Set<TreeEdge> edges = Sets.newHashSet();

        for (Tree v1 : vertices) {
            for (Tree v2 : vertices) {

                if (v1.equals(v2))
                    continue;

                if (!Sets.intersection(v1.getColors(), v2.getColors()).isEmpty()) {
                    edges.add(new TreeEdge(v1, v2));
                }

            }
        }

        return new TreeGraph(vertices, edges);
    }

    private void checkValiditiy(Set<Tree> subTreesWithUAsRoot, Set<Tree> allTrees,
            Map<Node, Tree> rootsU)
            throws IncompatibleProfileException {

        for (Tree t : allTrees) {

            Set<Node> vTi = Sets.newHashSet(t.getAllNodes());
            SetView<Node> uIs = Sets.intersection(rootsU.keySet(), vTi);

            if (uIs.size() >= 2) {

                // V1
                boolean v1Fulfilled = recursiveV1Check(uIs, Lists.newArrayList(t));

                if (!v1Fulfilled)
                    throw new IncompatibleProfileException();

                // V2
                Set<Color> uiColors = rootsU.entrySet()
                        .stream()
                        .filter(e -> uIs.contains(e.getKey()))
                        .map(Entry::getValue)
                        .flatMap(e -> e.getColors().stream())
                        .collect(Collectors.toSet());
                Set<Color> tiColors = t.getColors();
                Set<Color> uColors = subTreesWithUAsRoot.stream()
                        .flatMap(u -> u.getColors().stream())
                        .collect(Collectors.toSet());

                if (!Util.equalSets(uiColors, Sets.intersection(tiColors, uColors)))
                    throw new IncompatibleProfileException();
            }
        }
    }

    private boolean recursiveV1Check(SetView<Node> uIs, List<Tree> subTrees) {
        if (subTrees.isEmpty())
            return false;

        for (Tree tree : subTrees) {
            List<Node> vChildren = tree.getDirectChildren();
            if (vChildren.containsAll(uIs))
                return true;
        }
        return recursiveV1Check(uIs, subTrees.stream()
                .flatMap(t -> t.getSubTrees().stream())
                .collect(Collectors.toList()));
    }

    public class IncompatibleProfileException extends Exception {
        public IncompatibleProfileException() {
            super("Profile incompatible.");
        }
    }

}
