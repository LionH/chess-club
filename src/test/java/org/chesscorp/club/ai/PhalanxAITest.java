package org.chesscorp.club.ai;

import org.chesscorp.club.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("phalanx")
@Rollback
public class PhalanxAITest {

    @Autowired
    private PhalanxAI phalanxAI;

    @Autowired
    private GenericTestHelper genericTestHelper;

    @Test
    public void testAI() {
        genericTestHelper.testAI(phalanxAI);
    }
}
