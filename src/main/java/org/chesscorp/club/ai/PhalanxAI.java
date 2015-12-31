package org.chesscorp.club.ai;

import org.alcibiade.chess.engine.PhalanxEngineImpl;
import org.alcibiade.chess.model.IllegalMoveException;
import org.alcibiade.chess.model.PgnMoveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Phalanx external engine AI.
 */
@Component("phalanxAI")
public class PhalanxAI implements ChessAI {

    @Autowired
    private PhalanxEngineImpl phalanxEngine;

    @Override
    public String computeNextMove(String options, List<String> pgnMoves) throws PgnMoveException, IllegalMoveException {
        int depth = Integer.valueOf(options);
        String move = phalanxEngine.computeNextMove(depth, 0, pgnMoves);
        return move;
    }
}
