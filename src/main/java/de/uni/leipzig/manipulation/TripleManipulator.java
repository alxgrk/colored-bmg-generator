package de.uni.leipzig.manipulation;

import java.util.Iterator;
import java.util.Set;

import de.uni.leipzig.model.Triple;

public class TripleManipulator<T extends Triple> {

	private Integer percentage;

	public TripleManipulator(Integer percentage) {
		if (percentage < 0 || percentage > 99)
			throw new IllegalArgumentException("No valid percentage - must be between 1 & 99 %");

		this.percentage = percentage;
	}

	public void manipulate(Set<T> tripleSet) {
		if (percentage == 0)
			return;
		
		Integer toBeDeleted = tripleSet.size() * percentage / 100;
		System.out.println("remove " + toBeDeleted + " triples from set");
		while (toBeDeleted-- != 0) {
			Iterator<T> iterator = tripleSet.iterator();
			if (iterator.hasNext()) {
				tripleSet.remove(iterator.next());
			} else 
				break;
		}
	}
	
}
