package de.uni.leipzig.method;

import static org.mockito.Mockito.*;

import java.util.Set;

import org.junit.Test;

import de.uni.leipzig.informative.InformativeTripleFinder;
import de.uni.leipzig.model.*;

public class AhoInformativeTest {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testCreate_AdjacencyList() throws Exception {
        InformativeTripleFinder informativeTripleFinder = mock(InformativeTripleFinder.class);
        Aho aho = mock(Aho.class);
        AdjacencyList adjList = mock(AdjacencyList.class);
        Set triples = mock(Set.class);
        Set leaves = mock(Set.class);
        when(informativeTripleFinder.findTriple(adjList)).thenReturn(triples);
        when(informativeTripleFinder.getLeaves()).thenReturn(leaves);

        AhoInformative uut = new AhoInformative(informativeTripleFinder, aho);
        uut.create(adjList);

        verify(informativeTripleFinder).findTriple(adjList);
        verify(informativeTripleFinder).getLeaves();
        verify(aho).create(triples, leaves);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testCreate_TriplesAndDiGraph() throws Exception {
        InformativeTripleFinder informativeTripleFinder = mock(InformativeTripleFinder.class);
        Aho aho = mock(Aho.class);
        DiGraph diGraph = mock(DiGraph.class);
        Set triples = mock(Set.class);
        Set leaves = mock(Set.class);
        when(informativeTripleFinder.findTriple(triples, diGraph)).thenReturn(triples);
        when(informativeTripleFinder.getLeaves()).thenReturn(leaves);

        AhoInformative uut = new AhoInformative(informativeTripleFinder, aho);
        uut.create(triples, diGraph);

        verify(informativeTripleFinder).findTriple(triples, diGraph);
        verify(informativeTripleFinder).getLeaves();
        verify(aho).create(triples, leaves);
    }
}
