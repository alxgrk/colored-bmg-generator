package de.uni.leipzig.ncolored.dengfernandezbaca;

import java.util.*;

import com.google.common.collect.*;

import de.uni.leipzig.model.*;

public class DFBBuildST {

    public Tree build(Map<Pair<Color>, Tree> inputU) throws IncompatibleProfileException {
        List<Color> colors = Lists.newArrayList();
        DiGraph diGraph = new DiGraph(Sets.newHashSet(), Sets.newHashSet());

        // convert map

        return build(colors, diGraph);
    }

    public Tree build(List<Color> colors, DiGraph inputU) throws IncompatibleProfileException {

        Node rU = Node.helpNode();
        Tree tU = new Tree(rU);

        // determine distinct color number
        int colorNumber = 0; // FIXME calculate

        // for one-colored profiles
        if (colorNumber == 1)
            return tU; // FIXME take only node in inputU

        // for two-colored profiles
        if (colorNumber == 2)
            return tU; // FIXME take rU and append nodes from inputU

        // for each tree in map that consist of only one leaf
        {
            // do not manipulate the tree (only node 'v')
            // inputU = 'inputU without v' U 'children of v'
        }

        // connected components of inputU
        List<Tree> components = Lists.newArrayList(); // FIXME

        if (components.size() == 1)
            throw new IncompatibleProfileException();

        for (Tree c : components) {

            // recursive call
            Tree tJ = build(colors, inputU); // FIXME

            // if no exception was thrown, tJ is compatible
            tU.addSubTree(tJ);
        }

        return tU;
    }

    public class IncompatibleProfileException extends Exception {
        public IncompatibleProfileException() {
            super("Profile incompatible.");
        }
    }

}
