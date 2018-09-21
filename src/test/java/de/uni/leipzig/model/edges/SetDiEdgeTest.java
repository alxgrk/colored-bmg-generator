package de.uni.leipzig.model.edges;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uni.leipzig.model.Node;

public class SetDiEdgeTest {

    @Test
    public void testSetDiEdge() throws Exception {
        Node node1 = Node.of(1, Lists.newArrayList(1));
        Node node2 = Node.of(2, Lists.newArrayList(2));

        SetDiEdge uut = new SetDiEdge(Sets.newHashSet(node1), Sets.newHashSet(node2));

        assertThat(uut.toString()).isEqualTo("[1-1]->[2-2]");
    }

    @Test
    public void testSetDiEdge_DirectionRelevant() throws Exception {
        Node node1 = Node.of(1, Lists.newArrayList(1));
        Node node2 = Node.of(2, Lists.newArrayList(2));
        Node node3 = Node.of(3, Lists.newArrayList(3));

        SetDiEdge uut = new SetDiEdge(Sets.newHashSet(node1), Sets.newHashSet(node2));
        SetDiEdge equalSetDiEdge = new SetDiEdge(Sets.newHashSet(node2), Sets.newHashSet(node1));
        SetDiEdge differentSetDiEdge = new SetDiEdge(Sets.newHashSet(node1), Sets.newHashSet(
                node3));

        assertThat(uut).isNotEqualTo(equalSetDiEdge);
        assertThat(uut).isNotEqualTo(differentSetDiEdge);
        assertThat(equalSetDiEdge).isNotEqualTo(differentSetDiEdge);
    }
}
