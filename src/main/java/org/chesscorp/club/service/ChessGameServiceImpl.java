package org.chesscorp.club.service;

import org.alcibiade.chess.model.ChessException;
import org.alcibiade.chess.model.ChessGameStatus;
import org.alcibiade.chess.model.ChessMovePath;
import org.alcibiade.chess.model.ChessPosition;
import org.alcibiade.chess.persistence.PgnBookReader;
import org.alcibiade.chess.persistence.PgnGameModel;
import org.alcibiade.chess.persistence.PgnMarshaller;
import org.alcibiade.chess.rules.ChessHelper;
import org.alcibiade.chess.rules.ChessRules;
import org.chesscorp.club.ai.ChessAI;
import org.chesscorp.club.exception.InvalidChessMoveException;
import org.chesscorp.club.model.ChessGame;
import org.chesscorp.club.model.ChessMove;
import org.chesscorp.club.model.Player;
import org.chesscorp.club.model.Robot;
import org.chesscorp.club.persistence.ChessGameRepository;
import org.chesscorp.club.persistence.ChessMoveRepository;
import org.chesscorp.club.persistence.PlayerRepository;
import org.chesscorp.club.service.factories.PlayerFactory;
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
import java.util.Map;

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
    private PlayerFactory playerFactory;
    @Autowired
    private ChessRules chessRules;
    @Autowired
    private PgnMarshaller pgnMarshaller;
    @Autowired
    private Map<String, ChessAI> aiMap;

    @Override
    @Transactional
    public ChessGame createGame(Number whitePlayer, Number blackPlayer) {
        Player white = playerRepository.getOne(whitePlayer);
        Player black = playerRepository.getOne(blackPlayer);

        if (white.equals(black)) {
            throw new IllegalStateException("Can't open a game with a single player on both sides");
        }

        ChessGame game = new ChessGame(white, black);
        game = chessGameRepository.save(game);
        game = checkForRobotMove(game);
        return game;
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

            updatedGame = checkForRobotMove(updatedGame);

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
    public long batchImport(InputStream pgnStream) throws IOException {
        logger.debug("Importing games as batch");
        PgnBookReader bookReader = new PgnBookReader(pgnStream);
        long gamesCount = 0;
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

            Player playerW = playerFactory.findOrCreatePlayer(pgnGameModel.getWhitePlayerName());
            Player playerB = playerFactory.findOrCreatePlayer(pgnGameModel.getBlackPlayerName());
            ChessGame chessGame = new ChessGame(playerW, playerB, chessMoves, ChessGameStatus.OPEN, new Date());
            chessGameRepository.saveAndFlush(chessGame);
        }

        logger.debug("Imported {} games", gamesCount);

        return gamesCount;
    }

    /**
     * Make a robot move if this game's turn is on a robot.
     *
     * @param game the current game object
     * @return the new value of the game object
     */
    private ChessGame checkForRobotMove(ChessGame game) {
        Player nextPlayer = game.getNextPlayer();

        if (nextPlayer instanceof Robot) {
            Robot robot = (Robot) nextPlayer;
            ChessAI ai = aiMap.get(robot.getEngine());

            if (ai == null) {
                throw new IllegalStateException("Unknwown AI: " + robot.getEngine() + " for robot " + robot.getId());
            }

            List<String> pgnMoves = new ArrayList<>();
            game.getMoves().forEach(m -> pgnMoves.add(m.getPgn()));
            try {
                String robotMove = ai.computeNextMove(robot.getParameters(), pgnMoves);
                if (robotMove != null) {
                    game = move(game, robotMove);
                }
            } catch (ChessException e) {
                logger.warn("Inconsistent move in game " + game.getId(), e);
            }
        }

        return game;
    }
}
