package de.uni.leipzig.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;

import de.uni.leipzig.model.DiGraph;

public class ParserTest {

    @Test
    public void testParser() throws Exception {

        File input = new File("myproject.blast-graph");

        Parser uut = new Parser();
        DiGraph graph = uut.parse(input);

        assertThat(graph).isNotNull();
    }

}
