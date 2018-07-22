package de.uni.leipzig.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.google.common.collect.Lists;

public class EquivalenceClassTest {
    @Test
    public void testAdd() throws Exception {
        Node node1 = Node.of(1, Lists.newArrayList(1));
        Node node2 = Node.of(2, Lists.newArrayList(2));
        Node node3 = Node.of(3, Lists.newArrayList(3));

        EquivalenceClass ÄK = new EquivalenceClass(node1);
        ÄK.add(node2);
        ÄK.add(node3);

        assertThat(ÄK.contains(node1)).isTrue();
        assertThat(ÄK.contains(node2)).isTrue();
        assertThat(ÄK.contains(node3)).isTrue();
    }
}
