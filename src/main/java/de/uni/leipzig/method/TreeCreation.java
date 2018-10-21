package de.uni.leipzig.method;

import java.util.Set;
import java.util.function.Supplier;

import de.uni.leipzig.model.*;
import de.uni.leipzig.user.UserInput;
import lombok.RequiredArgsConstructor;

/**
 * An interface for tree creation methods. The implementation of the creation via a adjacency list
 * is required, all other creation ways may or may not be implemented.
 */
public interface TreeCreation {

    Tree create(AdjacencyList adjList);

    default Tree create(Set<Triple> triples, DiGraph diGraph) {
        return new Tree(Node.helpNode());
    }

    default Tree create(Set<Triple> triples, Set<Node> leaves) {
        return new Tree(Node.helpNode());
    }

    default Tree create(DiGraph graph) {
        return new Tree(Node.helpNode());
    }

    TreeCreation inNonInteractiveMode(boolean mode);

    @RequiredArgsConstructor
    enum Method {
        AHO(Aho::new),
        AHO_INFORMATIVE(AhoInformative::new),
        THINNESS_CLASS(ThinnessClass::new);

        private final Supplier<TreeCreation> method;

        public TreeCreation get() {
            return method.get().inNonInteractiveMode(false);
        }
    }

    public static void askForInteractiveMode(TreeCreation tc, UserInput ui) {
        ui.clear();

        ui.register("yes", () -> {
            tc.inNonInteractiveMode(false);
        });
        ui.register("no", () -> {
            tc.inNonInteractiveMode(true);
        });

        ui.askWithOptions("Do you want to run tree creation in interactive mode?");
    }

}
