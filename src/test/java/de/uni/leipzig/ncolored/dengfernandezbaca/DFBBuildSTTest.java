package de.uni.leipzig.ncolored.dengfernandezbaca;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

import org.junit.Test;

import com.google.common.collect.*;

import de.uni.leipzig.model.*;

public class DFBBuildSTTest {

    // @formatter:off
    Node a1 = Node.of("a", Lists.newArrayList(0,1,1));
    Node b1 = Node.of("b", Lists.newArrayList(0,1,2));
    Node c1 = Node.of("c", Lists.newArrayList(0,2));
    Node b2 = Node.of("b", Lists.newArrayList(0,1,1));
    Node c2 = Node.of("c", Lists.newArrayList(0,1,2));
    Node d2 = Node.of("d", Lists.newArrayList(0,2));
    Node c3 = Node.of("c", Lists.newArrayList(0,1,1));
    Node d3 = Node.of("d", Lists.newArrayList(0,1,2));
    Node e3 = Node.of("e", Lists.newArrayList(0,2));
    
    Tree a1t = new Tree(a1);
    Tree b1t = new Tree(b1);
    Tree c1t = new Tree(c1);
    Tree b2t = new Tree(b2);
    Tree c2t = new Tree(c2);
    Tree d2t = new Tree(d2);
    Tree c3t = new Tree(c3);
    Tree d3t = new Tree(d3);
    Tree e3t = new Tree(e3);
    
    Tree t1 = new Tree(Lists.newArrayList(
                    new Tree(Lists.newArrayList(a1t, b1t)), 
                    c1t));
    Tree t2 = new Tree(Lists.newArrayList(
                    new Tree(Lists.newArrayList(b2t, c2t)), 
                    d2t));
    Tree t3 = new Tree(Lists.newArrayList(
                    new Tree(Lists.newArrayList(c3t, d3t)), 
                    e3t));
    
    @Test
    public void testBuildST() throws Exception {
        Map<Set<Color>, Tree> preMap = new HashMap<>();
        preMap.put(Sets.newHashSet(new Color("a"), new Color("b"), new Color("c")), t1);
        preMap.put(Sets.newHashSet(new Color("b"), new Color("c"), new Color("d")), t2);
        preMap.put(Sets.newHashSet(new Color("c"), new Color("d"), new Color("e")), t3);
        
        Tree expected = new Tree(Lists.newArrayList(
                    new Tree(Lists.newArrayList(
                            new Tree(Lists.newArrayList(
                                    new Tree(Lists.newArrayList(
                                            new Tree(Node.of("a", Lists.newArrayList())), 
                                            new Tree(Node.of("b", Lists.newArrayList()))
                                        )), 
                                    new Tree(Node.of("c", Lists.newArrayList()))
                                )), 
                            new Tree(Node.of("d", Lists.newArrayList()))
                        )), 
                    new Tree(Node.of("e", Lists.newArrayList()))
                ));

        DFBBuildST buildST = new DFBBuildST();
        Tree actual = buildST.build(preMap);
        
        assertThat(actual.toNewickNotation()).isEqualTo(expected.toNewickNotation());
    }

    // @formatter:on

}
