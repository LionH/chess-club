package org.chesscorp.club.jobs;

import org.chesscorp.club.persistence.ChessMoveToPositionRepository;
import org.chesscorp.club.persistence.ChessPositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(PositionIndexUpdateJob.class);

    @Autowired
    private ChessPositionRepository chessPositionRepository;
    @Autowired
    private ChessMoveToPositionRepository chessMoveToPositionRepository;

    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void run() {
        logger.debug("Updating position tables");
        logger.debug("First move is {}", chessMoveToPositionRepository.findFirstByOrderByChessMoveIdDesc());
    }

}
