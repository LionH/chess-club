package org.chesscorp.club.service;

import org.alcibiade.chess.model.ChessGameStatus;
import org.alcibiade.chess.model.ChessMovePath;
import org.alcibiade.chess.model.ChessPosition;
import org.alcibiade.chess.persistence.PgnMarshaller;
import org.alcibiade.chess.rules.ChessHelper;
import org.alcibiade.chess.rules.ChessRules;
import org.chesscorp.club.exception.InvalidChessMoveException;
import org.chesscorp.club.model.ChessGame;
import org.chesscorp.club.model.ChessMove;
import org.chesscorp.club.model.Player;
import org.chesscorp.club.persistence.ChessGameRepository;
import org.chesscorp.club.persistence.ChessMoveRepository;
import org.chesscorp.club.persistence.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ChessGameServiceImpl implements ChessGameService {

    @Autowired
    private ChessGameRepository chessGameRepository;
    @Autowired
    private ChessMoveRepository chessMoveRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ChessRules chessRules;
    @Autowired
    private PgnMarshaller pgnMarshaller;

    @Override
    @Transactional
    public ChessGame createGame(String whitePlayer, String blackPlayer) {
        Player white = playerRepository.getOne(whitePlayer);
        Player black = playerRepository.getOne(blackPlayer);

        if (white.equals(black)) {
            throw new IllegalStateException("Can't open a game with a single player on both sides");
        }

        ChessGame game = new ChessGame(white, black);
        return chessGameRepository.save(game);
    }

    @Override
    @Transactional(readOnly = true)
    public ChessGame getGame(String id) {
        return chessGameRepository.getOne(id);
    }

    @Override
    @Transactional
    public ChessGame move(ChessGame game, String pgnMove) {
        try {
            ChessPosition position = chessRules.getInitialPosition();

            for (ChessMove gameMove : game.getMoves()) {
                ChessMovePath path = pgnMarshaller.convertPgnToMove(position, gameMove.getPgn());
                position = ChessHelper.applyMoveAndSwitch(chessRules, position, path);
            }

            ChessMovePath path = pgnMarshaller.convertPgnToMove(position, pgnMove);
            String canonicalPgn = pgnMarshaller.convertMoveToPgn(position, path);
            ChessPosition updatedPosition = ChessHelper.applyMoveAndSwitch(chessRules, position, path);
            ChessGameStatus status = chessRules.getStatus(updatedPosition);

            ChessMove move = chessMoveRepository.save(new ChessMove(canonicalPgn));
            ChessGame updatedGame = chessGameRepository.save(new ChessGame(game, move, status));

            return updatedGame;
        } catch (org.alcibiade.chess.model.ChessException e) {
            throw new InvalidChessMoveException(pgnMove, e);
        }
    }
}
