package de.uni.leipzig.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class HierarchyTest {

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

    // Rqs:
    Set<Node> rq12 = Sets.newHashSet(one, two);
    Set<Node> rq1234 = Sets.newHashSet(one, two, three, four);
    Set<Node> rq56 = Sets.newHashSet(five, six);
    Set<Node> rq5678 = Sets.newHashSet(five, six, seven, eight);
    Set<Node> rq1_10 = Sets.newHashSet(one, two, three, four, five,
            six, seven, eight, nine, ten);
    
    Map<EquivalenceClass, Reachables> rqs;
    
    // @formatter:on

    @Before
    public void setUp() throws Exception {

        // Äks:
        EquivalenceClass a = new EquivalenceClass(one);
        EquivalenceClass b = new EquivalenceClass(two);
        EquivalenceClass c = new EquivalenceClass(three);
        EquivalenceClass d = new EquivalenceClass(four);
        EquivalenceClass e = new EquivalenceClass(five);
        EquivalenceClass f = new EquivalenceClass(six);
        EquivalenceClass g = new EquivalenceClass(seven, eight);
        EquivalenceClass h = new EquivalenceClass(nine, ten);

        Reachables r12 = new Reachables(null, null, rq12);
        Reachables r1234 = new Reachables(null, null, rq1234);
        Reachables r56 = new Reachables(null, null, rq56);
        Reachables r5678 = new Reachables(null, null, rq5678);
        Reachables r1_10 = new Reachables(null, null, rq1_10);

        rqs = new HashMap<>();
        rqs.put(a, r12);
        rqs.put(b, r12);
        rqs.put(c, r1234);
        rqs.put(d, r1234);
        rqs.put(e, r56);
        rqs.put(f, r56);
        rqs.put(g, r5678);
        rqs.put(h, r1_10);

    }

    @Test
    public void testHierarchy() throws Exception {

        // RUN
        Hierarchy uut = new Hierarchy(rqs);

        // ASSERT
        assertThat(uut.getSets()).hasSize(5);
        assertThat(uut.getSets()).contains(rq12, rq1234, rq1_10, rq56, rq5678);
    }

    @Test
    public void testToHasseTree() throws Exception {
        // INIT
        Tree tree12 = new Tree(rq12);
        Tree tree56 = new Tree(rq56);

        Tree tree1234 = new Tree(rq1234);
        tree1234.addSubTree(tree12);
        Tree tree5678 = new Tree(rq5678);
        tree5678.addSubTree(tree56);

        Tree root = new Tree(rq1_10);
        root.addSubTree(tree1234);
        root.addSubTree(tree5678);

        // RUN
        Hierarchy uut = new Hierarchy(rqs);
        Tree actual = uut.toHasseTree();

        // ASSERT
        // expected:
        // (0-03,0-04,(1-012,0-013,(1-0111,0-0112)),(1-022,1-023,(0-0211,0-0212)))
        assertThat(actual.toNewickNotation()).isEqualTo(root.toNewickNotation());

    }

}
