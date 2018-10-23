package de.uni.leipzig.method;

import java.util.Set;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import de.uni.leipzig.method.TreeCreation.Method;
import de.uni.leipzig.model.AdjacencyList;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Tree;

public class TreeCreationTest {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testDefaultMethods() throws Exception {
        Set someSet = mock(Set.class);
        DiGraph graph = mock(DiGraph.class);

        TestCreation impl = new TestCreation();
        impl.create(graph);
        impl.create(someSet, someSet);

        verifyZeroInteractions(someSet);
        verifyZeroInteractions(graph);
    }

    @Test
    public void testMethods() throws Exception {

        assertThat(Method.AHO.get()).isInstanceOf(Aho.class);
        assertThat(Method.AHO_INFORMATIVE.get()).isInstanceOf(AhoInformative.class);
        assertThat(Method.THINNESS_CLASS.get()).isInstanceOf(ThinnessClass.class);

    }

    class TestCreation implements TreeCreation {

        @Override
        public Tree create(AdjacencyList adjList) {
            return new Tree(Node.helpNode());
        }

        @Override
        public TreeCreation inNonInteractiveMode(boolean mode) {
            return this;
        }

    }

}
