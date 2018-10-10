package de.uni.leipzig.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.alg.util.Pair;

import com.google.common.collect.Sets;

import de.uni.leipzig.model.DefaultTriple;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.model.edges.DiEdge;
import de.uni.leipzig.model.edges.Edge;
import de.uni.leipzig.user.Result;
import de.uni.leipzig.user.UserInput;

public class BlastGraphParser {

    public File getBlastGraphFile() throws Exception {
        File cwd = new File(System.getProperty("user.dir") + "/src");
        List<File> blastFiles = Files.find(cwd.toPath(), 10,
                (f, a) -> f.toFile().getName().endsWith(".blast-graph"))
                .map(Path::toFile)
                .collect(Collectors.toList());

        UserInput whichFile = new UserInput();

        final Result<File> blastFile = Result.empty();

        for (File f : blastFiles) {
            whichFile.register(f.getName(),
                    () -> {
                        blastFile.fixValue(f);
                    });
        }

        whichFile.register("custom path", () -> {
            System.out.println("enter path: ");
            blastFile.fixValue(new File(whichFile.listenForResult()));
        });

        whichFile.askWithOptions(
                "Choose a file or enter file path relative to your execution directory...");

        return blastFile.getValue();
    }

    public DiGraph parseDiGraph(File input) throws IOException {

        if (!input.exists() || !input.canRead()) {
            throw new IllegalArgumentException("File not readable!!");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(input));) {

            Set<Node> nodes = Sets.newHashSet();
            Set<DiEdge> edges = Sets.newHashSet();

            reader.lines()
                    .filter(f -> !f.startsWith("#"))
                    .map(l -> new BlastGraphLine(l))
                    .forEach(l -> {
                        Node node1 = l.getGene1().asNode();
                        Node node2 = l.getGene2().asNode();
                        nodes.add(node1);
                        nodes.add(node2);

                        edges.add(new DiEdge(node1, node2));
                        edges.add(new DiEdge(node2, node1));
                    });

            return new DiGraph(nodes, edges);
        }
    }

    public Pair<Set<Triple>, Set<Node>> parseTriple(File input) throws IOException {

        if (!input.exists() || !input.canRead()) {
            throw new IllegalArgumentException("File not readable!!");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(input));) {

            Set<Triple> triples = Sets.newHashSet();
            Set<Node> nodes = Sets.newHashSet();

            reader.lines()
                    .filter(f -> !f.startsWith("#"))
                    .map(l -> new BlastGraphLine(l))
                    .peek(l -> {
                        nodes.add(l.getGene1().asNode());
                        nodes.add(l.getGene2().asNode());
                    })
                    .forEach(l -> {
                        Node node1 = l.getGene1().asNode();
                        Node node2 = l.getGene2().asNode();

                        nodes.forEach(n -> {

                            if (!n.equals(node1) && !n.equals(node2)
                                    && oneWithEqualLabel(n, node1, node2)) {

                                Triple t1 = new DefaultTriple(new Edge(node1, node2), n);
                                Triple t2 = new DefaultTriple(new Edge(node2, node1), n);

                                // FIXME only use triples that make sense (for ab|c, c needs to be
                                // selected carefully)

                                triples.add(t1);
                                triples.add(t2);
                            }
                        });
                    });

            return new Pair<>(triples, nodes);
        }
    }

    private boolean oneWithEqualLabel(Node ofInterest, Node n1, Node n2) {
        return (ofInterest.getLabel().equals(n1.getLabel())
                && !ofInterest.getLabel().equals(n2.getLabel()))
                || (ofInterest.getLabel().equals(n2.getLabel())
                        && !ofInterest.getLabel().equals(n1.getLabel()));
    }

}
