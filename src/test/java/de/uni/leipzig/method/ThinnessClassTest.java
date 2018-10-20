package de.uni.leipzig.method;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.uni.leipzig.model.AdjacencyList;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Tree;
import de.uni.leipzig.twocolored.DiGraphExtractor2;

public class ThinnessClassTest {

    @Test
    public void testCreate_AdjacencyList() throws Exception {
        DiGraphExtractor2 extractor = mock(DiGraphExtractor2.class);
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
