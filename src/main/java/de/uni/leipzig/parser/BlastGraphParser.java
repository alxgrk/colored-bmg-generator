package de.uni.leipzig.parser;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jgrapht.alg.util.Pair;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;

import de.uni.leipzig.model.*;
import de.uni.leipzig.model.edges.*;
import de.uni.leipzig.user.*;

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
                (f, a) -> f.toFile().getName().endsWith(".blast-graph"))
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
                    .reduce(new HashMap<String, AtomicInteger>(),
                            (m, l) -> {
                                String l1 = l.getGene1().asNode().getLabel();
                                String l2 = l.getGene2().asNode().getLabel();

                                increment(m, l1);
                                increment(m, l2);

                                return m;
                            }, (m1, m2) -> {
                                m1.putAll(m2);
                                return m1;
                            })
                    .size();
        }
    }

    private void increment(Map<String, AtomicInteger> m, String l) {
        AtomicInteger v = m.get(l);
        if (v == null) {
            m.put(l, new AtomicInteger(0));
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

    private void fileCheck(File input) {
        if (!input.exists() || !input.canRead()) {
            throw new IllegalArgumentException("File not readable!!");
        }
    }

    private boolean oneWithEqualLabel(Node ofInterest, Node n1, Node n2) {
        return (ofInterest.getLabel().equals(n1.getLabel())
                && !ofInterest.getLabel().equals(n2.getLabel()))
                || (ofInterest.getLabel().equals(n2.getLabel())
                        && !ofInterest.getLabel().equals(n1.getLabel()));
    }

}
