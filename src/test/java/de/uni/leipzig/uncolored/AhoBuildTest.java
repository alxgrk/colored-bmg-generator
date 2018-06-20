package de.uni.leipzig.uncolored;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.Lists;
import org.junit.Test;

import com.google.common.collect.Sets;

import de.uni.leipzig.model.Edge;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Tree;
import de.uni.leipzig.model.Triple;

public class AhoBuildTest {

    @Test
    public void testAhoBuild() throws Exception {
        // *
        // / \
        // * \
        // / \ \
        // * \ \
        // / \ \ \
        // * * * *
        // 1 2 3 4

        Node one = Node.of(1, Lists.newArrayList(0, 1, 1, 1));
        Node two = Node.of(0, Lists.newArrayList(0, 1, 1, 2));
        Node three = Node.of(1, Lists.newArrayList(0, 1, 2));
        Node four = Node.of(0, Lists.newArrayList(0, 2));

        Triple tripleOne = new Triple(new Edge(one, two), three);
        Triple tripleTwo = new Triple(new Edge(one, two), four);
        Triple tripleThree = new Triple(new Edge(one, three), four);
        Triple tripleFour = new Triple(new Edge(two, three), four);

        Tree result = AhoBuild.build(Sets.newHashSet(tripleOne, tripleTwo, tripleThree, tripleFour),
                Lists.newArrayList(one, two, three, four));

        assertThat(result.getLeafs()).containsExactly(one, two, three, four);
        assertThat(result.getNodes()).hasSize(7);
        assertThat(result.toNewickNotation()).isEqualTo("(((1,2),3),4)");
    }
}
