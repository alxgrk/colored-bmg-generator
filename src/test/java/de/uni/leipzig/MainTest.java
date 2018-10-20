package de.uni.leipzig;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.zalando.fauxpas.*;

import de.uni.leipzig.method.TreeCreation.Method;
import de.uni.leipzig.model.AdjacencyList;
import de.uni.leipzig.ncolored.NColored;
import de.uni.leipzig.parser.BlastGraphParser;
import de.uni.leipzig.user.*;

@RunWith(MockitoJUnitRunner.class)
public class MainTest {

    @Mock
    UserInput input;

    @Mock
    BlastGraphParser parser;

    @Mock
    NColored nColored;

    @Captor
    ArgumentCaptor<ThrowingRunnable<Exception>> captor;

    @Test
    public void testMain() throws Exception {

        Main uut = new Main(input, parser, nColored);
        uut.run();

        verify(input).register(eq("parse a file"), captor.capture());
        verify(input).register(eq("create a random tree"), captor.capture());
        verify(input).askWithOptions(any());

        ThrowingRunnable<Exception> parseAction = captor.getAllValues().get(0);
        ThrowingRunnable<Exception> randomAction = captor.getAllValues().get(1);

        testParse(parseAction);
        testRandom(randomAction);
    }

    @SuppressWarnings("unchecked")
    private void testParse(ThrowingRunnable<Exception> parseAction) throws Exception {
        File file = mock(File.class);
        Result<File> result = Result.empty();
        when(parser.getBlastGraphFile()).thenReturn(result.fixValue(file));
        when(parser.colorsIn(file)).thenReturn(2);

        reset(input);
        parseAction.run();

        verify(parser).getBlastGraphFile();
        verify(parser).colorsIn(file);
        verify(input).clear();
        verify(input).register(eq(Method.AHO), any(ThrowingConsumer.class));
        verify(input).register(eq(Method.AHO_INFORMATIVE), any(ThrowingConsumer.class));
        verify(input).register(eq(Method.THINNESS_CLASS), any(ThrowingConsumer.class));
        verify(input).askWithOptions(anyString());
        // TODO execute captured consumers
    }

    private void testRandom(ThrowingRunnable<Exception> randomAction) throws Exception {
        reset(input);
        when(input.listenForResult()).thenReturn("2");
        when(input.askForTrigger(anyString(), anyString())).thenReturn(false);

        randomAction.run();

        verify(input, times(2)).clear();
        verify(input).register(eq(Method.AHO), any(AdjacencyList.class));
        verify(input).register(eq(Method.AHO_INFORMATIVE), any(AdjacencyList.class));
        verify(input).register(eq(Method.THINNESS_CLASS), any(AdjacencyList.class));
        verify(input).askWithOptions(anyString());
    }

}
