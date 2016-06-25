package org.chesscorp.club.jobs;

import org.chesscorp.club.persistence.ChessPositionRepository;
import org.chesscorp.club.service.MessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Re-start stale robot moves.
 */
@Component
public class ChessPositionRecoveryJob {

    private Logger logger = LoggerFactory.getLogger(ChessPositionRecoveryJob.class);

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private ChessPositionRepository positionRepository;

    private boolean executed = false;

    @Scheduled(initialDelay = 10_000, fixedDelay = 3600_000)
    @Transactional
    public void triggerRecovery() {
        if (!executed) {
            int notificationCount = positionRepository.findAllByScore(null)
                    .map(p -> {
                        messagingService.notifyPositionCreated(p.getId());
                        return 1;
                    })
                    .reduce(0, (a, b) -> a + b);

            if (notificationCount > 0) {
                logger.warn("Sent {} position analysis notifications", notificationCount);
            }

            executed = true;
        }
    }

}
