package de.uni.leipzig.colored;

import java.util.Set;

import org.assertj.core.util.Lists;
import org.junit.Test;

import com.google.common.collect.Sets;

import static org.assertj.core.api.Assertions.assertThat;

import de.uni.leipzig.model.DiEdge;
import de.uni.leipzig.model.DiGraph;
import de.uni.leipzig.model.Node;
import de.uni.leipzig.model.ÄquivalenzKlasse;

public class ÄquivalenzKlassenFinderTest {
	@Test
	public void testName() throws Exception {

		Node node1 = Node.of(1, Lists.newArrayList(1));
		Node node2 = Node.of(2, Lists.newArrayList(2));
		Node node3 = Node.of(1, Lists.newArrayList(3));
		Node node4 = Node.of(2, Lists.newArrayList(4));
		DiEdge edge1 = new DiEdge(node1, node2);
		DiEdge edge2 = new DiEdge(node1, node3);
		DiEdge edge3 = new DiEdge(node2, node4);
		DiEdge edge4 = new DiEdge(node3, node4);

		DiGraph dgraph = new DiGraph(Sets.newHashSet(node1, node2, node3, node4),
				Sets.newHashSet(edge1, edge2, edge3, edge4));

		ÄquivalenzKlassenFinder uut = new ÄquivalenzKlassenFinder();
		Set<ÄquivalenzKlasse> result = uut.findÄK(dgraph);

		assertThat(result).hasSize(3);
		assertThat(result).anySatisfy(äk -> {
			assertThat(äk.contains(node1)).isTrue();
			assertThat(äk.getNodes()).hasSize(1);
		}).anySatisfy(äk -> {
			assertThat(äk.contains(node2)).isTrue();
			assertThat(äk.contains(node3)).isTrue();
			assertThat(äk.getNodes()).hasSize(2);
		}).anySatisfy(äk -> {
			assertThat(äk.contains(node4)).isTrue();
			assertThat(äk.getNodes()).hasSize(1);
		});

	}
}
