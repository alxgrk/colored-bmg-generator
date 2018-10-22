package de.uni.leipzig.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jgrapht.alg.util.Pair;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;

import de.uni.leipzig.model.Color;
import de.uni.leipzig.model.DefaultTriple;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.model.edges.DiEdge;
import de.uni.leipzig.model.edges.Edge;
import de.uni.leipzig.user.Result;
import de.uni.leipzig.user.UserInput;

public class BlastGraphParser {

    private final Predicate<? super String> commentedLines = f -> !f.startsWith("#");

    public Result<File> getBlastGraphFile() throws Exception {
        return getBlastGraphFile(
                new File(System.getProperty("user.dir") + "/src"),
                new UserInput());
    }

    @VisibleForTesting
    protected Result<File> getBlastGraphFile(File cwd, UserInput whichFile) throws Exception {
        List<File> blastFiles = Files.find(cwd.toPath(), 10,
                (f, a) -> f.toFile().getName().endsWith(".blast-graph") || f.toFile().getName().endsWith(".ffadj-graph"))
                .map(Path::toFile)
                .collect(Collectors.toList());

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

        return blastFile;
    }

    public Integer colorsIn(File input) throws IOException {

        fileCheck(input);

        try (BufferedReader reader = new BufferedReader(new FileReader(input));) {

            return reader.lines()
                    .filter(commentedLines)
                    .map(BlastGraphLine::new)
                    .reduce(new HashMap<Color, AtomicInteger>(),
                            (m, l) -> {
                                Color s = l.getGene1().asNode().getColor();
                                Color t = l.getGene2().asNode().getColor();

                                increment(m, s);
                                increment(m, t);

                                return m;
                            }, (m1, m2) -> {
                                m1.putAll(m2);
                                return m1;
                            })
                    .size();
        }
    }

    private void increment(Map<Color, AtomicInteger> m, Color c) {
        AtomicInteger v = m.get(c);
        if (v == null) {
            m.put(c, new AtomicInteger(0));
        } else {
            v.incrementAndGet();
        }
    }

    public DiGraph parseDiGraph(File input) throws IOException {

        fileCheck(input);

        try (BufferedReader reader = new BufferedReader(new FileReader(input));) {

            Set<Node> nodes = Sets.newHashSet();
            Set<DiEdge> edges = Sets.newHashSet();

            reader.lines()
                    .filter(commentedLines)
                    .map(BlastGraphLine::new)
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

        fileCheck(input);

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
                                    && oneWithEqualColor(n, node1, node2)) {

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

    private void fileCheck(File input) {
        if (!input.exists() || !input.canRead()) {
            throw new IllegalArgumentException("File not readable!!");
        }
    }

    private boolean oneWithEqualColor(Node ofInterest, Node n1, Node n2) {
        return (ofInterest.getColor().equals(n1.getColor())
                && !ofInterest.getColor().equals(n2.getColor()))
                || (ofInterest.getColor().equals(n2.getColor())
                        && !ofInterest.getColor().equals(n1.getColor()));
    }

}
