package de.uni.leipzig.parser;

import java.io.File;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import de.uni.leipzig.model.DiGraph;

public class BlastGraphParserTest {

    @Test
        public void testParseDiGraphr() throws Exception {
    
            File input = new File(BlastGraphParserTest.class.getResource("myproject.blast-graph").toURI());
    
            BlastGraphParser uut = new BlastGraphParser();
            DiGraph graph = uut.parseDiGraph(input);
    
            assertThat(graph).isNotNull();
        }

}