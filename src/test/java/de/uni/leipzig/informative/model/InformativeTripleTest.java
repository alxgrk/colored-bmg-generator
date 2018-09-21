package de.uni.leipzig.informative.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.Lists;
import org.junit.Test;

import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.edges.Edge;

public class InformativeTripleTest {

    @Test
    public void testToString() throws Exception {
        Node node1 = Node.of(0, Lists.newArrayList(1));
        Node node2 = Node.of(1, Lists.newArrayList(2));
        Node node3 = Node.of(0, Lists.newArrayList(3));

        InformativeTriple uut = new InformativeTriple(new Edge(node1, node2), node3);

        assertThat(uut.toString()).isEqualTo("(0-1,1-2|0-3)");
    }

    @Test
    public void testContains() throws Exception {
        Node node1 = Node.of(0, Lists.newArrayList(1));
        Node node2 = Node.of(1, Lists.newArrayList(2));
        Node node3 = Node.of(0, Lists.newArrayList(3));
        Node node4 = Node.of(1, Lists.newArrayList(4));

        InformativeTriple uut = new InformativeTriple(new Edge(node1, node2), node3);

        assertThat(uut.contains(node1)).isTrue();
        assertThat(uut.contains(node2)).isTrue();
        assertThat(uut.contains(node3)).isTrue();
        assertThat(uut.contains(node4)).isFalse();
    }

}
