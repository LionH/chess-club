package org.chesscorp.club.integration;

import org.alcibiade.chess.model.IllegalMoveException;
import org.alcibiade.chess.model.PgnMoveException;
import org.chesscorp.club.ai.RandomAI;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.people.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Create chess games for test purposes.
 */
@Component
public class ChessGameFactory {

    @Autowired
    private RandomAI randomAI;

    public ChessGame createGame(Player white, Player black, int moveCount) throws PgnMoveException, IllegalMoveException {
        ChessGame game = new ChessGame(white, black);


        List<String> pgnMoves = new ArrayList<>();
        for (int moveIndex = 0; moveIndex < moveCount; moveIndex++) {
            String pgnMove = randomAI.computeNextMove("", pgnMoves);
            game.addMove(OffsetDateTime.now(), pgnMove);
        }

        return game;
    }
}
