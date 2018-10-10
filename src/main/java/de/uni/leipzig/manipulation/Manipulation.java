package de.uni.leipzig.manipulation;

import java.util.Set;

import org.zalando.fauxpas.ThrowingRunnable;

import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.Tree;
import de.uni.leipzig.model.Triple;
import de.uni.leipzig.uncolored.AhoBuild;
import de.uni.leipzig.user.UserInput;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Manipulation {

    private final Set<Triple> triples;

    private final Set<Node> leaves;

    public static void askForManipulation(Set<Triple> triples, Set<Node> leaves) {
        UserInput manipulate = new UserInput();

        manipulate.register("no", () -> {
        });

        manipulate.register("yes", () -> {
            Manipulation manipulation = new Manipulation(triples, leaves);
            manipulation.apply();
        });

        manipulate.askWithOptions("Do you want to manipulate the triple set?");
    }

    public void apply() {
        UserInput manipulationKind = new UserInput();

        manipulationKind.register("deletion", kind(DeletionManipulator.class));
        manipulationKind.register("insertion", kind(InsertionManipulator.class));
        manipulationKind.register("invert triples", kind(InversionManipulator.class));

        manipulationKind.askWithOptions("What kind of manipulation do you want to apply?");
    }

    private ThrowingRunnable<Exception> kind(Class<? extends Manipulator> manipulatorClass) {
        return () -> {
            UserInput percentageInput = new UserInput();

            System.out.println("How many percent of the triple set should be manipulated?");
            Integer percentage = Integer.parseInt(percentageInput.listenForResult());

            System.out.println("How the tree looked before:");
            AhoBuild ahoBuild = new AhoBuild();
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
