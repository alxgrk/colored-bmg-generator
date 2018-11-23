package de.uni.leipzig.informative;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.jgrapht.alg.util.Pair;
import org.junit.Test;

import com.google.common.collect.*;

import de.uni.leipzig.informative.model.InformativeTriple;
import de.uni.leipzig.model.*;
import de.uni.leipzig.model.edges.*;

public class InformativeTripleFinder2Test {

    // @formatter:off
    // *
    // | \
    // *  \
    // | \ \
    // *  \ \
    // | \ \ \
    // * * * *
    // 1 2 3 4

    Node n0 = Node.of(1, Lists.newArrayList(0));
    Node n01 = Node.of(1, Lists.newArrayList(0, 1));
    Node n011 = Node.of(1, Lists.newArrayList(0, 1, 1));
    Node n0111 = Node.of(1, Lists.newArrayList(0, 1, 1, 1));
    Node n0112 = Node.of(0, Lists.newArrayList(0, 1, 1, 2));
    Node n012 = Node.of(1, Lists.newArrayList(0, 1, 2));
    Node n02 = Node.of(0, Lists.newArrayList(0, 2));

    DiEdge e1 = new DiEdge(n0111, n0112);
    DiEdge e2 = new DiEdge(n0112, n0111);
    DiEdge e3 = new DiEdge(n0112, n012);
    DiGraph diGraph = new DiGraph(
            Sets.newHashSet(n0111, n0112, n012, n02),
            Sets.newHashSet(e1, e2, e3));

    // @formatter:on

    @Test
    public void testFind() throws Exception {

        InformativeTripleFinder2 uut = new InformativeTripleFinder2();
        Pair<Set<InformativeTriple>, Set<Node>> actual = uut.findTriple(diGraph);

        assertThat(actual.getFirst()).allMatch(t -> t instanceof InformativeTriple);
        assertThat(actual.getFirst()).containsExactlyInAnyOrder(
                new InformativeTriple(new Edge(n0111, n0112), n02),
                new InformativeTriple(new Edge(n0112, n012), n02));
    }

}
