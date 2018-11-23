package de.uni.leipzig.model;

import static java.util.stream.Collectors.*;

import java.util.Set;

import javax.annotation.Nullable;

import de.uni.leipzig.model.edges.DiEdge;
import lombok.*;

@Getter
@ToString
public class Neighbourhood {

    private Set<DiEdge> edges;

    private final Set<Node> n1;

    private final Set<Node> nIn;

    @Nullable
    private Set<Node> n2;

    @Nullable
    private Set<Node> n3;

    public Neighbourhood(ThinnessClass tc, Set<DiEdge> edges) {
        this.edges = edges;
        n1 = tc.getOutNeighbours();
        nIn = tc.getInNeighbours();
    }

    private Set<Node> neighboursCalc(Set<Node> tc, Set<DiEdge> edges) {

        return edges.stream()
                .filter(e -> tc.contains(e.getFirst()))
                .collect(mapping(DiEdge::getSecond, toSet()));

    }

    public Set<Node> getN2() {
        if (n2 == null) {
            this.n2 = neighboursCalc(n1, edges);
        }

        return n2;
    }

    public Set<Node> getN3() {
        if (n3 == null) {
            this.n3 = neighboursCalc(getN2(), edges);
        }

        return n3;
    }

}
