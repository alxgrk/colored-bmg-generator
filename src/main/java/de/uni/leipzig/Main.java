package de.uni.leipzig;

import static de.uni.leipzig.method.TreeCreation.Method.*;

import java.io.File;
import java.util.Set;

import org.jgrapht.alg.util.Pair;

import com.google.common.annotations.VisibleForTesting;

import de.uni.leipzig.model.*;
import de.uni.leipzig.ncolored.NColored;
import de.uni.leipzig.parser.BlastGraphParser;
import de.uni.leipzig.user.UserInput;
import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED, onConstructor = @__(@VisibleForTesting))
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

            Main main = new Main();
            main.run();

            repeat.register("quit", () -> {
                System.exit(0);
            });

            repeat.ask("Type 'quit' to exit the program - anything else will repeat it.");
        }
    }

    private final UserInput ui;

    private final BlastGraphParser blastGraphParser;

    private final NColored nColored;

    public Main() {
        this(new UserInput(), new BlastGraphParser(), new NColored());
    }

    public void run() throws Exception {

        ui.register("parse a file", () -> {
            File file = blastGraphParser.getBlastGraphFile().getValue();

            boolean twoColored = blastGraphParser.colorsIn(file) <= 2;

            ui.clear();

            ui.register(AHO, aho -> {
                Pair<Set<Triple>, Set<Node>> nodesAndTriples = blastGraphParser.parseTriple(file);

                aho.create(nodesAndTriples.getFirst(), nodesAndTriples.getSecond());
            });

            ui.register(THINNESS_CLASS, tc -> {
                DiGraph diGraph = blastGraphParser.parseDiGraph(file);

                if (twoColored) {
                    tc.create(diGraph);
                } else {
                    nColored.by(g -> tc.create(g), diGraph);
                }
            });

            ui.register(AHO_INFORMATIVE, inf -> {
                Pair<Set<Triple>, Set<Node>> nodesAndTriples = blastGraphParser.parseTriple(file);
                DiGraph diGraph = blastGraphParser.parseDiGraph(file);

                inf.create(nodesAndTriples.getFirst(), diGraph);
            });

            ui.askWithOptions("How do you want to create the LRT?");
        });

        ui.register("create a random tree", () -> {

            ui.clear();
            RandomTree randomTree = RandomTree.askRandomTreeConfig(ui);

            // FIXME if >2 colors then ...
            // something like NColored.pass(this::thinnessClassBased, ...)

            AdjacencyList adjList = randomTree.create();

            ui.clear();
            ui.register(AHO, adjList);
            ui.register(THINNESS_CLASS, adjList);
            ui.register(AHO_INFORMATIVE, adjList);

            ui.askWithOptions("How do you want to create the LRT?");
        });

        ui.askWithOptions("Do you want to...");
    }

}
