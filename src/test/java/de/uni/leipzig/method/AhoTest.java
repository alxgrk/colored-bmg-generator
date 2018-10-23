package de.uni.leipzig.method;

import static org.mockito.Mockito.*;

import java.util.Set;

import org.jgrapht.alg.util.Pair;
import org.junit.Test;

import de.uni.leipzig.model.*;
import de.uni.leipzig.uncolored.AhoBuild;
import de.uni.leipzig.user.UserInput;

public class AhoTest {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testCreate_AdjacencyList() throws Exception {
        AhoBuild ahoBuild = mock(AhoBuild.class);
        TripleFinder tripleFinder = mock(TripleFinder.class);
        UserInput input = mock(UserInput.class);
        AdjacencyList adjList = mock(AdjacencyList.class);
        Set triples = mock(Set.class);
        Set leaves = mock(Set.class);
        Tree tree = mock(Tree.class);
        when(tripleFinder.findTriple(adjList)).thenReturn(Pair.of(triples, leaves));
        when(ahoBuild.build(triples, leaves)).thenReturn(tree);

        Aho uut = new Aho(ahoBuild, input, tripleFinder);
        uut.create(adjList);

        verify(tripleFinder).findTriple(adjList);
        verify(ahoBuild).build(triples, leaves);
        verify(tree).print();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testCreate_TriplesAndLeaves() throws Exception {
        AhoBuild ahoBuild = mock(AhoBuild.class);
        UserInput input = mock(UserInput.class);
        Set triples = mock(Set.class);
        Set leaves = mock(Set.class);
        Tree tree = mock(Tree.class);
        when(ahoBuild.build(triples, leaves)).thenReturn(tree);

        Aho uut = new Aho(ahoBuild, input);
        uut.create(triples, leaves);

        verify(ahoBuild).build(triples, leaves);
    }

}
