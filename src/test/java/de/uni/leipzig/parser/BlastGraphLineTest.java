package de.uni.leipzig.parser;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class BlastGraphLineTest {

    @Test
    public void testCreation() throws Exception {
        String line = "E_10\tC_10\t1.21e-143\t425\t4.59e-136\t405";

        BlastGraphLine uut = new BlastGraphLine(line);

        assertThat(uut.getGene1().getLabel()).isEqualTo("E");
        assertThat(uut.getGene1().getName()).isEqualTo("10");
        assertThat(uut.getGene1().asNode().toString()).isEqualTo("E-10");

        assertThat(uut.getGene2().getLabel()).isEqualTo("C");
        assertThat(uut.getGene2().getName()).isEqualTo("10");
        assertThat(uut.getGene2().asNode().toString()).isEqualTo("C-10");

        assertThat(uut.getEvalueAB()).isEqualTo(1.21e-143d);
        assertThat(uut.getEvalueBA()).isEqualTo(4.59e-136d);

        assertThat(uut.getBitscoreAB()).isEqualTo(425);
        assertThat(uut.getBitscoreBA()).isEqualTo(405);
    }

}
