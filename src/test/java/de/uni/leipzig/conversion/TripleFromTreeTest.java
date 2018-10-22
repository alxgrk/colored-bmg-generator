package de.uni.leipzig.conversion;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import static org.assertj.core.api.Assertions.assertThat;

import de.uni.leipzig.Util;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Tree;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.uncolored.AhoBuild;

public class TripleFromTreeTest {

    // Example tree taken from paper
    // @formatter:off
    //          _____0_____
    //         /     | \   \
    //        /      |  \   \
    //       /       |   \   \
    //      /        |   (9) (10)
    //    /|\       /|\
    //   / | \     / | \
    // (3) | [4] (7) | (8)
    //     |         |
    //    / \       / \
    //  (1) [2]   (5) [6]

    // Nodes: [0-0111, 1-0112, 0-012, 1-013, 0-0211, 1-0212, 0-022, 0-023,
    // 0-03, 0-04]
    Node one = Node.of(1, Lists.newArrayList(0, 1, 1, 1));
    Node two = Node.of(0, Lists.newArrayList(0, 1, 1, 2));
    Node three = Node.of(1, Lists.newArrayList(0, 1, 2));
    Node four = Node.of(0, Lists.newArrayList(0, 1, 3));
    Node five = Node.of(0, Lists.newArrayList(0, 2, 1, 1));
    Node six = Node.of(0, Lists.newArrayList(0, 2, 1, 2));
    Node seven = Node.of(1, Lists.newArrayList(0, 2, 2));
    Node eight = Node.of(1, Lists.newArrayList(0, 2, 3));
    Node nine = Node.of(0, Lists.newArrayList(0, 3));
    Node ten = Node.of(0, Lists.newArrayList(0, 4));
    
    private Tree root;
    private HashSet<Node> allLeafs;
    
    // @formatter:on

    @Before
    public void setUp() throws Exception {
        Tree tree12 = Util.nodeSetToLeafTree(Sets.newHashSet(one, two));
        Tree tree56 = Util.nodeSetToLeafTree(Sets.newHashSet(five, six));

        Tree tree1234 = Util.nodeSetToLeafTree(Sets.newHashSet(one, two, three, four));
        tree1234.addSubTree(tree12);
        Tree tree5678 = Util.nodeSetToLeafTree(Sets.newHashSet(five, six, seven, eight));
        tree5678.addSubTree(tree56);

        allLeafs = Sets.newHashSet(one, two, three, four, five, six, seven,
                eight, nine, ten);
        root = Util.nodeSetToLeafTree(allLeafs);
        root.addSubTree(tree1234);
        root.addSubTree(tree5678);
    }

    @Test
    @Ignore
    public void testRoot() throws Exception {

        TripleFromTree uut = new TripleFromTree();
        Set<Triple> actual = uut.treeToTriples(root);

        // FIXME
        AhoBuild ahoBuild = new AhoBuild();
        Tree result = ahoBuild.build(actual, allLeafs);

        assertThat(actual).isNotNull();
    }

}
