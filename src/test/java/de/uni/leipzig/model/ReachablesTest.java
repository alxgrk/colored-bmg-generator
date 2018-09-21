package de.uni.leipzig.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.assertj.core.util.Lists;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uni.leipzig.model.edges.DiEdge;

public class ReachablesTest {

    // @formatter:off
    // Nodes: [1-01, 0-0212, 1-0221, 0-0211, 0-0222]
    Node n01 = Node.of(1, Lists.newArrayList(0, 1));
    Node n0212 = Node.of(0, Lists.newArrayList(0, 2, 1, 2));
    Node n0221 = Node.of(1, Lists.newArrayList(0, 2, 2, 1));
    Node n0211 = Node.of(0, Lists.newArrayList(0, 2, 1, 1));
    Node n0222 = Node.of(0, Lists.newArrayList(0, 2, 2, 2));

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

    Map<EquivalenceClass, Neighbourhood> allN = Maps.asMap(Sets.newHashSet(alpha, beta, gamma, delta), 
            ec -> new Neighbourhood(ec, edges));
    // @formatter:on

    @Test
    public void testCreation() throws Exception {
        Entry<EquivalenceClass, Neighbourhood> eBeta = getNeighbourhoodEntry(beta);
        Entry<EquivalenceClass, Neighbourhood> eGamma = getNeighbourhoodEntry(gamma);

        Reachables uutBeta = new Reachables(eBeta, allN);
        Reachables uutGamma = new Reachables(eGamma, allN);

        assertThat(uutBeta.getR()).isEmpty();
        assertThat(uutBeta.getQ()).containsExactly(n01);
        assertThat(uutBeta.getRq()).containsExactly(n01);

        assertThat(uutGamma.getR()).containsExactlyInAnyOrder(n0221, n0222);
        assertThat(uutGamma.getQ()).containsExactlyInAnyOrder(n01, n0211, n0212);
        assertThat(uutGamma.getRq()).containsExactlyInAnyOrder(n01, n0211, n0212, n0221, n0222);
    }

    @Test
    public void testInNeighboursAreEqual() throws Exception {
        Entry<EquivalenceClass, Neighbourhood> e1 = getNeighbourhoodEntry(alpha);
        Entry<EquivalenceClass, Neighbourhood> e2 = getNeighbourhoodEntry(beta);
        Entry<EquivalenceClass, Neighbourhood> e3 = getNeighbourhoodEntry(gamma);

        boolean alphaBeta = Reachables.inNeighboursAreEqual(e1, e2);
        boolean betaAlpha = Reachables.inNeighboursAreEqual(e2, e1);
        boolean betaGamma = Reachables.inNeighboursAreEqual(e2, e3);

        assertThat(alphaBeta).isFalse();
        assertThat(betaAlpha).isFalse();
        assertThat(betaGamma).isTrue();
    }

    @Test
    public void testNeighboursOfAlphaAreSupersetOfBeta() throws Exception {
        Entry<EquivalenceClass, Neighbourhood> e1 = getNeighbourhoodEntry(alpha);
        Entry<EquivalenceClass, Neighbourhood> e2 = getNeighbourhoodEntry(beta);
        Entry<EquivalenceClass, Neighbourhood> e3 = getNeighbourhoodEntry(gamma);
        Entry<EquivalenceClass, Neighbourhood> e4 = getNeighbourhoodEntry(delta);

        boolean alphaBeta = Reachables.neighboursOfAlphaAreSupersetOfBeta(e1, e2);
        boolean betaAlpha = Reachables.neighboursOfAlphaAreSupersetOfBeta(e2, e1);
        boolean betaGamma = Reachables.neighboursOfAlphaAreSupersetOfBeta(e2, e3);
        boolean gammaDelta = Reachables.neighboursOfAlphaAreSupersetOfBeta(e3, e4);

        assertThat(alphaBeta).isTrue();
        assertThat(betaAlpha).isFalse();
        assertThat(betaGamma).isFalse();
        assertThat(gammaDelta).isTrue();
    }

    private Entry<EquivalenceClass, Neighbourhood> getNeighbourhoodEntry(EquivalenceClass ec) {
        return allN.entrySet()
                .stream()
                .filter(e -> e.getKey().equals(ec))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

}
