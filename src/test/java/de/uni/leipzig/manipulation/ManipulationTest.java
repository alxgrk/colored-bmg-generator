package de.uni.leipzig.manipulation;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.zalando.fauxpas.ThrowingRunnable;

import de.uni.leipzig.model.Tree;
import de.uni.leipzig.uncolored.AhoBuild;
import de.uni.leipzig.user.UserInput;

@RunWith(MockitoJUnitRunner.class)
public class ManipulationTest {

    @Captor
    private ArgumentCaptor<ThrowingRunnable<Exception>> captor;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testAskForManipulation() throws Exception {
        Set tripleSet = mock(Set.class);
        Set nodeSet = mock(Set.class);
        UserInput mockInput = mock(UserInput.class);
        AhoBuild ahoBuild = mock(AhoBuild.class);

        new Manipulation(tripleSet, nodeSet, mockInput, ahoBuild);

        verify(mockInput).register(eq("no"), any());
        verify(mockInput).register(eq("yes"), any());
        verify(mockInput).askWithOptions(anyString());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testApply() throws Exception {
        Set tripleSet = mock(Set.class);
        Set nodeSet = mock(Set.class);
        Tree tree = mock(Tree.class);
        AhoBuild ahoBuild = mock(AhoBuild.class);
        UserInput mockInput = mock(UserInput.class);
        when(ahoBuild.build(tripleSet, nodeSet)).thenReturn(tree);
        when(mockInput.listenForResult()).thenReturn("50");

        Manipulation uut = new Manipulation(tripleSet, nodeSet, mockInput, ahoBuild);
        uut.apply();

        verify(mockInput).register(eq("deletion"), captor.capture());
        verify(mockInput).register(eq("insertion"), captor.capture());
        verify(mockInput).register(eq("invert triples"), captor.capture());
        verify(mockInput, times(2)).askWithOptions(anyString());

        captor.getAllValues().forEach(r -> r.run());
        verify(tree, times(3)).print();
    }

}
