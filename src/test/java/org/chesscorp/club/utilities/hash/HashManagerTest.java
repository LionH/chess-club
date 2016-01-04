package org.chesscorp.club.utilities.hash;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Hash manager tests.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class HashManagerTest {

    @Autowired
    private HashManager hashManager;

    @Test
    public void testSaltGeneration() {
        String salt1 = hashManager.createSalt();
        String salt2 = hashManager.createSalt();

        Assertions.assertThat(salt1.length()).isGreaterThan(10);
        Assertions.assertThat(salt2.length()).isGreaterThan(10);
        Assertions.assertThat(salt1).isNotEqualTo(salt2);
    }

    @Test
    public void testHashCalculation() {
        String salt1 = hashManager.createSalt();
        String salt2 = hashManager.createSalt();

        Assertions.assertThat(hashManager.hash(salt1, "Hello"))
                .hasSize(32)
                .isEqualTo(hashManager.hash(salt1, "Hello"));

        Assertions.assertThat(hashManager.hash(salt2, "Hello"))
                .hasSize(32)
                .isNotEqualTo(hashManager.hash(salt1, "Hello"));

        Assertions.assertThat(hashManager.hash(salt1, "Hello"))
                .hasSize(32)
                .isNotEqualTo(hashManager.hash(salt1, "Hello2"));
    }

}
