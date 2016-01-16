package org.chesscorp.club.jobs;

import org.chesscorp.club.monitoring.PerformanceMonitor;
import org.chesscorp.club.service.ChessPositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @JmsListener(destination = "chess-game-update")
    @Transactional(propagation = Propagation.NEVER)
    public void gameUpdated(String message) {
        logger.debug("Game {} updated", message);
        performanceMonitor.mark();
        long moves = chessPositionService.updateMovePositions();
        performanceMonitor.register("IndexUpdateJob", "update-positions", moves, "move");
    }
}
