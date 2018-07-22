package de.uni.leipzig.model.edges;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.google.common.collect.Lists;

import de.uni.leipzig.model.Node;

public class DiEdgeTest {

    @Test
    public void testDiEdge() throws Exception {
        Node node1 = Node.of(1, Lists.newArrayList(1));
        Node node2 = Node.of(2, Lists.newArrayList(2));

        DiEdge uut = new DiEdge(node1, node2);

        assertThat(uut.toString()).isEqualTo("1-1->2-2");
    }

    @Test
    public void testDiEdge_DirectionRelevant() throws Exception {
        Node node1 = Node.of(1, Lists.newArrayList(1));
        Node node2 = Node.of(2, Lists.newArrayList(2));
        Node node3 = Node.of(3, Lists.newArrayList(3));

        DiEdge uut = new DiEdge(node1, node2);
        DiEdge equalDiEdge = new DiEdge(node2, node1);
        DiEdge differentDiEdge = new DiEdge(node1, node3);

        assertThat(uut).isNotEqualTo(equalDiEdge);
        assertThat(uut).isNotEqualTo(differentDiEdge);
        assertThat(equalDiEdge).isNotEqualTo(differentDiEdge);
    }
}
