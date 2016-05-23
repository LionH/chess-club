package org.chesscorp.club.jobs;

import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.game.ChessMove;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.model.people.RobotPlayer;
import org.chesscorp.club.monitoring.PerformanceMonitor;
import org.chesscorp.club.service.ChessGameService;
import org.chesscorp.club.service.ChessPositionService;
import org.chesscorp.club.service.ChessRobotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Game update post-processing.
 */
@Component
public class GameUpdateListener {
    private Logger logger = LoggerFactory.getLogger(GameUpdateListener.class);

    @Autowired
    private ChessPositionService chessPositionService;
    @Autowired
    private PerformanceMonitor performanceMonitor;
    @Autowired
    private ChessRobotService chessRobotService;
    @Autowired
    private ChessGameService chessGameService;

    @JmsListener(destination = "chess-game-update")
    @Transactional
    public void gameUpdated(Long gameId) {
        logger.debug("Game {} updated", gameId);

        ChessGame game = chessGameService.getGame(gameId);
        checkForRobotMove(game);

        performanceMonitor.mark();
        long moves = chessPositionService.updateMovePositions();
        performanceMonitor.register("IndexUpdateJob", "update-positions", moves, "move");
    }

    /**
     * Make a robot move if this game's turn is on a robot.
     *
     * @param game the current game object
     * @return the new value of the game object
     */
    private ChessGame checkForRobotMove(ChessGame game) {
        Player nextPlayer = game.getNextPlayer();
        logger.debug("Next player in game {} is {} ({} moves)", game.getId(), nextPlayer, game.getMoves().size());

        if (nextPlayer instanceof RobotPlayer) {
            RobotPlayer robotPlayer = (RobotPlayer) nextPlayer;

            List<String> moves = game.getMoves().stream()
                    .map(ChessMove::getPgn)
                    .collect(Collectors.toList());

            String robotMove = chessRobotService.play(robotPlayer, moves);
            chessGameService.move(game, robotMove);
        }

        return game;
    }
}
