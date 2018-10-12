package de.uni.leipzig.user;

import java.io.InputStream;
import java.util.*;
import java.util.function.Function;

import org.zalando.fauxpas.*;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;

import de.uni.leipzig.method.TreeCreation;
import de.uni.leipzig.model.AdjacencyList;
import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED, onConstructor = @__(@VisibleForTesting))
public class UserInput {

    private final Scanner scanner;

    @Getter(value = AccessLevel.PROTECTED, onMethod = @__(@VisibleForTesting))
    private Map<String, Runnable> actions = Maps.newLinkedHashMap();

    public UserInput() {
        this(System.in);
    }

    public UserInput(InputStream in) {
        this.scanner = new Scanner(in);
    }

    public void register(String trigger, ThrowingRunnable<Exception> action)
            throws RuntimeException {
        actions.put(trigger, action);
    }

    public void register(TreeCreation.Method creation,
            ThrowingConsumer<TreeCreation, Exception> action)
            throws RuntimeException {
        register(creation.get().getClass().getSimpleName(), () -> action.accept(creation.get()));
    }

    public void register(TreeCreation.Method creation, AdjacencyList adjList)
            throws RuntimeException {
        register(creation.get().getClass().getSimpleName(), () -> creation.get().create(adjList));
    }

    public String listenForResult() throws RuntimeException {
        return scanner.next();
    }

    public void listen() throws RuntimeException {
        listen(e -> e);
    }

    public void listen(Function<String, String> conversion) throws RuntimeException {

        String input = scanner.next();
        String convertedInput = conversion.apply(input);

        actions.entrySet()
                .stream()
                .filter(e -> e.getKey().equals(convertedInput))
                .findFirst()
                .ifPresent(e -> e.getValue().run());
    }

    public void ask(String question) throws RuntimeException {
        System.out.println(question);
        listen();
    }

    public void askWithOptions(String question) throws RuntimeException {
        System.out.println(question);

        Map<String, String> shortHand = Maps.newHashMap();
        Integer count = 0;
        for (String trigger : actions.keySet()) {
            count++;
            shortHand.put(count.toString(), trigger);
            System.out.println(trigger + " -> " + count);
        }

        listen(i -> shortHand.get(i));
    }

    public boolean askForTrigger(String question, String trigger) {
        System.out.println(question);
        return listenForResult().equals(trigger);
    }

    public void clear() {
        actions.clear();
    }

}
