package de.uni.leipzig.model;

import org.junit.Test;

import com.google.common.collect.Lists;

import static org.assertj.core.api.Assertions.assertThat;

public class ÄquivalenzKlasseTest {
	@Test
	public void testHinzufügen() throws Exception {
		 Node node1 = Node.of(1, Lists.newArrayList(1));
	        Node node2 = Node.of(2, Lists.newArrayList(2));
	        Node node3 = Node.of(3, Lists.newArrayList(3));
	        
	        ÄquivalenzKlasse ÄK = new ÄquivalenzKlasse();
	        
	        ÄK.hinzufügen(node1);
	        ÄK.hinzufügen(node2);
	        ÄK.hinzufügen(node3);
	        
	        assertThat(ÄK.contains(node1)).isTrue();
	        assertThat(ÄK.contains(node2)).isTrue();
	        assertThat(ÄK.contains(node3)).isTrue();
	}
}
