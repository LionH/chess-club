package org.chesscorp.club.ai;

import org.alcibiade.chess.engine.GnuChessEngineImpl;
import org.alcibiade.chess.model.IllegalMoveException;
import org.alcibiade.chess.model.PgnMoveException;
import org.alcibiade.chess.persistence.PgnMarshaller;
import org.alcibiade.chess.rules.ChessRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Yet another completely random AI.
 */
@Component("gnuchessAI")
public class GnuChessAI implements ChessAI {

    @Autowired
    private ChessRules chessRules;

    @Autowired
    private PgnMarshaller pgnMarshaller;

    @Autowired
    private GnuChessEngineImpl gnuChessEngine;

    @Override
    public String computeNextMove(String options, List<String> pgnMoves) throws PgnMoveException, IllegalMoveException {
        String move = gnuChessEngine.computeNextMove(3, 0, pgnMoves);
        return move;
    }
}
