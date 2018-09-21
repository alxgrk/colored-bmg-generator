package de.uni.leipzig.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.assertj.core.util.Lists;
import org.junit.Test;

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
    EquivalenceClass alpha = new EquivalenceClass(n0221);
    EquivalenceClass beta = new EquivalenceClass(n01);
    EquivalenceClass gamma = new EquivalenceClass(n0212, n0211);
    EquivalenceClass delta = new EquivalenceClass(n0222);
    
    // @formatter:on

    @Test
    public void testEquivalenceClasses() throws Exception {

        DiGraph uut = new DiGraph(nodes, edges);

        assertThat(uut.getEquivalenceClasses())
                .containsExactlyInAnyOrder(alpha, beta, gamma, delta);
    }

    @Test
    public void testNeighbours() throws Exception {

        DiGraph uut = new DiGraph(nodes, edges);

        assertThat(uut.getNeighboursByEc().keySet())
                .containsExactlyInAnyOrder(alpha, beta, gamma, delta);
        assertThat(uut.getNeighboursByEc().values())
                .doesNotContainNull();
        assertThat(uut.getN1(gamma)).containsExactly(n0221);
        assertThat(uut.getN2(gamma)).containsExactly(n0222);
        assertThat(uut.getN3(gamma)).containsExactly(n0221);
    }

    @Test
    public void testReachables() throws Exception {

        DiGraph uut = new DiGraph(nodes, edges);

        assertThat(uut.getReachablesByEc().keySet())
                .containsExactlyInAnyOrder(alpha, beta, gamma, delta);
        assertThat(uut.getReachablesByEc().values())
                .doesNotContainNull();
    }

    @Test
    public void testHierarchy() throws Exception {

        DiGraph uut = new DiGraph(nodes, edges);

        assertThat(uut.getHasseDiagram().toNewickNotation())
                .isEqualTo("(0-0211,0-0212,1-01,(1-0221,0-0222))");
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

}
