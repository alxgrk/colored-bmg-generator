package de.uni.leipzig.method;

import static org.mockito.Mockito.*;

import org.junit.Test;

import de.uni.leipzig.model.*;
import de.uni.leipzig.twocolored.DiGraphExtractor;

public class ThinnessClassTest {

    @Test
    public void testCreate_AdjacencyList() throws Exception {
        DiGraphExtractor extractor = mock(DiGraphExtractor.class);
        AdjacencyList adjList = mock(AdjacencyList.class);
        DiGraph diGraph = mock(DiGraph.class);
        Tree tree = mock(Tree.class);
        when(extractor.extract(adjList)).thenReturn(diGraph);
        when(diGraph.getHasseDiagram()).thenReturn(tree);

        ThinnessClass uut = new ThinnessClass(extractor);
        uut.create(adjList);

        verify(extractor).extract(adjList);
        verify(diGraph).getNodes();
        verify(diGraph).getEdges();
        verify(diGraph).getNeighboursByTc();
        verify(diGraph).getReachablesByTc();
        verify(diGraph).getHasseDiagram();
        verify(tree).toNewickNotation();
        verify(tree).print();
    }

    @Test
    public void testCreate_DiGraph() throws Exception {
        DiGraph diGraph = mock(DiGraph.class);
        Tree tree = mock(Tree.class);
        when(diGraph.getHasseDiagram()).thenReturn(tree);

        ThinnessClass uut = new ThinnessClass();
        uut.create(diGraph);

        verify(diGraph).getNodes();
        verify(diGraph).getEdges();
        verify(diGraph).getNeighboursByTc();
        verify(diGraph).getReachablesByTc();
        verify(diGraph).getHasseDiagram();
        verify(tree).toNewickNotation();
        verify(tree).print();
    }

}
