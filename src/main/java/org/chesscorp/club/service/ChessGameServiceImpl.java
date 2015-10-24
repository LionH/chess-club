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
import org.chesscorp.club.model.*;
import org.chesscorp.club.persistence.ChessGameRepository;
import org.chesscorp.club.persistence.ChessMoveRepository;
import org.chesscorp.club.persistence.EloRatingRepository;
import org.chesscorp.club.persistence.PlayerRepository;
import org.chesscorp.club.service.factories.PlayerFactory;
import org.chesscorp.club.utilities.elo.EloRatingCalculator;
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
import java.util.stream.Collectors;

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
    private EloRatingRepository eloRatingRepository;
    @Autowired
    private PlayerFactory playerFactory;
    @Autowired
    private ChessRules chessRules;
    @Autowired
    private PgnMarshaller pgnMarshaller;
    @Autowired
    private Map<String, ChessAI> aiMap;
    @Autowired
    private EloRatingCalculator eloRatingCalculator;

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

            ChessMove move = chessMoveRepository.save(new ChessMove(game, new Date(), canonicalPgn));
            ChessGame updatedGame = chessGameRepository.save(new ChessGame(game, move, status));

            if (updatedGame.getStatus() == ChessGameStatus.OPEN) {
                updatedGame = checkForRobotMove(updatedGame);
            } else {
                updatePostGame(game);
            }

            return updatedGame;
        } catch (org.alcibiade.chess.model.ChessException e) {
            throw new InvalidChessMoveException(pgnMove, e);
        }
    }

    /**
     * Updates performed when a game has ended.
     *
     * @param game the game that just ended.
     */
    private void updatePostGame(ChessGame game) {
        double score = Double.NaN;

        switch (game.getStatus()) {
            case BLACKWON:
                score = 0.0;
                break;
            case WHITEWON:
                score = 1.0;
                break;
            case PAT:
                score = 0.5;
                break;
            case OPEN:
                throw new IllegalStateException("Post game triggers can't be run on open games");
        }

        Player whitePlayer = game.getWhitePlayer();
        Player blackPlayer = game.getBlackPlayer();

        EloRating ratingW = eloRatingRepository.findFirstByPlayerIdOrderByIdDesc(whitePlayer.getId());
        EloRating ratingB = eloRatingRepository.findFirstByPlayerIdOrderByIdDesc(blackPlayer.getId());

        int rW = ratingW == null ? EloRatingCalculator.INITIAL_RATING : ratingW.getEloRating();
        int rB = ratingB == null ? EloRatingCalculator.INITIAL_RATING : ratingB.getEloRating();

        int eloPoints = eloRatingCalculator.computeRatingDelta(rW, rB, score);

        int r2W = rW + eloPoints;
        int r2B = rB - eloPoints;

        logger.debug("Game {} ended, player {} rating {} -> {}, player {} rating {} -> {}",
                game.getId(),
                whitePlayer.getId(), rW, r2W,
                blackPlayer.getId(), rB, r2B
        );

        eloRatingRepository.save(new EloRating(whitePlayer, game, r2W));
        eloRatingRepository.save(new EloRating(blackPlayer, game, r2B));
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
            final PgnGameModel pgnGame = pgnGameModel;
            Player playerW = playerFactory.findOrCreatePlayer(pgnGameModel.getWhitePlayerName());
            Player playerB = playerFactory.findOrCreatePlayer(pgnGameModel.getBlackPlayerName());
            Date gameDate = pgnGameModel.getGameDate();

            List<ChessGame> matchingGames = chessGameRepository
                    .findByWhitePlayerIdAndBlackPlayerIdAndStartDate(playerW.getId(), playerB.getId(), gameDate)
                    .stream()
                    .filter(g -> g.getMoves().stream().map(ChessMove::getPgn).collect(Collectors.toList()).equals(pgnGame.getMoves()))
                    .collect(Collectors.toList());

            logger.trace("    - {}: {} vs. {}: {} games matching",
                    pgnGameModel.getGameDate(),
                    pgnGameModel.getWhitePlayerName(),
                    pgnGameModel.getBlackPlayerName(),
                    matchingGames.size());

            if (!matchingGames.isEmpty()) {
                continue;
            }

            gamesCount += 1;

            ChessGame chessGame = new ChessGame(playerW, playerB, new ArrayList<>(), ChessGameStatus.OPEN, gameDate);
            pgnGameModel.getMoves().forEach(m -> chessGame.addMove(gameDate, m));

            chessGameRepository.save(chessGame);
            chessGame.getMoves().stream().forEach(chessMoveRepository::save);
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

        if (nextPlayer instanceof RobotPlayer) {
            RobotPlayer robotPlayer = (RobotPlayer) nextPlayer;
            ChessAI ai = aiMap.get(robotPlayer.getEngine());

            if (ai == null) {
                throw new IllegalStateException("Unknwown AI: " + robotPlayer.getEngine() + " for robotPlayer " + robotPlayer.getId());
            }

            List<String> pgnMoves = new ArrayList<>();
            game.getMoves().forEach(m -> pgnMoves.add(m.getPgn()));
            try {
                String robotMove = ai.computeNextMove(robotPlayer.getParameters(), pgnMoves);
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
