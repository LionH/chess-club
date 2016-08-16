package org.chesscorp.club.ai;

import org.alcibiade.chess.engine.CraftyEngineImpl;
import org.alcibiade.chess.model.IllegalMoveException;
import org.alcibiade.chess.model.PgnMoveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * GnuChess external engine AI.
 */
@Component("craftyAI")
@Profile("ai-crafty")
public class CraftyAI implements ChessAI {

    private CraftyEngineImpl craftyEngine;

    @Autowired
    public CraftyAI(CraftyEngineImpl craftyEngine) {
        this.craftyEngine = craftyEngine;
    }

    @Override
    public String computeNextMove(String options, List<String> pgnMoves) throws PgnMoveException, IllegalMoveException {
        int depth = Integer.valueOf(options);
        String move = craftyEngine.computeNextMove(depth, 0, pgnMoves);
        return move;
    }
}
