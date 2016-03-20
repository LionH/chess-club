package org.chesscorp.club.persistence;

import org.alcibiade.chess.model.ChessPosition;
import org.alcibiade.chess.persistence.PositionMarshaller;
import org.alcibiade.chess.rules.ChessRules;
import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.model.people.RobotPlayer;
import org.chesscorp.club.model.robot.RobotCacheEntry;
import org.chesscorp.club.model.stats.ChessClubPosition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test robot cache repository access methods.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class RobotCacheRepositoryTest {

    @Autowired
    private RobotCacheRepository robotCacheRepository;

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private ChessPositionRepository positionRepository;

    @Autowired
    private ChessRules chessRules;

    @Autowired
    private PositionMarshaller positionMarshaller;

    @Test
    public void testRepositoryStorage() {
        Assertions.assertThat(robotCacheRepository.findAll()).hasSize(0);

        String moveText = "e4";
        ChessPosition position = chessRules.getInitialPosition();
        ChessClubPosition clubPosition = new ChessClubPosition(positionMarshaller.convertPositionToString(position));
        clubPosition = positionRepository.save(clubPosition);

        RobotPlayer player = robotRepository.save(new RobotPlayer("Robot", "basicengine", "{}"));
        RobotCacheEntry cacheEntry = new RobotCacheEntry(player, clubPosition, moveText);
        robotCacheRepository.save(cacheEntry);

        Assertions.assertThat(robotCacheRepository.findAll()).hasSize(1);
    }
}
