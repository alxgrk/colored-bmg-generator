package de.uni.leipzig.model;

import org.junit.Test;

import com.google.common.collect.Lists;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NodeTest {

    @Test
    public void testHelpNode() throws Exception {

        Node uut = Node.helpNode();

        assertThat(uut.getIds()).isEmpty();
        assertThat(uut.getLabel()).isEqualTo("*");
        assertThat(uut.getPath()).isBlank();
        assertThat(uut.isHelpNode()).isTrue();
        assertThat(uut.toString()).isEqualTo("*");
    }

    @Test
    public void testNode() throws Exception {

        Node uut = Node.of(1, Lists.newArrayList(0, 1, 2));

        assertThat(uut.getIds()).containsSequence(0, 1, 2);
        assertThat(uut.getLabel()).isEqualTo("1");
        assertThat(uut.getPath()).isEqualTo("012");
        assertThat(uut.isHelpNode()).isFalse();
        assertThat(uut.toString()).isEqualTo("1-012");
    }

    @Test
    public void testNodeWithNegativeLabel() throws Exception {

        assertThatThrownBy(() -> Node.of(-1, Lists.newArrayList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
