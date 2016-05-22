package org.chesscorp.club.service;

import org.alcibiade.chess.model.*;
import org.alcibiade.chess.persistence.PgnGameModel;
import org.alcibiade.chess.persistence.PgnMarshaller;
import org.alcibiade.chess.rules.ChessHelper;
import org.alcibiade.chess.rules.ChessRules;
import org.chesscorp.club.exception.InvalidChessMoveException;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.game.ChessMove;
import org.chesscorp.club.model.game.EloRating;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.model.people.RobotPlayer;
import org.chesscorp.club.persistence.ChessGameRepository;
import org.chesscorp.club.persistence.ChessMoveRepository;
import org.chesscorp.club.persistence.EloRatingRepository;
import org.chesscorp.club.persistence.PlayerRepository;
import org.chesscorp.club.service.factories.PlayerFactory;
import org.chesscorp.club.utilities.elo.EloRatingCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
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
    private EloRatingCalculator eloRatingCalculator;

    @Autowired
    private JmsTemplate jmsTemplate;

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
        if (game.getStatus() != ChessGameStatus.OPEN) {
            throw new InvalidChessMoveException("Move " + pgnMove + " rejected as game "
                    + game.getId() + " is in status " + game.getStatus());
        }

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

            ChessMove move = chessMoveRepository.save(new ChessMove(game, OffsetDateTime.now(), canonicalPgn));
            ChessGame updatedGame = chessGameRepository.save(new ChessGame(game, move, status));

            if (updatedGame.getStatus() != ChessGameStatus.OPEN) {
                updatePostGame(game);
            }

            notifyGameUpdated(game);

            return updatedGame;
        } catch (org.alcibiade.chess.model.ChessException e) {
            throw new InvalidChessMoveException(pgnMove, e);
        }
    }

    /**
     * Notify game update on messaging bus.
     *
     * @param game a game that has been updated
     */
    private void notifyGameUpdated(ChessGame game) {
        jmsTemplate.send("chess-game-update", session -> {
            return session.createObjectMessage(game.getId());
        });
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
    public List<ChessGame> searchGames(Number playerId, Boolean open) {
        long longValue = playerId.longValue();
        List<ChessGameStatus> status = new ArrayList<>();

        if (open == null) {
            status.add(ChessGameStatus.BLACKWON);
            status.add(ChessGameStatus.WHITEWON);
            status.add(ChessGameStatus.OPEN);
            status.add(ChessGameStatus.PAT);
        } else if (open) {
            status.add(ChessGameStatus.OPEN);
        } else {
            status.add(ChessGameStatus.BLACKWON);
            status.add(ChessGameStatus.WHITEWON);
            status.add(ChessGameStatus.PAT);
        }

        List<ChessGame> games =
                chessGameRepository.findByWhitePlayerIdAndStatusInOrBlackPlayerIdAndStatusIn(
                        longValue, status, longValue, status).collect(Collectors.toList());
        return games;
    }

    @Override
    @Transactional
    public ChessGame batchImport(PgnGameModel pgnGameModel) {
        Player playerW = playerFactory.findOrCreateExternalPlayer(pgnGameModel.getWhitePlayerName());
        Player playerB = playerFactory.findOrCreateExternalPlayer(pgnGameModel.getBlackPlayerName());
        OffsetDateTime gameDate = OffsetDateTime.ofInstant(pgnGameModel.getGameDate().toInstant(), ZoneId.systemDefault());

        final PgnGameModel pgnG = pgnGameModel;

        List<ChessGame> matchingGames = chessGameRepository
                .findByWhitePlayerIdAndBlackPlayerIdAndStartDate(playerW.getId(), playerB.getId(), gameDate)
                .filter(g -> g.getMoves().stream().map(ChessMove::getPgn).collect(Collectors.toList()).equals(pgnG.getMoves()))
                .collect(Collectors.toList());

        logger.trace("    - {}: {} vs. {}: {} games matching",
                pgnGameModel.getGameDate(),
                pgnGameModel.getWhitePlayerName(),
                pgnGameModel.getBlackPlayerName(),
                matchingGames.size());

        if (!matchingGames.isEmpty()) {
            return null;
        }

        ChessPosition position = chessRules.getInitialPosition();
        for (String m : pgnGameModel.getMoves()) {
            try {
                position = ChessHelper.applyMoveAndSwitch(chessRules, position, pgnMarshaller.convertPgnToMove(position, m));
            } catch (ChessException e) {
                throw new IllegalStateException("Error in PGN stream for move " + m, e);
            }
        }

        ChessGameStatus status = ChessGameStatus.OPEN;

        switch (pgnGameModel.getResult()) {
            case "1-0":
                status = ChessGameStatus.WHITEWON;
                break;
            case "0-1":
                status = ChessGameStatus.BLACKWON;
                break;
            case "1/2-1/2":
                status = ChessGameStatus.PAT;
                break;
        }

        ChessGame chessGame = new ChessGame(
                playerW, playerB, new ArrayList<>(),
                gameDate, status, pgnGameModel.getSite(),
                pgnGameModel.getEvent(), pgnGameModel.getRound()
        );

        pgnGameModel.getMoves().forEach(m -> chessGame.addMove(gameDate, m));
        ChessGame result = chessGameRepository.save(chessGame);
        chessGame.getMoves().stream().forEach(chessMoveRepository::save);

        return result;
    }

    @Override
    @Transactional
    public ChessGame resign(ChessGame game, Player player) {
        ChessSide resigningSide = null;

        if (player.getId().equals(game.getWhitePlayer().getId())) {
            resigningSide = ChessSide.WHITE;
        }

        if (player.getId().equals(game.getBlackPlayer().getId())) {
            resigningSide = ChessSide.BLACK;
        }

        if (resigningSide == null) {
            throw new IllegalArgumentException(
                    "Resigning player " + player.getId() + " is not playing in game " + game.getId());
        }


        switch (resigningSide) {
            case WHITE:
                game.setStatus(ChessGameStatus.BLACKWON);
                break;

            case BLACK:
                game.setStatus(ChessGameStatus.WHITEWON);
                break;
        }

        if (game.getMoves().size() >= 2) {
            updatePostGame(game);
        }

        chessGameRepository.save(game);

        return game;
    }

}
