package de.uni.leipzig;

import static de.uni.leipzig.method.TreeCreation.Method.*;

import java.io.File;
import java.util.Set;

import org.jgrapht.alg.util.Pair;

import de.uni.leipzig.model.*;
import de.uni.leipzig.parser.BlastGraphParser;
import de.uni.leipzig.user.UserInput;

public class Main {

    /**
     * Uses a random tree or a specified file ending with '.blast-graph'. Three possible
     * constructions methods are:<br>
     * <ul>
     * <li><b>aho</b> - the normal Aho-Build algorithm</li>
     * <li><b>thinness-based</b> - calculates the least resolved tree using thinness classes,
     * neighbourships and reachability relations</li>
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

            UserInput main = new UserInput();
            new Main(main);

            repeat.register("quit", () -> {
                System.exit(0);
            });

            repeat.ask("Type 'quit' to exit the program - anything else will repeat it.");
        }
    }

    public Main(UserInput main) throws Exception {

        main.register("parse a file", () -> {
            BlastGraphParser blastGraphParser = new BlastGraphParser();
            File file = blastGraphParser.getBlastGraphFile().getValue();

            // FIXME if >2 colors then ...
            // something like NColored.pass(this::thinnessClassBased, ...)

            UserInput parsed = new UserInput();

            parsed.register(AHO, aho -> {
                Pair<Set<Triple>, Set<Node>> nodesAndTriples = blastGraphParser.parseTriple(file);

                aho.create(nodesAndTriples.getFirst(), nodesAndTriples.getSecond());
            });

            parsed.register(THINNESS_CLASS, tc -> {
                DiGraph diGraph = blastGraphParser.parseDiGraph(file);

                tc.create(diGraph);
            });

            parsed.register(AHO_INFORMATIVE, inf -> {
                Pair<Set<Triple>, Set<Node>> nodesAndTriples = blastGraphParser.parseTriple(file);
                DiGraph diGraph = blastGraphParser.parseDiGraph(file);

                inf.create(nodesAndTriples.getFirst(), diGraph);
            });

            parsed.askWithOptions("How do you want to create the LRT?");
        });

        main.register("create a random tree", () -> {

            UserInput config = new UserInput();
            RandomTree randomTree = RandomTree.askRandomTreeConfig(config);

            // FIXME if >2 colors then ...
            // something like NColored.pass(this::thinnessClassBased, ...)

            AdjacencyList adjList = randomTree.create();

            UserInput random = new UserInput();
            random.register(AHO, adjList);

            random.register(THINNESS_CLASS, adjList);

            random.register(AHO_INFORMATIVE, adjList);

            random.askWithOptions("How do you want to create the LRT?");
        });

        main.askWithOptions("Do you want to...");
    }

}
