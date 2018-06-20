package de.uni.leipzig;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import de.uni.leipzig.model.Node;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RandomTree {

    private final int maxKinder;

    private final int maxTiefe;

    private final int maxLabel;

    private List<List<Node>> adjList = new ArrayList<>();

    public List<List<Node>> create() {
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

        if (shouldHaveChildren == true && ids.size() < maxTiefe) {

            // bestimme wie viele Abzweige
            int zahl = randomInt(maxKinder);
            // System.out.println("erzeuge verzweigung mit " + zahl + "
            // Kindern");

            for (int i = 1; i <= zahl; i++) {
                // erzeugung des neuen Namen-Arrays
                List<Integer> copy = new ArrayList<>(ids);
                copy.add(i);

                // rekursive Funktion
                create(copy, randomBoolean());
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

    private int randomInt(int max) {
        Random rand = new Random();
        return rand.nextInt(max - 2 + 1) + 2;
    }

    private Boolean randomBoolean() {
        Random rand = new Random();
        return rand.nextBoolean();
    }

    private int randomLabel() {
        Random rand = new Random();
        return rand.nextInt(maxLabel);
    }

}
