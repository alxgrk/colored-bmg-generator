package de.uni.leipzig.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ResultTest {

    @Test
    public void testValueFixing() throws Exception {
        Result<String> uut = Result.empty();

        assertThat(uut.getValue()).isNull();
        uut.fixValue("test");
        assertThat(uut.getValue()).isEqualTo("test");
        uut.fixValue("not accepted");
        assertThat(uut.getValue()).isEqualTo("test");
    }

}
