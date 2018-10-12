package de.uni.leipzig.user;

import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.*;

import org.junit.Test;
import org.zalando.fauxpas.*;

import de.uni.leipzig.method.TreeCreation;
import de.uni.leipzig.method.TreeCreation.Method;
import de.uni.leipzig.model.AdjacencyList;

public class UserInputTest {

    InputStream mockIs = new ByteArrayInputStream("test".getBytes());

    @Test
    public void testRegister() throws Exception {

        UserInput uut = new UserInput(mockIs);
        uut.register("test", () -> {
        });

        assertThat(uut.getActions()).hasSize(1);
    }

    @Test
    public void testRegister_TreeCreationMethod_Action() throws Exception {
        Method method = TreeCreation.Method.AHO;
        ThrowingConsumer<TreeCreation, Exception> action = tc -> {
            assertThat(tc).isInstanceOf(TreeCreation.class);
        };

        UserInput uut = new UserInput(mockIs);
        uut.register(method, action);

        assertThat(uut.getActions())
                .hasSize(1)
                .containsKey("Aho");
        uut.getActions().get("Aho").run();
    }

    @Test
    public void testRegister_TreeCreationMethod_AdjList() throws Exception {
        Method method = TreeCreation.Method.AHO;
        AdjacencyList adjList = mock(AdjacencyList.class);

        UserInput uut = new UserInput(mockIs);
        uut.register(method, adjList);

        assertThat(uut.getActions())
                .hasSize(1)
                .containsKey("Aho");
    }

    @Test
    public void testListenForResult() throws Exception {

        UserInput uut = new UserInput(mockIs);
        String result = uut.listenForResult();

        assertThat(result).isEqualTo("test");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testListen() throws Exception {
        UserInput uut = new UserInput(mockIs);
        ThrowingRunnable<Exception> actionToTest = mock(ThrowingRunnable.class);
        ThrowingRunnable<Exception> actionToIgnore = mock(ThrowingRunnable.class);
        uut.register("test", actionToTest);
        uut.register("ignore", actionToIgnore);

        uut.listen();

        verify(actionToTest).run();
        verifyZeroInteractions(actionToIgnore);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testListen_WithConversion() throws Exception {
        UserInput uut = new UserInput(mockIs);
        ThrowingRunnable<Exception> actionToExecute = mock(ThrowingRunnable.class);
        ThrowingRunnable<Exception> actionToIgnore = mock(ThrowingRunnable.class);
        uut.register("execute", actionToExecute);
        uut.register("ignore", actionToIgnore);

        uut.listen(t -> t.equals("test") ? "execute" : "");

        verify(actionToExecute).run();
        verifyZeroInteractions(actionToIgnore);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAsk() throws Exception {
        UserInput uut = new UserInput(mockIs);
        ThrowingRunnable<Exception> actionToTest = mock(ThrowingRunnable.class);
        ThrowingRunnable<Exception> actionToIgnore = mock(ThrowingRunnable.class);
        uut.register("test", actionToTest);
        uut.register("ignore", actionToIgnore);

        uut.ask("Test");

        verify(actionToTest).run();
        verifyZeroInteractions(actionToIgnore);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAskWithOptions() throws Exception {
        InputStream option1Is = new ByteArrayInputStream("1".getBytes());
        UserInput uut = new UserInput(option1Is);
        ThrowingRunnable<Exception> actionToTest = mock(ThrowingRunnable.class);
        ThrowingRunnable<Exception> actionToIgnore = mock(ThrowingRunnable.class);
        uut.register("test", actionToTest);
        uut.register("ignore", actionToIgnore);

        uut.askWithOptions("Test");

        verify(actionToTest).run();
        verifyZeroInteractions(actionToIgnore);
    }

    @Test
    public void testAskForTrigger() throws Exception {

        UserInput uut = new UserInput(mockIs);
        boolean result = uut.askForTrigger("test", "test");

        assertThat(result).isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testClear() throws Exception {
        UserInput uut = new UserInput(mockIs);
        ThrowingRunnable<Exception> actionToTest = mock(ThrowingRunnable.class);
        ThrowingRunnable<Exception> actionToIgnore = mock(ThrowingRunnable.class);
        uut.register("test", actionToTest);
        uut.register("ignore", actionToIgnore);

        uut.clear();

        assertThat(uut.getActions()).isEmpty();
    }

}
