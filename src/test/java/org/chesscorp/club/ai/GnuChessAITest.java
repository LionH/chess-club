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
@ActiveProfiles("ai-gnuchess")
@Rollback
public class GnuChessAITest {

    @Autowired
    private GnuChessAI gnuChessAI;

    @Autowired
    private GenericTestHelper genericTestHelper;

    @Test
    public void testAI() {
        genericTestHelper.testAI(gnuChessAI);
    }
}
