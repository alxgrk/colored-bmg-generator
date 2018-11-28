package de.uni.leipzig.uncolored;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.jgrapht.alg.util.Pair;
import org.junit.Test;

import com.google.common.collect.Lists;

import de.uni.leipzig.model.*;
import de.uni.leipzig.model.edges.DiEdge;

public class DefaultTripleFinderTest {

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

    AdjacencyList adjList = new AdjacencyList(
            Lists.newArrayList(
                Lists.newArrayList(n0, n01, n02),
                Lists.newArrayList(n01, n011, n012),
                Lists.newArrayList(n011, n0111, n0112),
                Lists.newArrayList(n0111),
                Lists.newArrayList(n0112),
                Lists.newArrayList(n012),
                Lists.newArrayList(n02))
            );

    // @formatter:on

    @Test
    public void testFind() throws Exception {

        DefaultTripleFinder uut = new DefaultTripleFinder();
        Pair<Set<Triple>, Set<Node>> actual = uut.findTriple(adjList);

        assertThat(actual.getFirst()).containsExactlyInAnyOrder(
                new DefaultTriple(new DiEdge(n0111, n0112), n02),
                new DefaultTriple(new DiEdge(n0112, n0111), n012),
                new DefaultTriple(new DiEdge(n012, n0112), n02));
    }

}
