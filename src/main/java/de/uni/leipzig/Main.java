package de.uni.leipzig;

import java.io.File;
import java.util.List;
import java.util.Set;

import de.uni.leipzig.colored.DiGraphExtractor;
import de.uni.leipzig.colored.axiom.Axioms;
import de.uni.leipzig.informative.InformativeTripleFinder;
import de.uni.leipzig.manipulation.Manipulation;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Reachables;
import de.uni.leipzig.model.Tree;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.parser.Parser;
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
            File file = getBlastGraphFile();
            if (file != null) {
                Parser parser = new Parser();
                DiGraph diGraph = parser.parse(file);

                UserInput parsed = new UserInput();
                parsed.register("equivalence class based", () -> {
                    equivalenceClassBased(diGraph);
                });
                parsed.askWithOptions("How do you want to create the LRT?");
            }
        });

        main.register("create a random tree", () -> {
            RandomTree randomTree = new RandomTree(3, 5, 2);
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
        Set<Triple> triples = tripleFinder.findTriple(adjList);

        System.out.println(triples.toString());

        // TODO triples random sortieren & zerstören - einlesen und wieder rausschreiben
        UserInput manipulate = new UserInput();

        manipulate.register("no", () -> {
        });

        manipulate.register("yes", () -> {
            Manipulation manipulation = new Manipulation(triples, tripleFinder);
            manipulation.apply();
        });

        manipulate.askWithOptions("Do you want to manipulate the triple set?");

        AhoBuild ahoBuild = new AhoBuild();
        Tree result = ahoBuild.build(triples, tripleFinder.getLeaves());

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
        Set<Triple> informativeTriples = informativeTripleFinder
                .findInformativeTriples(adjList);
        System.out.println(informativeTriples);

        AhoBuild ahoBuild = new AhoBuild();
        Tree result = ahoBuild.build(informativeTriples, informativeTripleFinder.getLeaves());

        System.out.println(result.toNewickNotation());
        System.out.println(result.print());
    }

    private File getBlastGraphFile() throws Exception {
        UserInput blastFile = new UserInput();
        System.out.println("Enter file path relative to your execution directory...");
        String result = blastFile.listenForResult();

        return new File(result);
    }
}
