package org.chesscorp.club.ai;

import java.util.List;

import org.alcibiade.chess.engine.PhalanxEngineImpl;
import org.alcibiade.chess.model.IllegalMoveException;
import org.alcibiade.chess.model.PgnMoveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Phalanx external engine AI.
 */
@Component("phalanxAI")
@Profile("ai-phalanx")
public class PhalanxAI implements ChessAI {

    private PhalanxEngineImpl phalanxEngine;

    @Autowired
    public PhalanxAI(PhalanxEngineImpl phalanxEngine) {
        this.phalanxEngine = phalanxEngine;
    }

    @Override
    public String computeNextMove(String options, List<String> pgnMoves) throws PgnMoveException, IllegalMoveException {
        int depth = Integer.valueOf(options);
        String move = phalanxEngine.computeNextMove(depth, 0, pgnMoves);
        return move;
    }
}
