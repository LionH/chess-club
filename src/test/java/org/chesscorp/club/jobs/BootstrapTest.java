package org.chesscorp.club.jobs;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.persistence.ChessGameRepository;
import org.chesscorp.club.persistence.PlayerRepository;
import org.chesscorp.club.persistence.RobotRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Test the bootstrap process that initializes the storage contents.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@TransactionConfiguration(defaultRollback = true)
@ActiveProfiles(profiles = "bootstrap")
public class BootstrapTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private ChessGameRepository chessGameRepository;

    @Test
    public void testSampleData() {
        Assertions.assertThat(playerRepository.findAll()).hasSize(5);
        Assertions.assertThat(robotRepository.findAll()).hasSize(1);
        Assertions.assertThat(chessGameRepository.findAll()).hasSize(2);
    }
}
