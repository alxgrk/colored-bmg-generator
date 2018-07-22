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

        Triple uut = new Triple(new Edge(node1, node2), node3);

        assertThat(uut.toString()).isEqualTo("(0-1,1-2|0-3)");
    }

}
