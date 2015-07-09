package org.chesscorp.club.service;

import org.chesscorp.club.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@TransactionConfiguration(defaultRollback = true)
public class ChessGameServiceTest {
    private Logger logger = LoggerFactory.getLogger(ChessGameServiceTest.class);

    @Autowired
    private ChessGameService chessGameService;

    @Test
    @Transactional
    public void testGameCreation() {
        logger.debug("Testing game creation");
    }

}
