package de.uni.leipzig;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uni.leipzig.informative.model.InformativeTriple;
import de.uni.leipzig.model.DefaultTriple;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.model.edges.Edge;

public class UtilTest {

    Node one = Node.of(0, Lists.newArrayList(0, 1));

    Node two = Node.of(0, Lists.newArrayList(0, 2));

    Node three = Node.of(0, Lists.newArrayList(0, 3));

    @Test
    public void testEqualSets_NotEqual() throws Exception {
        Set<Node> s1 = Sets.newHashSet(one, two);
        Set<Node> s2 = Sets.newHashSet(two, three);
        Set<Node> s3 = Sets.newHashSet(three);

        boolean is1Equal2 = Util.equalSets(s1, s2);
        boolean is2Equal1 = Util.equalSets(s2, s1);
        boolean is2Equal3 = Util.equalSets(s2, s3);

        assertThat(is1Equal2).isFalse();
        assertThat(is2Equal1).isFalse();
        assertThat(is2Equal3).isFalse();
    }

    @Test
    public void testEqualSets_Equal() throws Exception {
        Set<Node> s1 = Sets.newHashSet(one, two, three);
        Set<Node> s2 = Sets.newHashSet(one, two, three);

        boolean isEqual1 = Util.equalSets(s1, s2);
        boolean isEqual2 = Util.equalSets(s2, s1);

        assertThat(isEqual1).isTrue();
        assertThat(isEqual2).isTrue();
    }

    @Test
    public void testProperSubset() throws Exception {
        Set<Node> s1 = Sets.newHashSet(one, two);
        Set<Node> s2 = Sets.newHashSet(two);
        Set<Node> s3 = Sets.newHashSet(two);

        boolean is1SubsetOf2 = Util.properSubset(s1, s2);
        boolean is2SubsetOf1 = Util.properSubset(s2, s1);
        boolean is2SubsetOf3 = Util.properSubset(s2, s3);

        assertThat(is1SubsetOf2).isFalse();
        assertThat(is2SubsetOf1).isTrue();
        assertThat(is2SubsetOf3).isTrue();
    }

    @Test
    public void testUglyCast_DefaultTriple() throws Exception {
        DefaultTriple defaultTriple = new DefaultTriple(new Edge(one, two), three);
        Set<DefaultTriple> defaultTripleSet = Sets.newHashSet(defaultTriple);

        Set<Triple> resultSet = Util.uglyCast(defaultTripleSet);

        assertThat(resultSet).isEqualTo(defaultTripleSet);
    }

    @Test
    public void testUglyCast_InformativeTriple() throws Exception {
        InformativeTriple informativeTriple = new InformativeTriple(new Edge(one, two), three);
        Set<InformativeTriple> informativeTripleSet = Sets.newHashSet(informativeTriple);

        Set<Triple> resultSet = Util.uglyCast(informativeTripleSet);

        assertThat(resultSet).isEqualTo(informativeTripleSet);
    }

}
