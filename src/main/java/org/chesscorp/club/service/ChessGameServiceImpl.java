package org.chesscorp.club.service;

import org.alcibiade.chess.model.ChessGameStatus;
import org.alcibiade.chess.model.ChessMovePath;
import org.alcibiade.chess.model.ChessPosition;
import org.alcibiade.chess.persistence.PgnBookReader;
import org.alcibiade.chess.persistence.PgnGameModel;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ChessGameServiceImpl implements ChessGameService {
    private Logger logger = LoggerFactory.getLogger(ChessGameServiceImpl.class);

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
    public ChessGame createGame(Number whitePlayer, Number blackPlayer) {
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
    public ChessGame getGame(Number id) {
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

    @Override
    @Transactional(readOnly = true)
    public List<ChessGame> searchGames(Number playerId) {
        long longValue = playerId.longValue();
        List<ChessGame> games = chessGameRepository.findByWhitePlayerIdOrBlackPlayerId(longValue, longValue);
        return games;
    }

    @Override
    @Transactional
    public void batchImport(InputStream pgnStream) throws IOException {
        logger.debug("Importing games as batch");
        PgnBookReader bookReader = new PgnBookReader(pgnStream);
        int gamesCount = 0;
        PgnGameModel pgnGameModel;

        while ((pgnGameModel = bookReader.readGame()) != null) {
            gamesCount += 1;
            logger.trace("    - {}", pgnGameModel);

            List<ChessMove> chessMoves = new ArrayList<>();

            pgnGameModel.getMoves().forEach(m -> {
                ChessMove move = new ChessMove(m);
                chessMoveRepository.save(move);
                chessMoves.add(move);
            });

            Player playerW = playerRepository.findByDisplayName("Alcibiade").get(0);
            Player playerB = playerRepository.findByDisplayName("Bob").get(0);
            ChessGame chessGame = new ChessGame(playerW, playerB, chessMoves, ChessGameStatus.OPEN, new Date());
            chessGameRepository.saveAndFlush(chessGame);
        }

        logger.debug("Imported {} games", gamesCount);
    }
}
