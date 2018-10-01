package de.uni.leipzig.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.Lists;
import org.junit.Test;

import de.uni.leipzig.model.edges.Edge;

public class TripleTest {

    @Test
    public void testToString() throws Exception {
        Node node1 = Node.of(0, Lists.newArrayList(1));
        Node node2 = Node.of(1, Lists.newArrayList(2));
        Node node3 = Node.of(0, Lists.newArrayList(3));

        Triple uut = new DefaultTriple(new Edge(node1, node2), node3);

        assertThat(uut.toString()).isEqualTo("(0-1,1-2|0-3)");
    }

    @Test
    public void testContains() throws Exception {
        Node node1 = Node.of(0, Lists.newArrayList(1));
        Node node2 = Node.of(1, Lists.newArrayList(2));
        Node node3 = Node.of(0, Lists.newArrayList(3));
        Node node4 = Node.of(1, Lists.newArrayList(4));

        Triple uut = new DefaultTriple(new Edge(node1, node2), node3);

        assertThat(uut.contains(node1)).isTrue();
        assertThat(uut.contains(node2)).isTrue();
        assertThat(uut.contains(node3)).isTrue();
        assertThat(uut.contains(node4)).isFalse();
    }

    @Test
    public void testEquals_InvertedEdges() throws Exception {
        Node node1 = Node.of(0, Lists.newArrayList(1));
        Node node2 = Node.of(1, Lists.newArrayList(2));
        Node node3 = Node.of(0, Lists.newArrayList(3));

        Triple uut1 = new DefaultTriple(new Edge(node1, node2), node3);
        Triple uut2 = new DefaultTriple(new Edge(node2, node1), node3);

        assertThat(uut1).isEqualTo(uut2);
    }

    @Test
    public void testInvertEdges() throws Exception {
        Node node1 = Node.of(0, Lists.newArrayList(1));
        Node node2 = Node.of(1, Lists.newArrayList(2));
        Node node3 = Node.of(0, Lists.newArrayList(3));

        Triple uut = new DefaultTriple(new Edge(node1, node2), node3);
        uut.invertEdge();

        assertThat(uut.getEdge())
                .extracting("first", "second")
                .contains(node2, node1);
    }

}
