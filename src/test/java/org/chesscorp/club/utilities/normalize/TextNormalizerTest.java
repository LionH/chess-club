package org.chesscorp.club.utilities.normalize;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ELO rating tests.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class TextNormalizerTest {

    @Autowired
    private TextNormalizer textNormalizer;

    @Test
    public void testNumbers() {
        Assertions.assertThat(textNormalizer.normalize("Hello1")).isEqualTo("hello");
        Assertions.assertThat(textNormalizer.normalize("Be2free")).isEqualTo("befree");
    }

    @Test
    public void testAccents() {
        Assertions.assertThat(textNormalizer.normalize("Trapèze")).isEqualTo("trapeze");
        Assertions.assertThat(textNormalizer.normalize("Hervé")).isEqualTo("herve");
    }

    @Test
    public void testSpaces() {
        Assertions.assertThat(textNormalizer.normalize("  Bob")).isEqualTo("bob");
        Assertions.assertThat(textNormalizer.normalize("Bob  ")).isEqualTo("bob");
        Assertions.assertThat(textNormalizer.normalize("Henry III")).isEqualTo("henryiii");
        Assertions.assertThat(textNormalizer.normalize("Sir\tCharles")).isEqualTo("sircharles");
    }

    @Test
    public void testPunctuation() {
        Assertions.assertThat(textNormalizer.normalize("Smith, John")).isEqualTo("smithjohn");
        Assertions.assertThat(textNormalizer.normalize("Ted O'Brien")).isEqualTo("tedobrien");
    }
}
