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

	@Test
	public void testName2() throws Exception {

		Node node1 = Node.of(0, Lists.newArrayList(0, 1));
		Node node2 = Node.of(1, Lists.newArrayList(0, 2, 1, 1, 1));
		Node node3 = Node.of(0, Lists.newArrayList(0, 2, 1, 1, 2));
		Node node4 = Node.of(0, Lists.newArrayList(0, 2, 1, 2, 1));
		Node node5 = Node.of(0, Lists.newArrayList(0, 2, 1, 2, 2));
		Node node6 = Node.of(1, Lists.newArrayList(0, 2, 2, 1, 1));
		Node node7 = Node.of(0, Lists.newArrayList(0, 2, 2, 1, 2));
		Node node8 = Node.of(1, Lists.newArrayList(0, 2, 2, 2));
		DiEdge edge1 = new DiEdge(node2, node3);
		DiEdge edge2 = new DiEdge(node2, node4);
		DiEdge edge3 = new DiEdge(node2, node5);
		DiEdge edge4 = new DiEdge(node2, node7);
		DiEdge edge5 = new DiEdge(node3, node2);
		DiEdge edge6 = new DiEdge(node4, node2);
		DiEdge edge7 = new DiEdge(node5, node2);
		DiEdge edge8 = new DiEdge(node6, node3);
		DiEdge edge9 = new DiEdge(node6, node4);
		DiEdge edge10 = new DiEdge(node6, node5);
		DiEdge edge11 = new DiEdge(node6, node7);
		DiEdge edge12 = new DiEdge(node7, node6);
		DiEdge edge13 = new DiEdge(node7, node8);
		DiEdge edge14 = new DiEdge(node8, node3);
		DiEdge edge15 = new DiEdge(node8, node4);
		DiEdge edge16 = new DiEdge(node8, node5);
		DiEdge edge17 = new DiEdge(node8, node7);

		DiGraph dgraph = new DiGraph(Sets.newHashSet(node1, node2, node3, node4, node5, node6, node7, node8),
				Sets.newHashSet(edge1, edge2, edge3, edge4, edge5, edge6, edge7, edge8, edge9, edge10, edge11, edge12,
						edge13, edge14, edge15, edge16, edge17));

		ÄquivalenzKlassenFinder uut = new ÄquivalenzKlassenFinder();
		Set<ÄquivalenzKlasse> result = uut.findÄK(dgraph);

		assertThat(result).hasSize(5);
		assertThat(result).anySatisfy(äk -> {
			assertThat(äk.contains(node1)).isTrue();
			assertThat(äk.getNodes()).hasSize(1);
		}).anySatisfy(äk -> {
			assertThat(äk.contains(node2)).isTrue();
			assertThat(äk.getNodes()).hasSize(1);
		}).anySatisfy(äk -> {
			assertThat(äk.contains(node3)).isTrue();
			assertThat(äk.contains(node4)).isTrue();
			assertThat(äk.contains(node5)).isTrue();
			assertThat(äk.getNodes()).hasSize(3);
		}).anySatisfy(äk -> {
			assertThat(äk.contains(node6)).isTrue();
			assertThat(äk.contains(node8)).isTrue();
			assertThat(äk.getNodes()).hasSize(2);
		}).anySatisfy(äk -> {
			assertThat(äk.contains(node7)).isTrue();
			assertThat(äk.getNodes()).hasSize(1);
		});

	}

}
