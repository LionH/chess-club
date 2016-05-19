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
 * Gravatar hashing tests
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class GravatarHashManagerTest {

    @Autowired
    private GravatarHashManager hashManager;

    @Test
    public void testHashing() {
        Assertions.assertThat(hashManager.hashGravatar("MyEmailAddress@example.com "))
                .isEqualTo("0bc83cb571cd1c50ba6f3e8a78ef1346");
    }

}
