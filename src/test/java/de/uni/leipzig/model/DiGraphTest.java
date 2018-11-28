package de.uni.leipzig.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.assertj.core.util.Lists;
import org.junit.*;

import com.google.common.collect.Sets;

import de.uni.leipzig.model.edges.DiEdge;

public class DiGraphTest {

    // @formatter:off
    // Nodes: [1-01, 0-0212, 1-0221, 0-0211, 0-0222]
    Node n01 = Node.of(1, Lists.newArrayList(0, 1));
    Node n0212 = Node.of(0, Lists.newArrayList(0, 2, 1, 2));
    Node n0221 = Node.of(1, Lists.newArrayList(0, 2, 2, 1));
    Node n0211 = Node.of(0, Lists.newArrayList(0, 2, 1, 1));
    Node n0222 = Node.of(0, Lists.newArrayList(0, 2, 2, 2));
    Set<Node> nodes = Sets.newHashSet(n01, n0211, n0212, n0221, n0222);

    // Edges: [0-0211->1-0221, 0-0222->1-0221, 1-0221->0-0222, 0-0212->1-0221]
    DiEdge e1 = new DiEdge(n0211, n0221);
    DiEdge e2 = new DiEdge(n0222, n0221);
    DiEdge e3 = new DiEdge(n0221, n0222);
    DiEdge e4 = new DiEdge(n0212, n0221);
    Set<DiEdge> edges = Sets.newHashSet(e1, e2, e3, e4);
    
    // EC(nodes=[1-0221])=N(n1=[0-0222], n2=[1-0221], n3=[0-0222],nIn=[0-0212, 0-0211, 0-0222]),
    // EC(nodes=[1-01])=N(n1=[], n2=[], n3=[], nIn=[]),
    // EC(nodes=[0-0212, 0-0211])=N(n1=[1-0221], n2=[0-0222],n3=[1-0221], nIn=[]),
    // EC(nodes=[0-0222])=N(n1=[1-0221], n2=[0-0222], n3=[1-0221],nIn=[1-0221])
    ThinnessClass alpha = new ThinnessClass(Sets.newHashSet(n0222), Sets.newHashSet(n0211, n0222, n0212), n0221);
    ThinnessClass beta = new ThinnessClass(Sets.newHashSet(), Sets.newHashSet(), n01);
    ThinnessClass gamma = new ThinnessClass(Sets.newHashSet(n0221), Sets.newHashSet(), n0212, n0211);
    ThinnessClass delta = new ThinnessClass(Sets.newHashSet(n0221), Sets.newHashSet(n0221), n0222);
    
    // @formatter:on

    @Test
    public void testThinnessClasses() throws Exception {

        DiGraph uut = new DiGraph(nodes, edges);

        assertThat(uut.getThinnessClasses())
                .containsExactlyInAnyOrder(alpha, beta, gamma, delta);
    }

    @Test
    public void testNeighbours() throws Exception {

        DiGraph uut = new DiGraph(nodes, edges);

        assertThat(uut.getNeighboursByTc().keySet())
                .containsExactlyInAnyOrder(alpha, beta, gamma, delta);
        assertThat(uut.getNeighboursByTc().values())
                .doesNotContainNull();
        assertThat(uut.getN1(gamma)).containsExactly(n0221);
        assertThat(uut.getN2(gamma)).containsExactly(n0222);
        assertThat(uut.getN3(gamma)).containsExactly(n0221);
    }

    @Test
    public void testReachables() throws Exception {

        DiGraph uut = new DiGraph(nodes, edges);

        assertThat(uut.getReachablesByTc().keySet())
                .containsExactlyInAnyOrder(alpha, beta, gamma, delta);
        assertThat(uut.getReachablesByTc().values())
                .doesNotContainNull();
    }

    @Test
    public void testHierarchy() throws Exception {

        DiGraph uut = new DiGraph(nodes, edges);

        assertThat(uut.getHasseDiagram().toNewickNotation())
                .isEqualTo("(0-0211,0-0212,(0-0222,1-0221),1-01)");
    }

    @Test
    public void testNodes() throws Exception {
        Node node1 = Node.of(1, Lists.newArrayList(1));
        Node node2 = Node.of(2, Lists.newArrayList(2));

        DiGraph uut = new DiGraph(Sets.newHashSet(node1, node2),
                Sets.newHashSet());

        assertThat(uut.getNodes()).containsExactlyInAnyOrder(node1, node2);
        assertThat(uut.getEdges()).isEmpty();
    }

    @Test
    public void testEdges() throws Exception {
        Node node1 = Node.of(1, Lists.newArrayList(1));
        Node node2 = Node.of(2, Lists.newArrayList(2));
        DiEdge edge1 = new DiEdge(node1, node2);
        DiEdge edge2 = new DiEdge(node2, node1);

        DiGraph uut = new DiGraph(Sets.newHashSet(node1, node2),
                Sets.newHashSet(edge1, edge2));

        assertThat(uut.getNodes()).containsExactlyInAnyOrder(node1, node2);
        assertThat(uut.getEdges()).containsExactlyInAnyOrder(edge1, edge2);
    }

    // FAILING HIERARCHY
    @Test
    @Ignore
    public void testFailingHierarchy() throws Exception {
        // INIT
        Node one = Node.of("L", Lists.newArrayList(6, 2, 7));
        Node two = Node.of("E", Lists.newArrayList(3, 6, 8));
        Node three = Node.of("L", Lists.newArrayList(6, 3, 5));
        Node four = Node.of("L", Lists.newArrayList(6, 3, 1));
        Node five = Node.of("E", Lists.newArrayList(3, 1, 7));
        Node six = Node.of("L", Lists.newArrayList(3, 2, 3));

        DiEdge e1 = new DiEdge(two, one);
        DiEdge e2 = new DiEdge(two, four);
        DiEdge e3 = new DiEdge(one, two);
        DiEdge e4 = new DiEdge(four, one);
        DiEdge e5 = new DiEdge(five, three);
        DiEdge e6 = new DiEdge(five, four);
        DiEdge e7 = new DiEdge(two, six);
        DiEdge e8 = new DiEdge(three, five);
        DiEdge e9 = new DiEdge(four, five);
        DiEdge e10 = new DiEdge(six, two);

        // RUN
        DiGraph uut = new DiGraph(Sets.newHashSet(one, two, three, four, five, six),
                Sets.newHashSet(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10));
        Tree actual = uut.getHasseDiagram();

        // ASSERT
        assertThat(actual.toNewickNotation()).isEqualTo(
                "((E-317,(E-368,L-323,L-627,L-631)),L-635)");

    }

}
