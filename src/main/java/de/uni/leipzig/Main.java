package de.uni.leipzig;

import java.io.File;
import java.util.Set;

import org.jgrapht.alg.util.Pair;

import com.google.common.annotations.VisibleForTesting;

import static de.uni.leipzig.method.TreeCreation.Method.AHO;
import static de.uni.leipzig.method.TreeCreation.Method.AHO_INFORMATIVE;
import static de.uni.leipzig.method.TreeCreation.Method.THINNESS_CLASS;

import de.uni.leipzig.method.TreeCreation;
import de.uni.leipzig.model.AdjacencyList;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Tree;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.ncolored.NColored;
import de.uni.leipzig.parser.BlastGraphParser;
import de.uni.leipzig.twocolored.DiGraphExtractor;
import de.uni.leipzig.user.UserInput;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

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

            try {
                Main main = new Main();
                main.run();
            } catch (Throwable e) {
                System.err.println(e);
                // just restart the programm
            }

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

            if (twoColored) {
                ui.register(AHO, aho -> {
                    Pair<Set<Triple>, Set<Node>> nodesAndTriples = blastGraphParser.parseTriple(
                            file);

                    aho.create(nodesAndTriples.getFirst(), nodesAndTriples.getSecond());
                });

                ui.register(THINNESS_CLASS, tc -> {
                    DiGraph diGraph = blastGraphParser.parseDiGraph(file);

                    tc.create(diGraph);
                });

                ui.register(AHO_INFORMATIVE, inf -> {
                    DiGraph diGraph = blastGraphParser.parseDiGraph(file);

                    inf.create(diGraph);
                });
            } else {
                ui.register(THINNESS_CLASS, tc -> {
                    TreeCreation.askForInteractiveMode(tc, ui);

                    DiGraph diGraph = blastGraphParser.parseDiGraph(file);

                    Tree result = nColored.by(g -> tc.create(g), diGraph);

                    System.out.println(result.print());
                });

                ui.register(AHO_INFORMATIVE, inf -> {
                    TreeCreation.askForInteractiveMode(inf, ui);

                    DiGraph diGraph = blastGraphParser.parseDiGraph(file);

                    Tree result = nColored.by(g -> inf.create(g),
                            diGraph);

                    System.out.println(result.print());
                });
            }

            ui.askWithOptions("How do you want to create the LRT?");
        });

        ui.register("create a random tree", () -> {

            ui.clear();
            RandomTree randomTree = RandomTree.askRandomTreeConfig(ui);

            boolean twoColored = randomTree.maxLabel() <= 2;

            AdjacencyList adjList = randomTree.create();
            ui.clear();

            if (twoColored) {
                ui.register(AHO, adjList);
                ui.register(THINNESS_CLASS, adjList);
                ui.register(AHO_INFORMATIVE, adjList);
            } else {

                AHO.get().create(adjList);

                ui.register(THINNESS_CLASS, tc -> {
                    TreeCreation.askForInteractiveMode(tc, ui);

                    DiGraphExtractor diGraphExtractor = new DiGraphExtractor();
                    Tree result = nColored.by(g -> tc.create(adjList),
                            diGraphExtractor.extract(adjList));

                    System.out.println(result.print());
                });

                ui.register(AHO_INFORMATIVE, inf -> {
                    TreeCreation.askForInteractiveMode(inf, ui);

                    DiGraphExtractor diGraphExtractor = new DiGraphExtractor();
                    Tree result = nColored.by(g -> inf.create(adjList),
                            diGraphExtractor.extract(adjList));

                    System.out.println(result.print());
                });
            }

            ui.askWithOptions("How do you want to create the LRT?");
        });

        ui.askWithOptions("Do you want to...");
    }

}
