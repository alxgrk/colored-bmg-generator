package de.uni.leipzig;

import java.util.List;

import org.assertj.core.api.Condition;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import de.uni.leipzig.model.Node;

public class RandomTreeTest {

    Condition<List<Node>> childNodes = new Condition<>(n -> n.size() == 1,
            "childNode");

    Condition<List<Node>> rootNodes = new Condition<>(n -> n.size() == 3,
            "rootNode");

    @Test
    public void testRandomTree() throws Exception {
        int maxKinder = 2;
        int maxTiefe = 3;
        int maxLabel = 2;

        RandomTree uut = new RandomTree(maxKinder, maxTiefe, maxLabel);
        List<List<Node>> adjList = uut.create();

        assertThat(adjList).areAtLeast(1, childNodes);
        assertThat(adjList).areAtMost(4, childNodes);
        assertThat(adjList).areAtLeast(1, rootNodes);
        assertThat(adjList).areAtMost(3, rootNodes);

        assertThat(adjList.size()).isLessThanOrEqualTo(7);
        assertThat(adjList).allMatch(n -> n.size() <= 3);

        assertThat(adjList).allSatisfy(n -> {
            assertThat(n).allMatch(c -> c.getLabel().equals("0") || c.getLabel().equals("1"));
        });
    }
}
