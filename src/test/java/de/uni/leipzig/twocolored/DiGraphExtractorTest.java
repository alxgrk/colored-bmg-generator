package de.uni.leipzig.twocolored;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.Test;

import com.google.common.collect.Lists;

import de.uni.leipzig.model.AdjacencyList;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.edges.DiEdge;

public class DiGraphExtractorTest {

    @Test
    public void testExtract() throws Exception {
        // INIT
        Node root = Node.of(0, Lists.newArrayList(0));
        Node node1 = Node.of(0, Lists.newArrayList(0, 1));
        Node node2 = Node.of(0, Lists.newArrayList(0, 2));
        Node node3 = Node.of(0, Lists.newArrayList(0, 1, 1));
        Node node4 = Node.of(1, Lists.newArrayList(0, 1, 2));
        Node node5 = Node.of(0, Lists.newArrayList(0, 1, 1, 1));
        Node node6 = Node.of(0, Lists.newArrayList(0, 1, 1, 2));

        ArrayList<Node> liste1 = Lists.newArrayList(root, node1, node2);
        ArrayList<Node> liste2 = Lists.newArrayList(node1, node3, node4);
        ArrayList<Node> liste3 = Lists.newArrayList(node3, node5, node6);
        ArrayList<Node> liste4 = Lists.newArrayList(node5);
        ArrayList<Node> liste5 = Lists.newArrayList(node6);
        ArrayList<Node> liste6 = Lists.newArrayList(node4);
        ArrayList<Node> liste7 = Lists.newArrayList(node2);

        AdjacencyList input = new AdjacencyList(
                Lists.newArrayList(liste1, liste2, liste3, liste4, liste5, liste6, liste7));

        // RUN
        DiGraphExtractor uut = new DiGraphExtractor();

        DiGraph result = uut.extract(input);

        // ASSERT
        assertThat(result.getNodes()).containsExactlyInAnyOrder(node5, node6, node4, node2);
        assertThat(result.getEdges()).containsExactlyInAnyOrder(new DiEdge(node4, node5),
                new DiEdge(node4, node6));

    }

}
