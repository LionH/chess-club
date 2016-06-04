package org.chesscorp.club.service;

import org.chesscorp.club.model.people.RobotPlayer;
import org.chesscorp.club.persistence.RobotCacheRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Persistent cache for robots
 */
@Component
@Qualifier("cache")
public class ChessRobotServiceCache implements ChessRobotService {
    private Logger logger = LoggerFactory.getLogger(ChessRobotServiceCache.class);

    @Autowired
    private RobotCacheRepository robotCacheRepository;

    @Autowired
    @Qualifier("direct")
    private ChessRobotService directService;

    @Override
    @Transactional
    public String play(RobotPlayer robotPlayer, List<String> moves) {
        String move = directService.play(robotPlayer, moves);
        logger.debug("{} plays {}", robotPlayer.getDisplayName(), move);
        return move;
    }
}
