package org.chesscorp.club.jobs;

import org.chesscorp.club.dto.PlayerProfile;
import org.chesscorp.club.model.people.RobotPlayer;
import org.chesscorp.club.model.robot.RobotPreparationQuery;
import org.chesscorp.club.monitoring.PerformanceMonitor;
import org.chesscorp.club.service.ChessRobotService;
import org.chesscorp.club.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Game update post-processing.
 */
@Component
public class RobotPreparationListener {
    private Logger logger = LoggerFactory.getLogger(RobotPreparationListener.class);

    private ChessRobotService chessRobotService;
    private PlayerService playerService;
    private PerformanceMonitor performanceMonitor;

    @Autowired
    public RobotPreparationListener(ChessRobotService chessRobotService, PlayerService playerService, PerformanceMonitor performanceMonitor) {
        this.chessRobotService = chessRobotService;
        this.playerService = playerService;
        this.performanceMonitor = performanceMonitor;
    }

    @JmsListener(destination = "robot-prepare")
    @Transactional
    public void prepareRobot(RobotPreparationQuery robotPreparationQuery) {
        PlayerProfile robotProfile = playerService.getProfile(robotPreparationQuery.getRobotId());
        RobotPlayer robotPlayer = (RobotPlayer) robotProfile.getPlayer();

        logger.debug("Preparing robot {} for {} half moves", robotPlayer, robotPreparationQuery.getHalfMoves());

        int halfMovesLeft = robotPreparationQuery.getHalfMoves();
        List<String> moves = new ArrayList<>(robotPreparationQuery.getMoves());
        performanceMonitor.mark();

        while (halfMovesLeft-- > 0) {
            String move = chessRobotService.play(robotPlayer, moves);

            if (move == null) {
                // No moves available, game is ended and robot returns null
                break;
            }

            moves.add(move);
        }

        performanceMonitor.register("RobotPreparationListener", "prepare", robotPreparationQuery.getHalfMoves(), "move");
    }
}
