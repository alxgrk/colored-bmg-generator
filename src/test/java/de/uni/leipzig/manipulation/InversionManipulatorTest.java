package de.uni.leipzig.manipulation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uni.leipzig.model.DefaultTriple;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.model.edges.Edge;

public class InversionManipulatorTest {

    private Node one = Node.of(0, Lists.newArrayList(0, 1));

    private Node two = Node.of(0, Lists.newArrayList(0, 2));

    private Node three = Node.of(0, Lists.newArrayList(0, 3));

    private Node four = Node.of(0, Lists.newArrayList(0, 4));

    private Set<Node> nodeSet = Sets.newHashSet(one, two, three, four);

    @Test
    public void testConstructor() throws Exception {
        try {
            new InversionManipulator(50);
            assertThatThrownBy(() -> new InversionManipulator(-1))
                    .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> new InversionManipulator(100))
                    .isInstanceOf(IllegalArgumentException.class);
        } catch (Throwable t) {
            fail();
        }
    }

    @Test
    public void testManipulate() throws Exception {
        Triple t1 = new DefaultTriple(new Edge(one, two), four);
        Triple t2 = new DefaultTriple(new Edge(one, three), four);
        TreeSet<Triple> tripleSet = Sets.newTreeSet(Lists.newArrayList(t1, t2));

        InversionManipulator uut = new InversionManipulator(50);
        uut.manipulate(tripleSet, nodeSet);

        Triple invertedFirst = new DefaultTriple(new Edge(two, one), four);
        assertThat(tripleSet).hasSize(2);
        assertThat(tripleSet)
                .first()
                .isEqualTo(invertedFirst);
    }

    @Test
    public void testManipulate_ZeroPercent() throws Exception {
        Triple t1 = new DefaultTriple(new Edge(one, two), four);
        Triple t2 = new DefaultTriple(new Edge(one, three), four);
        Set<Triple> tripleSet = spy(Sets.newHashSet(t1, t2));

        InversionManipulator uut = new InversionManipulator(0);
        uut.manipulate(tripleSet, nodeSet);

        verifyNoMoreInteractions(tripleSet);
    }

}
