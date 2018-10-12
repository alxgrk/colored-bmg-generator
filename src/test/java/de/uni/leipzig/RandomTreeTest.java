package de.uni.leipzig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import de.uni.leipzig.model.AdjacencyList;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.user.UserInput;

@RunWith(MockitoJUnitRunner.class)
public class RandomTreeTest {

    Condition<List<Node>> childNodes = new Condition<>(n -> n.size() == 1,
            "childNode");

    Condition<List<Node>> rootNodes = new Condition<>(n -> n.size() == 3,
            "rootNode");

    @Test
    public void testAskRandomTreeConfig() throws Exception {
        UserInput mockInput = mock(UserInput.class);
        when(mockInput.listenForResult()).thenReturn("2", "5", "2");
        when(mockInput.askForTrigger(anyString(), eq("y"))).thenReturn(true);

        RandomTree actual = RandomTree.askRandomTreeConfig(mockInput);

        assertThat(actual.maxChildren()).isEqualTo(2);
        assertThat(actual.maxDepth()).isEqualTo(5);
        assertThat(actual.maxLabel()).isEqualTo(2);
        assertThat(actual.maximalNodesWithChildren()).isEqualTo(true);
        assertThat(actual.maximalDepth()).isEqualTo(true);
    }

    @Test
    public void testRandomTree() throws Exception {
        int maxKinder = 2;
        int maxTiefe = 3;
        int maxLabel = 2;

        RandomTree uut = new RandomTree(maxKinder, maxTiefe, maxLabel);
        AdjacencyList adjList = uut.create();

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
