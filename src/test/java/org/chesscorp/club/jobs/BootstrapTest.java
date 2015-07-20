package org.chesscorp.club.jobs;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.model.ChessGame;
import org.chesscorp.club.persistence.ChessGameRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Created by yk on 20/07/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@TransactionConfiguration(defaultRollback = true)
@ActiveProfiles(profiles = "bootstrap")
public class BootstrapTest {

    @Autowired
    private ChessGameRepository chessGameRepository;

    @Test
    public void testSampleData() {
        Assertions.assertThat(chessGameRepository.findAll()).hasAtLeastOneElementOfType(ChessGame.class);
    }
}
