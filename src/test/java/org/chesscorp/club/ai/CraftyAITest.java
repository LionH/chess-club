package org.chesscorp.club.ai;

import org.chesscorp.club.Application;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("ai-crafty")
@Rollback
public class CraftyAITest {

    @Autowired
    private CraftyAI craftyAI;

    @Autowired
    private GenericTestHelper genericTestHelper;

    @Test
    public void testAI() {
        genericTestHelper.testAI(craftyAI);
    }
}
