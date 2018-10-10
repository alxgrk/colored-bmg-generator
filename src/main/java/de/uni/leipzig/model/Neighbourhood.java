package de.uni.leipzig.model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import de.uni.leipzig.model.edges.DiEdge;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
public class Neighbourhood {
    Set<Node> n1;

    Set<Node> n2;

    Set<Node> n3;

    Set<Node> nIn;

    public Neighbourhood(ThinnessClass tc, Set<DiEdge> edges) {
        n1 = neighboursCalc(tc, edges);
        n2 = neighboursCalc(n1, edges);
        n3 = neighboursCalc(n2, edges);
        nIn = inNeighboursOf(tc, edges);
    }

    private Set<Node> neighboursCalc(ThinnessClass tc, Set<DiEdge> edges) {
        return neighboursCalc(tc.getNodes(), edges);
    }

    private Set<Node> neighboursCalc(Set<Node> tc, Set<DiEdge> edges) {
        Set<Node> neighbours = new HashSet<>();

        Optional<Node> any = tc.stream().findAny();
        any.ifPresent(n -> {

            for (DiEdge diEdge : edges) {

                if (diEdge.getFirst() == n)
                    neighbours.add(diEdge.getSecond());
            }
        });

        return neighbours;
    }

    private Set<Node> inNeighboursOf(ThinnessClass tc, Set<DiEdge> edges) {
        Set<Node> neighbours = new HashSet<>();

        Optional<Node> any = tc.getNodes().stream().findAny();
        any.ifPresent(n -> {

            for (DiEdge diEdge : edges) {

                if (diEdge.getSecond() == n)
                    neighbours.add(diEdge.getFirst());
            }
        });

        return neighbours;
    }
}
