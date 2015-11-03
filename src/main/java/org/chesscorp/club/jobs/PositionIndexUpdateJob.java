package org.chesscorp.club.jobs;

import org.chesscorp.club.service.ChessPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * Update posititions repository.
 */
@Component
@Profile("jobs")
public class PositionIndexUpdateJob {

    @Autowired
    private ChessPositionService chessPositionService;

    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void run() {
        chessPositionService.updateMovePositions();
    }

}
