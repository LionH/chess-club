package org.chesscorp.club.ai;

import org.alcibiade.chess.model.*;
import org.alcibiade.chess.model.boardupdates.ChessBoardUpdate;
import org.alcibiade.chess.persistence.PgnMarshaller;
import org.alcibiade.chess.rules.ChessRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Yet another completely random AI.
 */
@Component("randomAI")
public class RandomAI implements ChessAI {

    @Autowired
    private ChessRules chessRules;

    @Autowired
    private PgnMarshaller pgnMarshaller;

    @Override
    public String computeNextMove(String options, List<String> pgnMoves) throws PgnMoveException, IllegalMoveException {

        ChessPosition position = chessRules.getInitialPosition();

        for (String pgn : pgnMoves) {
            ChessMovePath movePath = pgnMarshaller.convertPgnToMove(position, pgn);
            List<ChessBoardUpdate> updates = chessRules.getUpdatesForMove(position, movePath);

            ChessBoardModel afterMove = new ChessBoardModel();
            afterMove.setPosition(position);
            for (ChessBoardUpdate update : updates) {
                update.apply(afterMove);
            }
            afterMove.nextPlayerTurn();

            position = afterMove;
        }

        Set<ChessMovePath> availableMoves = chessRules.getAvailableMoves(position);

        String result = null;

        if (!availableMoves.isEmpty()) {
            ChessMovePath selectedMove = pickRandom(availableMoves);
            result = pgnMarshaller.convertMoveToPgn(position, selectedMove);
        }

        return result;
    }

    private ChessMovePath pickRandom(Set<ChessMovePath> paths) {
        int size = paths.size();
        int item = new Random().nextInt(size);
        int i = 0;

        for (ChessMovePath path : paths) {
            if (i == item) {
                return path;
            }

            i = i + 1;
        }

        // Should never occur
        assert false;
        return null;
    }
}
