package de.uni.leipzig.parser;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Set;

import org.jgrapht.alg.util.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.zalando.fauxpas.ThrowingRunnable;

import de.uni.leipzig.model.*;
import de.uni.leipzig.user.*;

@RunWith(MockitoJUnitRunner.class)
public class BlastGraphParserTest {

    @Captor
    ArgumentCaptor<ThrowingRunnable<Exception>> captor;

    @Test
    public void testGetBlastGraphFile() throws Exception {
        UserInput mockInput = mock(UserInput.class);
        File cwd = new File(getClass().getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getFile());

        BlastGraphParser uut = new BlastGraphParser();
        Result<File> file = uut.getBlastGraphFile(cwd, mockInput);

        verify(mockInput).register(eq("parser-test.blast-graph"), captor.capture());
        verify(mockInput).register(eq("custom path"), any());

        captor.getValue().run();
        assertThat(file.getValue()).hasName("parser-test.blast-graph");
    }

    @Test
    public void testParseDiGraph() throws Exception {

        File input = new File(BlastGraphParserTest.class.getResource("two-colored.blast-graph")
                .toURI());

        BlastGraphParser uut = new BlastGraphParser();
        DiGraph graph = uut.parseDiGraph(input);

        assertThat(graph).isNotNull();
    }

    @Test
    public void testColorsIn_TwoColored() throws Exception {

        File input = new File(BlastGraphParserTest.class.getResource("two-colored.blast-graph")
                .toURI());

        BlastGraphParser uut = new BlastGraphParser();
        Integer count = uut.colorsIn(input);

        assertThat(count).isEqualTo(2);
    }

    @Test
    public void testColorsIn_ThreeColored() throws Exception {

        File input = new File(BlastGraphParserTest.class.getResource("three-colored.blast-graph")
                .toURI());

        BlastGraphParser uut = new BlastGraphParser();
        Integer count = uut.colorsIn(input);

        assertThat(count).isEqualTo(3);
    }

    @Test
    public void testColorsIn_FourColored() throws Exception {

        File input = new File(BlastGraphParserTest.class.getResource("complete.blast-graph")
                .toURI());

        BlastGraphParser uut = new BlastGraphParser();
        Integer count = uut.colorsIn(input);

        assertThat(count).isEqualTo(5);
    }

    @Test
    public void testParseTriple() throws Exception {

        File input = new File(BlastGraphParserTest.class.getResource("two-colored.blast-graph")
                .toURI());

        BlastGraphParser uut = new BlastGraphParser();
        Pair<Set<Triple>, Set<Node>> triplesAndNodes = uut.parseTriple(input);

        assertThat(triplesAndNodes).isNotNull();
    }

}
