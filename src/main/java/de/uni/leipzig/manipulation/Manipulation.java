package de.uni.leipzig.manipulation;

import java.util.Set;

import org.zalando.fauxpas.ThrowingRunnable;

import com.google.common.annotations.VisibleForTesting;

import de.uni.leipzig.model.*;
import de.uni.leipzig.uncolored.AhoBuild;
import de.uni.leipzig.user.UserInput;

public class Manipulation {

    private final Set<Triple> triples;

    private final Set<Node> leaves;

    private final UserInput manipulate;

    private final AhoBuild ahoBuild;

    public Manipulation(Set<Triple> triples, Set<Node> leaves) {
        this(triples, leaves, new UserInput());
    }

    public Manipulation(Set<Triple> triples, Set<Node> leaves,
            UserInput manipulate) {
        this(triples, leaves, manipulate, new AhoBuild());
    }

    @VisibleForTesting
    protected Manipulation(Set<Triple> triples, Set<Node> leaves,
            UserInput manipulate, AhoBuild ahoBuild) {
        this.triples = triples;
        this.leaves = leaves;
        this.manipulate = manipulate;
        this.ahoBuild = ahoBuild;

        this.manipulate.clear();

        this.manipulate.register("no", () -> {
        });

        this.manipulate.register("yes", this::apply);

        this.manipulate.askWithOptions("Do you want to manipulate the triple set?");
    }

    public void apply() {
        manipulate.clear();

        manipulate.register("deletion", kind(DeletionManipulator.class));
        manipulate.register("insertion", kind(InsertionManipulator.class));
        manipulate.register("invert triples", kind(InversionManipulator.class));

        manipulate.askWithOptions("What kind of manipulation do you want to apply?");
    }

    private ThrowingRunnable<Exception> kind(Class<? extends Manipulator> manipulatorClass) {
        return () -> {
            System.out.println("How many percent of the triple set should be manipulated?");
            Integer percentage = Integer.parseInt(manipulate.listenForResult());

            System.out.println("How the tree looked before:");
            Tree result = ahoBuild.build(triples, leaves);

            System.out.println(result.toNewickNotation());
            System.out.println(result.print());

            Manipulator manipulator = manipulatorClass.getDeclaredConstructor(Integer.class)
                    .newInstance(percentage);
            manipulator.manipulate(triples, leaves);

            System.out.println("How the tree looks after manipulating:");
        };
    }

}
