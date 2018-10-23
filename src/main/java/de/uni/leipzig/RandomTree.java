package de.uni.leipzig;

import java.util.*;

import de.uni.leipzig.model.*;
import de.uni.leipzig.user.UserInput;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public class RandomTree {

    private final int maxChildren;

    private final int maxDepth;

    private final int maxLabel;

    private AdjacencyList adjList = new AdjacencyList();

    @Setter
    private boolean maximalNodesWithChildren = false;

    @Setter
    private int minDepth = 0;

    public static RandomTree askRandomTreeConfig(UserInput config) {

        System.out.println("Maximum amount of children?");
        int maxChildren = Integer.parseInt(config.listenForResult());
        System.out.println("Maximum depth?");
        int maxDepth = Integer.parseInt(config.listenForResult());
        System.out.println("Maximum number of labels?");
        int maxLabel = Integer.parseInt(config.listenForResult());

        RandomTree randomTree = new RandomTree(maxChildren, maxDepth, maxLabel);

        System.out.println("Minimal depth?");
        int minDepth = Integer.parseInt(config.listenForResult());
        randomTree.minDepth(minDepth);

        boolean wasTriggered = config.askForTrigger(
                "Do you want to have every node having the maximal "
                        + "amount of children? (type 'y' or leave blank)", "y");
        randomTree.maximalNodesWithChildren(wasTriggered);
        return randomTree;
    }

    public AdjacencyList create() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(0);

        create(arrayList, true);
        link();

        return adjList;
    }

    private void create(List<Integer> ids, Boolean shouldHaveChildren) {

        // erzeuge neue linkedList in Arraylist und neuen Node
        List<Node> childList = new LinkedList<Node>();
        adjList.add(childList);

        Node node = Node.of(randomLabel(), ids);
        childList.add(node);

        int depth = ids.size();
        if (shouldHaveChildren == true && depth < maxDepth) {

            // bestimme wie viele Abzweige
            int zahl = howManyChildren();
            // System.out.println("erzeuge verzweigung mit " + zahl + "
            // Kindern");

            for (int i = 1; i <= zahl; i++) {
                // erzeugung des neuen Namen-Arrays
                List<Integer> copy = new ArrayList<>(ids);
                copy.add(i);

                // rekursive Funktion
                create(copy, shouldHaveChildren(depth));
            }
        } else {
            return;
        }
    }

    private void link() {

        for (int i = 0; i < adjList.size() - 1; i++) { // Eintrag
            for (int j = 0; j < adjList.size(); j++) { // Vergleichseintrag

                Node first = adjList.get(i).get(0);
                Node second = adjList.get(j).get(0);

                // Vergleich der Baumtiefe über die Länge des ID-Arrays. Bei
                // Abstand von 1 wäre eine Kind-beziehung möglich
                if (first.getIds().size() == second.getIds().size() - 1) {
                    // vergleicht die ID-Arrays miteinander bis entweder eine
                    // ungleichheit auftritt, oder das kürzere Array vollständig
                    // durchlaufen wurde
                    int v = 0;
                    while (first.getIds().get(v) == second.getIds().get(v)) {
                        if (v == first.getIds().size() - 1) {
                            // hängt den vergleichseintrag an den Eintrag,
                            // sofern das kürzere ID-Array vollständig
                            // übereingestimmt hat
                            adjList.get(i).add(second);
                            break;
                        }
                        v++;
                    }
                }
            }
        }
    }

    private int howManyChildren() {
        return maximalNodesWithChildren ? maxChildren
                : new Random().nextInt(maxChildren - 2 + 1) + 2;
    }

    private Boolean shouldHaveChildren(int depth) {
        return depth < minDepth ? true : new Random().nextBoolean();
    }

    private int randomLabel() {
        Random rand = new Random();
        return rand.nextInt(maxLabel);
    }

}
