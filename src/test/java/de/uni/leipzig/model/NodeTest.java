package de.uni.leipzig.model;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import com.google.common.collect.Lists;

public class NodeTest {

    @Test
    public void testHelpNode() throws Exception {

        Node uut = Node.helpNode();

        assertThat(uut.getIds()).isEmpty();
        assertThat(uut.getColor()).isEqualTo(new Color("*"));
        assertThat(uut.getPath()).isBlank();
        assertThat(uut.isHelpNode()).isTrue();
        assertThat(uut.toString()).isEqualTo("*");
    }

    @Test
    public void testNode() throws Exception {

        Node uut = Node.of(1, Lists.newArrayList(0, 1, 2));

        assertThat(uut.getIds()).containsSequence(0, 1, 2);
        assertThat(uut.getColor()).isEqualTo(new Color("1"));
        assertThat(uut.getPath()).isEqualTo("012");
        assertThat(uut.isHelpNode()).isFalse();
        assertThat(uut.toString()).isEqualTo("1-012");
    }

    @Test
    public void testNodeWithNegativeLabel() throws Exception {

        assertThatThrownBy(() -> Node.of("", Lists.newArrayList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
