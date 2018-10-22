package de.uni.leipzig.model;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import static org.assertj.core.api.Assertions.assertThat;

public class TreeTest {

    // --- LEAVES ---

    @Test
    public void testToStringReturnsNewick() throws Exception {
        Node node = Node.of(1, Lists.newArrayList(2));
        Tree tree = new Tree(node);
        List<Tree> leaves = Lists.newArrayList(tree);

        Tree uut = new Tree(leaves);

        assertThat(uut.toString()).isEqualTo(uut.toNewickNotation());
    }

    @Test
    public void testOnlyLeaves() throws Exception {
        Node node1 = Node.of(0, Lists.newArrayList(1));
        Tree t1 = new Tree(node1);
        Node node2 = Node.of(1, Lists.newArrayList(2));
        Tree t2 = new Tree(node2);
        List<Tree> leaves = Lists.newArrayList(t1, t2);

        Tree uut = new Tree(leaves);

        assertThat(uut.getLeafs()).containsExactly(node1, node2);
        assertThat(uut.getSubTrees()).containsExactly(t1, t2);
        assertThat(uut.getAllNodes()).contains(node1, node2);
        assertThat(uut.toNewickNotation()).isEqualTo("(0-1,1-2)");
    }

    @Test
    public void testOnlyOneLeave() throws Exception {
        Node node1 = Node.of(0, Lists.newArrayList(1));

        Tree uut = new Tree(node1);

        assertThat(uut.getLeafs()).containsExactly(node1);
        assertThat(uut.getSubTrees()).isEmpty();
        assertThat(uut.getAllNodes()).containsExactly(node1);
        assertThat(uut.toNewickNotation()).isEqualTo("0-1");
    }

    // --- TREES ---

    @Test
    public void testOnlyTrees() throws Exception {
        Node node1 = Node.of(0, Lists.newArrayList(1));
        Tree leave1Tree = new Tree(node1);
        Node node2 = Node.of(1, Lists.newArrayList(2));
        Tree leave2Tree = new Tree(node2);

        Tree uut = new Tree(leave1Tree, leave2Tree);

        assertThat(uut.getLeafs()).containsExactly(node1, node2);
        assertThat(uut.getSubTrees()).containsExactly(leave1Tree, leave2Tree);
        assertThat(uut.getAllNodes()).contains(node1, node2);
        assertThat(uut.toNewickNotation()).isEqualTo("(0-1,1-2)");
    }

    @Test
    public void testAddSubTree() throws Exception {
        Node node1 = Node.of(0, Lists.newArrayList(1));
        Tree leave1Tree = new Tree(node1);

        Tree uut = new Tree(leave1Tree);

        assertThat(uut.getSubTrees()).containsExactly(leave1Tree);

        Node node2 = Node.of(1, Lists.newArrayList(2));
        Tree leave2Tree = new Tree(node2);

        uut.addSubTree(leave2Tree);

        assertThat(uut.getSubTrees()).containsExactly(leave1Tree, leave2Tree);
    }

    @Test
    public void testPrintTree() throws Exception {
        Node node1 = Node.of(0, Lists.newArrayList(1));
        Tree leave1Tree = new Tree(node1);
        Node node2 = Node.of(1, Lists.newArrayList(2));
        Tree leave2Tree = new Tree(node2);
        Node node3 = Node.of(0, Lists.newArrayList(3));
        Tree leave3Tree = new Tree(node3);
        Tree leave23Tree = new Tree(leave2Tree, leave3Tree);

        Tree uut = new Tree(leave1Tree, leave23Tree);

        assertThat(uut.print()).isEqualTo(
                "└── *\n" +
                        "    ├── 0-1\n" +
                        "    └── *\n" +
                        "        ├── 1-2\n" +
                        "        └── 0-3\n");
    }

}
