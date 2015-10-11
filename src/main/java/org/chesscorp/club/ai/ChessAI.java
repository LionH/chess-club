package org.chesscorp.club.ai;

import org.alcibiade.chess.model.IllegalMoveException;
import org.alcibiade.chess.model.PgnMoveException;

import java.util.List;

/**
 * Common behaviour of Chess Artificial Intelligences.
 */
public interface ChessAI {

    String computeNextMove(String options, List<String> pgnMoves) throws PgnMoveException, IllegalMoveException;
}
