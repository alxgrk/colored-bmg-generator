package de.uni.leipzig.uncolored;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.Lists;
import org.junit.Test;

import com.google.common.collect.Sets;

import de.uni.leipzig.model.DefaultTriple;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Tree;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.model.edges.Edge;

public class AhoBuildTest {

    @Test
    public void testAhoBuild() throws Exception {
        // *
        // | \
        // * \
        // | \ \
        // * \ \
        // | \ \ \
        // * * * *
        // 1 2 3 4

        Node one = Node.of(1, Lists.newArrayList(0, 1, 1, 1));
        Node two = Node.of(0, Lists.newArrayList(0, 1, 1, 2));
        Node three = Node.of(1, Lists.newArrayList(0, 1, 2));
        Node four = Node.of(0, Lists.newArrayList(0, 2));

        Triple tripleOne = new DefaultTriple(new Edge(one, two), three);
        Triple tripleTwo = new DefaultTriple(new Edge(one, two), four);
        Triple tripleThree = new DefaultTriple(new Edge(one, three), four);
        Triple tripleFour = new DefaultTriple(new Edge(two, three), four);

        AhoBuild ahoBuild = new AhoBuild();
        Tree result = ahoBuild.build(Sets.newHashSet(tripleOne, tripleTwo, tripleThree, tripleFour),
                Sets.newHashSet(one, two, three, four));

        assertThat(result.getNodes()).containsExactlyInAnyOrder(Node.helpNode(), one, two, three,
                four);
        assertThat(result.getNodes()).hasSize(5);
        String newickNotation = result.toNewickNotation();
        assertThat(newickNotation).contains(one.toString())
                .contains(two.toString())
                .contains(three.toString())
                .contains(four.toString());
    }
}
