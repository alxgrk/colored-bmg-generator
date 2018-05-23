
package de.uni.leipzig;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import de.uni.leipzig.model.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RandomTree {

	private final int maxKinder;
	private final int maxTiefe;
	private final int maxLabel;
	private List<List<Node>> adjList = new ArrayList<>();

	public void create() {
		ArrayList<Integer> arrayList = new ArrayList<>();
		arrayList.add(0);
		create(arrayList);
		link();
	}

	private void create(List<Integer> ids) {

		// erzeuge neue linkedList in Arraylist und neuen Node
		List<Node> childList = new LinkedList<Node>();
		adjList.add(childList);

		Node node = Node.of(randomLabel(), ids);
		childList.add(node);

		if (randomBoolean() == true && ids.size() < maxTiefe) {

			// bestimme wie viele Abzweige
			int zahl = randomInt(maxKinder);
			// System.out.println("erzeuge verzweigung mit " + zahl + "
			// Kindern");

			for (int i = 1; i <= zahl; i++) {
				// erzeugung des neuen Namen-Arrays
				ids.add(i);

				// rekursive Funktion
				create(ids);
			}
		} else {
			return;
		}
	}

	private void link() {

		for (int i = 0; i < adjList.size() - 1; i++) { // Eintrag
			for (int j = 0; j < adjList.size(); j++) { // Vergleichseintrag
				
				//Vergleich der Baumtiefe über die Länge des ID-Arrays. Bei Abstand von 1 wäre eine Kind-beziehung möglich
				if (adjList.get(i).get(0).getIds().size() == adjList.get(j).get(0).getIds().size() - 1) {
					// vergleicht die ID-Arrays miteinander bis entweder eine ungleichheit auftritt, oder das kürzere Array vollständig durchlaufen wurde 
					int v = 0; 
					while (adjList.get(i).get(0).getIds().get(v) == adjList.get(j).get(0).getIds().get(v)) {
						if (v == adjList.get(i).get(0).getIds().size() - 1) {
							// hängt den vergleichseintrag an den Eintrag, sofern das kürzere ID-Array vollständig übereingestimmt hat
							adjList.get(i).add(adjList.get(j).get(0));
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
