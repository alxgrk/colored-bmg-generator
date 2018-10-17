package de.uni.leipzig.method;

import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;

import de.uni.leipzig.method.TreeCreation.Method;
import de.uni.leipzig.model.*;

public class TreeCreationTest {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testDefaultMethods() throws Exception {
        Set someSet = mock(Set.class);
        DiGraph graph = mock(DiGraph.class);

        TestCreation impl = new TestCreation();
        impl.create(graph);
        impl.create(someSet, graph);
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
            return new Tree(Sets.newHashSet(Node.helpNode()));
        }

    }

}
