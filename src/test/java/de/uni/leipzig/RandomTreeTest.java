package de.uni.leipzig;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import de.uni.leipzig.model.*;
import de.uni.leipzig.user.UserInput;

@RunWith(MockitoJUnitRunner.class)
public class RandomTreeTest {

    Condition<List<Node>> childNodes = new Condition<>(n -> n.size() == 1,
            "childNode");

    Condition<List<Node>> rootNodes = new Condition<>(n -> n.size() == 3,
            "rootNode");

    @Test
    public void testAskRandomTreeConfig_MaxDepth() throws Exception {
        UserInput mockInput = mock(UserInput.class);
        when(mockInput.listenForResult()).thenReturn("2", "2", "-1", "4");
        when(mockInput.askForTrigger(anyString(), eq("y"))).thenReturn(true);

        RandomTree actual = RandomTree.askRandomTreeConfig(mockInput);

        assertThat(actual.maxChildren()).isEqualTo(2);
        assertThat(actual.maxLabel()).isEqualTo(2);
        assertThat(actual.numberOfLeaves()).isNotPresent();
        assertThat(actual.maxDepth().get()).isEqualTo(4);
        assertThat(actual.maximalNodesWithChildren()).isEqualTo(true);
    }

    @Test
    public void testAskRandomTreeConfig_NumberOfLeaves() throws Exception {
        UserInput mockInput = mock(UserInput.class);
        when(mockInput.listenForResult()).thenReturn("2", "2", "4");
        when(mockInput.askForTrigger(anyString(), eq("y"))).thenReturn(true);

        RandomTree actual = RandomTree.askRandomTreeConfig(mockInput);

        assertThat(actual.maxChildren()).isEqualTo(2);
        assertThat(actual.maxLabel()).isEqualTo(2);
        assertThat(actual.numberOfLeaves().get()).isEqualTo(4);
        assertThat(actual.maxDepth()).isNotPresent();
        assertThat(actual.maximalNodesWithChildren()).isEqualTo(true);
    }

    @Test
    public void testRandomTree_MaxDepth() throws Exception {
        int maxKinder = 2;
        int maxLabel = 2;
        int maxDepth = 3;

        RandomTree uut = new RandomTree(maxKinder, maxLabel);
        uut.maxDepth(Optional.of(maxDepth));
        AdjacencyList adjList = uut.create();

        assertThat(adjList).areAtLeast(1, childNodes);
        assertThat(adjList).areAtMost(4, childNodes);
        assertThat(adjList).areAtLeast(1, rootNodes);
        assertThat(adjList).areAtMost(3, rootNodes);

        assertThat(adjList.size()).isLessThanOrEqualTo(7);
        assertThat(adjList).allMatch(n -> n.size() <= 3);

        assertThat(adjList).allSatisfy(n -> {
            assertThat(n).allMatch(c -> c.getColor().toString().equals("0")
                    || c.getColor().toString().equals("1"));
        });
    }

    @Test
    public void testRandomTree_NumberOfLeaves() throws Exception {
        int maxKinder = 2;
        int maxLabel = 2;
        int numberOfLeaves = 8;

        RandomTree uut = new RandomTree(maxKinder, maxLabel);
        uut.numberOfLeaves(Optional.of(numberOfLeaves));
        AdjacencyList adjList = uut.create();

        assertThat(adjList).areExactly(numberOfLeaves, childNodes);

        assertThat(adjList).allSatisfy(n -> {
            assertThat(n).allMatch(c -> c.getColor().toString().equals("0")
                    || c.getColor().toString().equals("1"));
        });
    }
}
