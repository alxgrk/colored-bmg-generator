package de.alxgrk;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import de.alxgrk.model.Edge;
import de.alxgrk.model.Node;
import de.alxgrk.model.Triple;
import lombok.Getter;

@Getter
public class FileReader {

    private Set<Triple> tripleSet;

    private TreeSet<Node> leaveSet = new TreeSet<>((c1, c2) -> c1.getValue().compareTo(c2
            .getValue()));

    public FileReader(File input) throws IOException {
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(input));) {
            tripleSet = extractTriples(reader);
        }

        filterLeaves();
    }

    private Set<Triple> extractTriples(BufferedReader reader) {
        return reader.lines()
                .map(l -> l.split("\\|"))
                .filter(l -> l.length > 1 && !l[0].isEmpty() && !l[1].isEmpty())
                .map(l -> {
                    String[] xySplit = l[0].split(",");
                    String x = xySplit[0];
                    String y = xySplit[1];
                    String z = l[1];

                    return new Triple(Edge.of(x, y), Node.of(z));
                })
                .collect(Collectors.toSet());
    }

    private void filterLeaves() {
        tripleSet.forEach(t -> {
            getLeaveSet().add(t.getEdge().getFirst());
            getLeaveSet().add(t.getEdge().getSecond());
            getLeaveSet().add(t.getNode());
        });
    }

}
