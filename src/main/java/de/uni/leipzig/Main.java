package de.uni.leipzig;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.jgrapht.alg.util.Pair;

import de.uni.leipzig.informative.InformativeTripleFinder;
import de.uni.leipzig.informative.model.InformativeTriple;
import de.uni.leipzig.manipulation.Manipulation;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Reachables;
import de.uni.leipzig.model.Tree;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.parser.BlastGraphParser;
import de.uni.leipzig.twocolored.DiGraphExtractor;
import de.uni.leipzig.twocolored.axiom.Axioms;
import de.uni.leipzig.uncolored.AhoBuild;
import de.uni.leipzig.uncolored.TripleFinder;
import de.uni.leipzig.user.UserInput;

public class Main {

    /**
     * Uses a random tree or a specified file ending with '.blast-graph'. Three possible
     * constructions methods are:<br>
     * <ul>
     * <li><b>aho</b> - the normal Aho-Build algorithm</li>
     * <li><b>equivalence-class-based</b> - calculates the least resolved tree using equivalence
     * classes, neighbourships and reachability relations</li>
     * <li><b>aho-informative</b> - calculates the least resolved tree using informative
     * triples</li>
     * </ul>
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        while (true) {
            UserInput repeat = new UserInput();
            new Main();

            repeat.register("quit", () -> {
                System.exit(0);
            });

            repeat.ask("Type 'quit' to exit the program - anything else will repeat it.");
        }
    }

    public Main() throws Exception {
        UserInput main = new UserInput();

        main.register("parse a file", () -> {
            BlastGraphParser blastGraphParser = new BlastGraphParser();
            File file = blastGraphParser.getBlastGraphFile();

            UserInput parsed = new UserInput();

            parsed.register("aho", () -> {
                Pair<Set<Node>, Set<Triple>> nodesAndTriples = blastGraphParser.parseTriple(file);

                ahoBuild(nodesAndTriples.getSecond(), nodesAndTriples.getFirst());
            });

            parsed.register("equivalence class based", () -> {
                DiGraph diGraph = blastGraphParser.parseDiGraph(file);

                System.out.println("Graph nodes: " + diGraph.getNodes());
                System.out.println("Graph edges: " + diGraph.getEdges());

                equivalenceClassBased(diGraph);
            });

            parsed.register("aho informative", () -> {
                Pair<Set<Node>, Set<Triple>> nodesAndTriples = blastGraphParser.parseTriple(file);
                DiGraph diGraph = blastGraphParser.parseDiGraph(file);

                ahoWithInformativeTriples(nodesAndTriples.getSecond(), diGraph);
            });

            parsed.askWithOptions("How do you want to create the LRT?");
        });

        main.register("create a random tree", () -> {

            RandomTree randomTree = configRandomTree();

            List<List<Node>> adjList = randomTree.create();

            UserInput random = new UserInput();
            random.register("aho", () -> {
                ahoBuild(adjList);
            });

            random.register("equivalence class based", () -> {
                equivalenceClassBased(adjList);
            });

            random.register("aho informative", () -> {
                ahoWithInformativeTriples(adjList);
            });

            random.askWithOptions("How do you want to create the LRT?");
        });

        main.askWithOptions("Do you want to...");
    }

    private void ahoBuild(List<List<Node>> adjList) {
        // **** Aho Build ohne informative triple ****
        TripleFinder tripleFinder = new TripleFinder();
        ahoBuild(tripleFinder.findTriple(adjList), tripleFinder.getLeaves());
    }

    private void ahoBuild(Set<Triple> triples, Set<Node> leaves) {
        // **** Aho Build ohne informative triple ****
        System.out.println(triples.toString());

        UserInput manipulate = new UserInput();

        manipulate.register("no", () -> {
        });

        manipulate.register("yes", () -> {
            Manipulation manipulation = new Manipulation(triples, leaves);
            manipulation.apply();
        });

        manipulate.askWithOptions("Do you want to manipulate the triple set?");

        AhoBuild ahoBuild = new AhoBuild();
        Tree result = ahoBuild.build(triples, leaves);

        System.out.println(result.toNewickNotation());
        System.out.println(result.print());
    }

    private void equivalenceClassBased(List<List<Node>> adjList) {
        // **** Equivalence class based part ****
        DiGraphExtractor extractor = new DiGraphExtractor();
        DiGraph graph = extractor.extract(adjList);
        equivalenceClassBased(graph);
    }

    private void equivalenceClassBased(DiGraph graph) {
        // **** Equivalence class based part ****

        System.out.println("Nodes: " + graph.getNodes());
        System.out.println("Edges: " + graph.getEdges());

        System.out.println("Neighbourhood: " + graph.getNeighboursByEc());
        graph.getReachablesByEc()
                .values()
                .stream()
                .map(Reachables::getRq)
                .forEach(rq -> System.out.println("RQ: " + rq));

        boolean axiomsFulfilled = Axioms.checkAll(graph);

        if (!axiomsFulfilled)
            throw new RuntimeException("Axioms not fulfilled!");

        Tree leastResolvedTree = graph.getHasseDiagram();
        System.out.println(leastResolvedTree);
        System.out.println(leastResolvedTree.print());
    }

    private void ahoWithInformativeTriples(List<List<Node>> adjList) {
        // **** Aho Build mit informative triple ****

        InformativeTripleFinder informativeTripleFinder = new InformativeTripleFinder();
        Set<InformativeTriple> informativeTriples = informativeTripleFinder
                .findInformativeTriples(adjList);

        ahoBuild(Util.uglyCast(informativeTriples), informativeTripleFinder.getLeaves());
    }

    private void ahoWithInformativeTriples(Set<Triple> triples, DiGraph diGraph) {
        // **** Aho Build mit informative triple ****

        InformativeTripleFinder informativeTripleFinder = new InformativeTripleFinder();
        Set<InformativeTriple> informativeTriples = informativeTripleFinder
                .findInformativeTriples(triples, diGraph);

        ahoBuild(Util.uglyCast(informativeTriples), informativeTripleFinder.getLeaves());
    }

    private RandomTree configRandomTree() {
        UserInput config = new UserInput();

        System.out.println("Maximum amount of children?");
        int maxChildren = Integer.parseInt(config.listenForResult());
        System.out.println("Maximum depth?");
        int maxDepth = Integer.parseInt(config.listenForResult());
        System.out.println("Maximum number of labels?");
        int maxLabel = Integer.parseInt(config.listenForResult());

        RandomTree randomTree = new RandomTree(maxChildren, maxDepth, maxLabel);

        boolean wasTriggered = config.askForTrigger(
                "Do you want to have a tree with maximal depth? "
                        + "(type 'y' or leave blank)", "y");
        randomTree.maximalDepth(wasTriggered);

        wasTriggered = config.askForTrigger(
                "Do you want to have every node having the maximal "
                        + "amount of children? (type 'y' or leave blank)", "y");
        randomTree.maximalNodesWithChildren(wasTriggered);
        return randomTree;
    }

}
