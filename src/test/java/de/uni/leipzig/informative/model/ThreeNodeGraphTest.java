package de.uni.leipzig.informative.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.Lists;
import org.junit.Test;

import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.model.edges.DiEdge;
import de.uni.leipzig.model.edges.Edge;

public class ThreeNodeGraphTest {

    @Test
    public void testInitialization() throws Exception {
        Node node1 = Node.of(0, Lists.newArrayList(1));
        Node node2 = Node.of(1, Lists.newArrayList(2));
        Node node3 = Node.of(0, Lists.newArrayList(3));
        Triple triple = new Triple(new Edge(node1, node2), node3);

        ThreeNodeGraph uutByTriple = new ThreeNodeGraph(triple);
        ThreeNodeGraph uutByNodes = new ThreeNodeGraph(node1, node2, node3);

        assertThat(uutByNodes.getTriple()).isEqualTo(triple);
        assertThat(uutByNodes.getNodeFrequency())
                .containsEntry(node1, 0)
                .containsEntry(node2, 0)
                .containsEntry(node3, 0);

        assertThat(uutByNodes.getTriple()).isEqualTo(triple);
        assertThat(uutByTriple.getNodeFrequency())
                .containsEntry(node1, 0)
                .containsEntry(node2, 0)
                .containsEntry(node3, 0);
    }

    @Test
    public void testAdd() throws Exception {
        Node node1 = Node.of(0, Lists.newArrayList(1));
        Node node2 = Node.of(1, Lists.newArrayList(2));
        Node node3 = Node.of(0, Lists.newArrayList(3));
        Node node4 = Node.of(1, Lists.newArrayList(4));
        Node node5 = Node.of(0, Lists.newArrayList(5));
        DiEdge allowedEdge1 = new DiEdge(node1, node2);
        DiEdge allowedEdge2 = new DiEdge(node1, node3);

        ThreeNodeGraph uut = new ThreeNodeGraph(node1, node2, node3);
        boolean addedSucessfully1 = uut.add(allowedEdge1);
        boolean addedSucessfully2 = uut.add(allowedEdge2);
        boolean notAddedSucessfully1 = uut.add(new DiEdge(node4, node1));
        boolean notAddedSucessfully2 = uut.add(new DiEdge(node1, node4));
        boolean notAddedSucessfully3 = uut.add(new DiEdge(node4, node5));

        assertThat(addedSucessfully1).isTrue();
        assertThat(addedSucessfully2).isTrue();
        assertThat(notAddedSucessfully1).isFalse();
        assertThat(notAddedSucessfully2).isFalse();
        assertThat(notAddedSucessfully3).isFalse();
        assertThat(uut.getEdges()).containsExactly(allowedEdge1, allowedEdge2);
        assertThat(uut.getNodeFrequency())
                .containsEntry(node1, 2)
                .containsEntry(node2, 1)
                .containsEntry(node3, 1);
    }

}
