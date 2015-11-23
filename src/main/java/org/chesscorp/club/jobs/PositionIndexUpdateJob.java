package org.chesscorp.club.jobs;

import org.chesscorp.club.monitoring.PerformanceMonitor;
import org.chesscorp.club.service.ChessPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Update posititions repository.
 */
@Component
@Profile("jobs")
public class PositionIndexUpdateJob {

    @Autowired
    private ChessPositionService chessPositionService;

    @Autowired
    private PerformanceMonitor performanceMonitor;

    // @Scheduled(fixedDelay = 5_000)
    @Transactional(propagation = Propagation.NEVER)
    public void run() {
        performanceMonitor.mark();
        long moves = chessPositionService.updateMovePositions();
        performanceMonitor.register("IndexUpdateJob", "update-positions", moves, "move");
    }

}
