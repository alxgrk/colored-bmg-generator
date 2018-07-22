package de.uni.leipzig.model.edges;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.google.common.collect.Lists;

import de.uni.leipzig.model.Node;

import de.uni.leipzig.model.edges.Edge;

public class EdgeTest {

    @Test
    public void testEdge() throws Exception {
        Node node1 = Node.of(1, Lists.newArrayList(1));
        Node node2 = Node.of(2, Lists.newArrayList(2));

        Edge uut = new Edge(node1, node2);

        assertThat(uut.toString()).isEqualTo("1-1,2-2");
    }

    @Test
    public void testEdge_DirectionNotRelevant() throws Exception {
        Node node1 = Node.of(1, Lists.newArrayList(1));
        Node node2 = Node.of(2, Lists.newArrayList(2));
        Node node3 = Node.of(3, Lists.newArrayList(3));

        Edge uut = new Edge(node1, node2);
        Edge equalEdge = new Edge(node2, node1);
        Edge differentEdge = new Edge(node1, node3);

        assertThat(uut).isEqualTo(equalEdge);
        assertThat(uut).isNotEqualTo(differentEdge);
        assertThat(equalEdge).isNotEqualTo(differentEdge);
    }

}
