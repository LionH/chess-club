package org.chesscorp.club.integration;

import org.alcibiade.chess.model.IllegalMoveException;
import org.alcibiade.chess.model.PgnMoveException;
import org.chesscorp.club.ai.RandomAI;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.service.ChessGameService;
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

    private static final String[] WHITE_WIN = "e4 e5 Bc4 Bc5 Qh5 Nf6 Qxf7#".split(" ");
    private static final String[] BLACK_WIN = "e4 e5 Qh5 Ke7 Qxe5#".split(" ");

    @Autowired
    private RandomAI randomAI;

    @Autowired
    private ChessGameService chessGameService;

    public ChessGame createGame(Player white, Player black, int moveCount) throws PgnMoveException, IllegalMoveException {
        ChessGame game = new ChessGame(white, black);


        List<String> pgnMoves = new ArrayList<>();
        for (int moveIndex = 0; moveIndex < moveCount; moveIndex++) {
            String pgnMove = randomAI.computeNextMove("", pgnMoves);
            game.addMove(OffsetDateTime.now(), pgnMove);
        }

        return game;
    }

    public ChessGame createGameWonByWhite(Player white, Player black) throws PgnMoveException, IllegalMoveException {
        return createChessGame(white, black, WHITE_WIN);
    }

    public ChessGame createGameWonByBlack(Player white, Player black) throws PgnMoveException, IllegalMoveException {
        return createChessGame(white, black, BLACK_WIN);
    }

    private ChessGame createChessGame(Player white, Player black, String[] moves) {
        ChessGame game = chessGameService.createGame(white.getId(), black.getId());

        for (String pgnMove : moves) {
            chessGameService.move(game, pgnMove);
        }

        return game;
    }
}
