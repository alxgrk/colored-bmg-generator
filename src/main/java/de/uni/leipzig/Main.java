package de.uni.leipzig;

import java.io.IOException;
import java.util.List;

import de.uni.leipzig.colored.DiGraphExtractor;
import de.uni.leipzig.model.Node;

public class Main {

    public static void main(String[] args) throws IOException {

        RandomTree randomTree = new RandomTree(2, 5, 2);
        List<List<Node>> adjList = randomTree.create();

        /*
         * TripleFinder tripleFinder = new TripleFinder(); List<Triple> triple =
         * tripleFinder.findTriple(adjList);
         * 
         * System.out.println(triple.toString());
         * 
         * Tree result = AhoBuild.build(new HashSet<>(triple), new
         * ArrayList<>(tripleFinder.getLeaves()));
         * 
         * System.out.println(result.toNewickNotation());
         */

        DiGraphExtractor extractor = new DiGraphExtractor();

        System.out.println(extractor.extract(adjList));
    }

}