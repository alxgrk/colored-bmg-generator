package de.uni.leipzig.uncolored;

import java.util.Set;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;
import org.zalando.fauxpas.ThrowingRunnable;

import com.google.common.collect.Sets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.uni.leipzig.model.DefaultTriple;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Tree;
import de.uni.leipzig.model.Triple;
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

        assertThat(result.getAllNodes()).contains(one, two, three, four);
        assertThat(result.getAllNodes()).hasSize(7);
        String newickNotation = result.toNewickNotation();
        assertThat(newickNotation).isEqualTo("(((1-0111,0-0112),1-012),0-02)");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testAskForMinCut() throws Exception {
        MinCut creator = mock(MinCut.class);
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
                    verify(creator, atLeastOnce()).create(triples);
                    verify(components, atLeastOnce()).construct(any(), eq(leaves));
                })
                .anySatisfy(o -> {
                    Runnable r = (Runnable) o;
                    assertThatCode(r::run).doesNotThrowAnyException();

                    r.run();
                    assertThat(uut.isAlwaysMinCut()).isTrue();
                    verify(creator, atLeastOnce()).create(triples);
                    verify(components, atLeastOnce()).construct(any(), eq(leaves));
                });

        // assert, that the creator was run once more, since 'alwaysMinCut' should be true now
        uut.askForMinCut(triples, leaves);
        verify(creator, atLeast(3)).create(triples);
    }

}
