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

    private final int maxLabel;

    private AdjacencyList adjList = new AdjacencyList();

    @Setter
    private boolean maximalNodesWithChildren = false;

    @Setter
    private Optional<Integer> numberOfLeaves = Optional.empty();

    @Setter
    private Optional<Integer> maxDepth = Optional.empty();

    public static RandomTree askRandomTreeConfig(UserInput config) {

        System.out.println("Maximum amount of children?");
        int maxChildren = Integer.parseInt(config.listenForResult());
        System.out.println("Maximum number of labels?");
        int maxLabel = Integer.parseInt(config.listenForResult());

        RandomTree randomTree = new RandomTree(maxChildren, maxLabel);

        boolean wasTriggered = config.askForTrigger(
                "Do you want to have every node having the maximal "
                        + "amount of children? (type 'y' or leave blank)", "y");
        randomTree.maximalNodesWithChildren(wasTriggered);

        System.out.println("Number of leaves? (enter something less than 0 to skip this)");
        int numberOfLeaves = Integer.parseInt(config.listenForResult());
        if (numberOfLeaves >= 0) {
            randomTree.numberOfLeaves(Optional.of(numberOfLeaves));
        } else {
            System.out.println("Maximal depth?");
            int maxDepth = Integer.parseInt(config.listenForResult());
            randomTree.maxDepth(Optional.of(maxDepth));
        }

        return randomTree;
    }

    public AdjacencyList create() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(0);

        Optional<Integer> maxDepthByNumberOfLeaves = numberOfLeaves()
                .map(i -> (int) Math.ceil(Math.log(i) / Math.log(maxChildren)) + 4);

        create(arrayList, true, true, maxDepthByNumberOfLeaves);
        link();

        return adjList;
    }

    private void create(List<Integer> ids, Boolean shouldHaveChildren,
            Boolean checkForLeafCount,
            Optional<Integer> maxDepthByNumberOfLeaves) {

        // erzeuge neue linkedList in Arraylist und neuen Node
        List<Node> childList = new LinkedList<Node>();
        adjList.add(childList);

        Node node = Node.of(randomLabel(), ids);
        childList.add(node);
        adjList.getChildNodes().add(node);

        int depth = ids.size();
        if (shouldHaveChildren == true) {

            if (noRestrictionsApplied()
                    ||
                    maxDepthNotReached(depth)
                    ||
                    (numberOfLeavesNotReached()
                            && maxDepthByNumberOfLeavesNotReached(maxDepthByNumberOfLeaves,
                                    depth))) {

                // bestimme wie viele Abzweige
                int zahl = howManyChildren();
                // System.out.println("erzeuge verzweigung mit " + zahl + "
                // Kindern");

                // entferne 'node' wieder von 'leafList'
                adjList.getChildNodes().remove(node);

                List<Boolean> checkForLeafCountConditions = new ArrayList<>(zahl);
                for (int i = 0; i <= zahl - 1; i++) {
                    checkForLeafCountConditions.add(i, new Random().nextBoolean());
                }
                if (!checkForLeafCountConditions.contains(true))
                    checkForLeafCountConditions.set(0, true);

                for (int i = 1; i <= zahl; i++) {

                    // überspringe child creation, sobald anzahl an leaves erreicht ist
                    if (numberOfLeaves().isPresent() && !(adjList.getChildNodes()
                            .size() < numberOfLeaves().get()))
                        continue;

                    // erzeugung des neuen Namen-Arrays
                    List<Integer> copy = new ArrayList<>(ids);
                    copy.add(i);

                    Boolean shouldHaveFurtherChildren = shouldHaveChildren(depth);

                    if (checkForLeafCount && numberOfLeavesNotReached())
                        shouldHaveFurtherChildren = true;

                    // rekursive Funktion
                    create(copy, shouldHaveFurtherChildren,
                            checkForLeafCountConditions.get(i - 1), maxDepthByNumberOfLeaves);
                }
            }
        } else {
            return;
        }
    }

    private boolean maxDepthByNumberOfLeavesNotReached(Optional<Integer> maxDepthByNumberOfLeaves,
            int depth) {
        return !(maxDepthByNumberOfLeaves.isPresent() && depth > maxDepthByNumberOfLeaves.get());
    }

    private boolean numberOfLeavesNotReached() {
        return numberOfLeaves().isPresent() && adjList.getChildNodes().size() < numberOfLeaves()
                .get();
    }

    private boolean maxDepthNotReached(int depth) {
        return maxDepth().isPresent() && depth < maxDepth().get();
    }

    private boolean noRestrictionsApplied() {
        return !maxDepth().isPresent() && !numberOfLeaves().isPresent();
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
        return maxDepthNotReached(depth)
                ? true
                : new Random().nextBoolean();
    }

    private int randomLabel() {
        Random rand = new Random();
        return rand.nextInt(maxLabel);
    }

}
