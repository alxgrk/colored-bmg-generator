package de.uni.leipzig;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.zalando.fauxpas.ThrowingRunnable;

import de.uni.leipzig.user.UserInput;

public class MainTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testMain() throws Exception {
        UserInput mockInput = mock(UserInput.class);

        new Main(mockInput);

        verify(mockInput).register(eq("parse a file"), any(ThrowingRunnable.class));
        verify(mockInput).register(eq("create a random tree"), any(ThrowingRunnable.class));
        verify(mockInput).askWithOptions(any());
    }

}
