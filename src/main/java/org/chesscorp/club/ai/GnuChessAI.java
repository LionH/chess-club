package org.chesscorp.club.ai;

import org.alcibiade.chess.engine.GnuChessEngineImpl;
import org.alcibiade.chess.model.IllegalMoveException;
import org.alcibiade.chess.model.PgnMoveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * GnuChess external engine AI.
 */
@Component("gnuchessAI")
public class GnuChessAI implements ChessAI {

    private GnuChessEngineImpl gnuChessEngine;

    @Autowired
    public GnuChessAI(GnuChessEngineImpl gnuChessEngine) {
        this.gnuChessEngine = gnuChessEngine;
    }

    @Override
    public String computeNextMove(String options, List<String> pgnMoves) throws PgnMoveException, IllegalMoveException {
        int depth = Integer.valueOf(options);
        String move = gnuChessEngine.computeNextMove(depth, 0, pgnMoves);
        return move;
    }
}
