package org.chesscorp.club.ai;

import org.alcibiade.chess.model.ChessMovePath;
import org.alcibiade.chess.model.ChessPosition;
import org.alcibiade.chess.persistence.PgnMarshaller;
import org.alcibiade.chess.rules.ChessRules;
import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class GnuChessAITest {

    private Logger logger = LoggerFactory.getLogger(GnuChessAITest.class);

    @Autowired
    private ChessRules chessRules;

    @Autowired
    private PgnMarshaller pgnMarshaller;

    @Autowired
    private GnuChessAI gnuChessAI;

    @Test
    public void testAI() {
        ChessPosition initialPosition = chessRules.getInitialPosition();
        Set<ChessMovePath> availableMoves = chessRules.getAvailableMoves(initialPosition);

        String firstMovePgn = gnuChessAI.computeNextMove("", new ArrayList<>());

        logger.debug("First AI move is {}", firstMovePgn);

        ChessMovePath firstMovePath = pgnMarshaller.convertPgnToMove(initialPosition, firstMovePgn);
        Assertions.assertThat(availableMoves).contains(firstMovePath);
    }
}
