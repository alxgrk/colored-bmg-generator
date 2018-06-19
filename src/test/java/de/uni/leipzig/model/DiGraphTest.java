package de.uni.leipzig.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.Lists;
import org.junit.Test;

public class DiGraphTest {

    @Test
    public void testNodes() throws Exception {
        Node node1 = Node.of(1, Lists.newArrayList(1));
        Node node2 = Node.of(2, Lists.newArrayList(2));

        DiGraph uut = new DiGraph();
        uut.addNode(node1);
        uut.addNode(node1);
        uut.addNode(node2);

        assertThat(uut.getNodes()).containsExactlyInAnyOrder(node1, node2);
        assertThat(uut.getEdges()).isEmpty();
    }

    @Test
    public void testEdges() throws Exception {
        Node node1 = Node.of(1, Lists.newArrayList(1));
        Node node2 = Node.of(2, Lists.newArrayList(2));
        DiEdge edge1 = new DiEdge(node1, node2);
        DiEdge edge2 = new DiEdge(node2, node1);

        DiGraph uut = new DiGraph();
        uut.addEdge(edge1);
        uut.addEdge(edge1);
        uut.addEdge(edge2);

        assertThat(uut.getNodes()).isEmpty();
        assertThat(uut.getEdges()).containsExactlyInAnyOrder(edge1, edge2);
    }

}
