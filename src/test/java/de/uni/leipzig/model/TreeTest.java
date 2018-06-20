package de.uni.leipzig.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class TreeTest {

    // --- LEAVES ---

    @Test
    public void testToStringReturnsNewick() throws Exception {
        Node node = Node.of(1, Lists.newArrayList(2));
        List<Node> leaves = Lists.newArrayList(node);

        Tree uut = new Tree(leaves);

        assertThat(uut.toString()).isEqualTo(uut.toNewickNotation());
    }

    @Test
    public void testOnlyLeaves() throws Exception {
        Node node1 = Node.of(0, Lists.newArrayList(1));
        Node node2 = Node.of(1, Lists.newArrayList(2));
        List<Node> leaves = Lists.newArrayList(node1, node2);

        Tree uut = new Tree(leaves);

        assertThat(uut.getLeafs()).isEqualTo(leaves);
        assertThat(uut.getSubTrees()).isEmpty();
        assertThat(uut.getNodes()).containsExactly(node1, node2);
        assertThat(uut.toNewickNotation()).isEqualTo("(0-1,1-2)");
    }

    @Test
    public void testOnlyOneLeave() throws Exception {
        Node node1 = Node.of(0, Lists.newArrayList(1));
        List<Node> leaves = Lists.newArrayList(node1);

        Tree uut = new Tree(leaves);

        assertThat(uut.getLeafs()).isEqualTo(leaves);
        assertThat(uut.getSubTrees()).isEmpty();
        assertThat(uut.getNodes()).containsExactly(node1);
        assertThat(uut.toNewickNotation()).isEqualTo("0-1");
    }

    @Test
    public void testAtLeastOneNode() throws Exception {
        List<Node> leaves = Lists.newArrayList();

        assertThatThrownBy(() -> new Tree(leaves))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // --- TREES ---

    @Test
    public void testOnlyTrees() throws Exception {
        Node node1 = Node.of(0, Lists.newArrayList(1));
        List<Node> leave1 = Lists.newArrayList(node1);
        Tree leave1Tree = new Tree(leave1);
        Node node2 = Node.of(1, Lists.newArrayList(2));
        List<Node> leave2 = Lists.newArrayList(node2);
        Tree leave2Tree = new Tree(leave2);

        Tree uut = new Tree(leave1Tree, leave2Tree);

        assertThat(uut.getLeafs()).isEmpty();
        assertThat(uut.getSubTrees()).containsExactly(leave1Tree, leave2Tree);
        assertThat(uut.getNodes()).containsExactly(node1, node2);
        assertThat(uut.toNewickNotation()).isEqualTo("(0-1,1-2)");
    }

    @Test
    public void testAtLeastOneTrees() throws Exception {
        Tree[] leaves = new Tree[0];

        assertThatThrownBy(() -> new Tree(leaves))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testAddSubTree() throws Exception {
        Node node1 = Node.of(0, Lists.newArrayList(1));
        List<Node> leave1 = Lists.newArrayList(node1);
        Tree leave1Tree = new Tree(leave1);

        Tree uut = new Tree(leave1Tree);

        assertThat(uut.getSubTrees()).containsExactly(leave1Tree);

        Node node2 = Node.of(1, Lists.newArrayList(2));
        List<Node> leave2 = Lists.newArrayList(node2);
        Tree leave2Tree = new Tree(leave2);

        uut.addSubTree(leave2Tree);

        assertThat(uut.getSubTrees()).containsExactly(leave1Tree, leave2Tree);
    }

}
