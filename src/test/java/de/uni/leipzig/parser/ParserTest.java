package de.uni.leipzig.parser;

import java.io.File;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import de.uni.leipzig.model.DiGraph;

public class ParserTest {

    @Test
    public void testParser() throws Exception {

        File input = new File(ParserTest.class.getResource("myproject.blast-graph").toURI());

        Parser uut = new Parser();
        DiGraph graph = uut.parse(input);

        assertThat(graph).isNotNull();
    }

}
