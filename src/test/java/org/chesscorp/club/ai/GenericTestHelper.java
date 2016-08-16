package org.chesscorp.club.ai;

import org.alcibiade.chess.model.ChessMovePath;
import org.alcibiade.chess.model.ChessPosition;
import org.alcibiade.chess.persistence.PgnMarshaller;
import org.alcibiade.chess.rules.ChessRules;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;

@Component
public class GenericTestHelper {
    private Logger logger = LoggerFactory.getLogger(GenericTestHelper.class);

    private ChessRules chessRules;

    private PgnMarshaller pgnMarshaller;

    @Autowired
    public GenericTestHelper(ChessRules chessRules, PgnMarshaller pgnMarshaller) {
        this.chessRules = chessRules;
        this.pgnMarshaller = pgnMarshaller;
    }

    public void testAI(ChessAI ai) {
        ChessPosition initialPosition = chessRules.getInitialPosition();
        Set<ChessMovePath> availableMoves = chessRules.getAvailableMoves(initialPosition);

        String firstMovePgn = ai.computeNextMove("3", new ArrayList<>());

        logger.debug("First AI move is {}", firstMovePgn);

        ChessMovePath firstMovePath = pgnMarshaller.convertPgnToMove(initialPosition, firstMovePgn);
        Assertions.assertThat(availableMoves).contains(firstMovePath);
    }
}