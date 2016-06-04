package org.chesscorp.club.service;

import org.alcibiade.chess.model.ChessPosition;
import org.alcibiade.chess.persistence.PgnMarshaller;
import org.alcibiade.chess.persistence.PositionMarshaller;
import org.alcibiade.chess.rules.ChessHelper;
import org.alcibiade.chess.rules.ChessRules;
import org.chesscorp.club.model.people.RobotPlayer;
import org.chesscorp.club.model.robot.RobotCacheEntry;
import org.chesscorp.club.persistence.RobotCacheRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Persistent cache for robots
 */
@Component
@Qualifier("cache")
@Primary
public class ChessRobotServiceCache implements ChessRobotService {
    private Logger logger = LoggerFactory.getLogger(ChessRobotServiceCache.class);
    @Autowired
    private RobotCacheRepository robotCacheRepository;

    @Autowired
    private ChessRules chessRules;
    @Autowired
    private PgnMarshaller pgnMarshaller;
    @Autowired
    @Qualifier("fixed")
    private PositionMarshaller positionMarshaller;
    @Autowired
    @Qualifier("direct")
    private ChessRobotService directService;

    @Override
    @Transactional
    public String play(RobotPlayer robotPlayer, List<String> moves) {
        String result;
        Status status;

        if (robotPlayer.isCacheable()) {
            ChessPosition position = ChessHelper.movesToPosition(chessRules, pgnMarshaller, moves);
            String positionText = positionMarshaller.convertPositionToString(position);

            RobotCacheEntry cacheEntry = robotCacheRepository.findOneByEngineAndParametersAndPosition(
                    robotPlayer.getEngine(),
                    robotPlayer.getParameters(),
                    positionText
            );

            if (cacheEntry == null) {
                result = directService.play(robotPlayer, moves);
                cacheEntry = robotCacheRepository.save(new RobotCacheEntry(robotPlayer.getEngine(), robotPlayer.getParameters(), positionText, result));
                status = Status.MIS;
            } else {
                status = Status.HIT;
            }

            result = cacheEntry.getPgnMoveText();

        } else {
            status = Status.NOC;
            result = directService.play(robotPlayer, moves);
        }

        logger.debug("{} plays {} [{}]", robotPlayer.getDisplayName(), result, status);
        return result;
    }

    private enum Status {HIT, MIS, NOC}
}
