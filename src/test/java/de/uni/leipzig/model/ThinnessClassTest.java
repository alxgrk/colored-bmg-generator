package de.uni.leipzig.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;

import org.junit.Test;

import com.google.common.collect.Lists;

public class ThinnessClassTest {
    @Test
    public void testAdd() throws Exception {
        Node node1 = Node.of(1, Lists.newArrayList(1));
        Node node2 = Node.of(2, Lists.newArrayList(2));
        Node node3 = Node.of(3, Lists.newArrayList(3));

        ThinnessClass tc = new ThinnessClass(new HashSet<>(), new HashSet<>(), node1);
        tc.add(node2);
        tc.add(node3);

        assertThat(tc.contains(node1)).isTrue();
        assertThat(tc.contains(node2)).isTrue();
        assertThat(tc.contains(node3)).isTrue();
    }
}
