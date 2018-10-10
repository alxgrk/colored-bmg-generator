package de.uni.leipzig.twocolored.axiom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.edges.DiEdge;
import de.uni.leipzig.twocolored.axiom.Axiom1;
import de.uni.leipzig.twocolored.axiom.Axiom2;
import de.uni.leipzig.twocolored.axiom.Axiom3;
import de.uni.leipzig.twocolored.axiom.Axioms;

public class AxiomsTest {

    @Test
    public void testCheck3ImmutableAxioms() throws Exception {

        List<Axioms> axioms = Axioms.getAxioms();

        assertThat(axioms).containsExactly(new Axiom1(), new Axiom2(), new Axiom3());
        assertThatThrownBy(() -> axioms.remove(0))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testCheckAll_AxiomsNotFulfilled() throws Exception {
        // Nodes: [1-0223, 1-023, 1-0211, 1-0222, 1-01, 0-0212, 1-0221, 1-0213]
        Node n0 = Node.of(1, Lists.newArrayList(0, 2, 2, 3));
        Node n1 = Node.of(1, Lists.newArrayList(0, 2, 3));
        Node n2 = Node.of(1, Lists.newArrayList(0, 2, 1, 1));
        Node n3 = Node.of(1, Lists.newArrayList(0, 2, 2, 2));
        Node n4 = Node.of(1, Lists.newArrayList(0, 1));
        Node n5 = Node.of(0, Lists.newArrayList(0, 2, 1, 2));
        Node n6 = Node.of(1, Lists.newArrayList(0, 2, 2, 1));
        Node n7 = Node.of(1, Lists.newArrayList(0, 2, 1, 3));
        Set<Node> nodes = Sets.newHashSet(n0, n1, n2, n3, n4, n5, n6, n7);

        // DiEdges: [0-0212->1-0211, 0-0212->1-0222, 0-0212->1-0223,
        // 0-0212->1-0213, 0-0212->1-023, 0-0212->1-0221]
        DiEdge e0 = new DiEdge(n5, n0);
        DiEdge e1 = new DiEdge(n5, n1);
        DiEdge e2 = new DiEdge(n5, n2);
        DiEdge e3 = new DiEdge(n5, n3);
        DiEdge e4 = new DiEdge(n5, n6);
        DiEdge e5 = new DiEdge(n5, n7);
        Set<DiEdge> edges = Sets.newHashSet(e0, e1, e2, e3, e4, e5);

        DiGraph graph = new DiGraph(nodes, edges);

        boolean actual = Axioms.checkAll(graph);

        assertThat(actual).isFalse();
    }

    @Test
    public void testCheckAll_AxiomsFulfilled() throws Exception {
        // Nodes: [1-0211, 0-0212, 0-01, 1-022]
        Node n0 = Node.of(1, Lists.newArrayList(0, 2, 1, 1));
        Node n1 = Node.of(0, Lists.newArrayList(0, 2, 1, 2));
        Node n2 = Node.of(0, Lists.newArrayList(0, 1));
        Node n3 = Node.of(1, Lists.newArrayList(0, 2, 2));
        Set<Node> nodes = Sets.newHashSet(n0, n1, n2, n3);

        // DiEdges: [0-0212->1-0211, 1-022->0-0212, 1-0211->0-0212]
        DiEdge e0 = new DiEdge(n1, n0);
        DiEdge e1 = new DiEdge(n3, n1);
        DiEdge e2 = new DiEdge(n0, n1);
        Set<DiEdge> edges = Sets.newHashSet(e0, e1, e2);

        DiGraph graph = new DiGraph(nodes, edges);

        boolean actual = Axioms.checkAll(graph);

        assertThat(actual).isTrue();
    }

}
