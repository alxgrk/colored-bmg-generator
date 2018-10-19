package de.uni.leipzig.uncolored;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.zalando.fauxpas.ThrowingRunnable;

import com.google.common.collect.Sets;

import de.uni.leipzig.model.*;
import de.uni.leipzig.model.edges.Edge;
import de.uni.leipzig.user.UserInput;

@RunWith(MockitoJUnitRunner.class)
public class AhoBuildTest {

    @Captor
    ArgumentCaptor<ThrowingRunnable<Exception>> captor;

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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testAskForMinCut() throws Exception {
        DiGraphFromTripleSet creator = mock(DiGraphFromTripleSet.class);
        UserInput input = mock(UserInput.class);
        ConnectedComponentsConstructor components = mock(ConnectedComponentsConstructor.class);
        Set triples = mock(Set.class);
        Set leaves = mock(Set.class);

        AhoBuild uut = new AhoBuild(creator, input, components);
        uut.askForMinCut(triples, leaves);

        assertThat(uut.isAlwaysMinCut()).isFalse();
        verify(input).register(eq("exit"), captor.capture());
        verify(input).register(eq("min cut"), captor.capture());
        verify(input).register(eq("always min cut"), captor.capture());

        assertThat(captor.getAllValues())
                .anySatisfy(r -> {
                    assertThatThrownBy(r::run)
                            .isInstanceOf(RuntimeException.class)
                            .hasMessage("no phylogenetic tree");
                })
                .anySatisfy(r -> {
                    assertThatCode(r::run).doesNotThrowAnyException();

                    r.run();
                    verify(creator, atLeastOnce()).create(triples, leaves);
                })
                .anySatisfy(o -> {
                    Runnable r = (Runnable) o;
                    assertThatCode(r::run).doesNotThrowAnyException();

                    r.run();
                    assertThat(uut.isAlwaysMinCut()).isTrue();
                    verify(creator, atLeastOnce()).create(triples, leaves);
                });

        // assert, that the creator was run once more, since 'alwaysMinCut' should be true now
        uut.askForMinCut(triples, leaves);
        verify(creator, atLeast(3)).create(triples, leaves);
    }

}
