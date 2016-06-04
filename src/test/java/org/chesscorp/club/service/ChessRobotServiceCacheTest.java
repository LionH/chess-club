package org.chesscorp.club.service;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.model.people.RobotPlayer;
import org.chesscorp.club.persistence.PlayerRepository;
import org.chesscorp.club.persistence.RobotCacheRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Test the caching service proxy.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class ChessRobotServiceCacheTest {

    @Autowired
    @Qualifier("cache")
    private ChessRobotService robotService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RobotCacheRepository robotCacheRepository;

    @Test
    @Transactional
    public void testService() {
        List<String> moves = new ArrayList<>();
        RobotPlayer robot1 = playerRepository.save(new RobotPlayer("Robot 1", "randomAI", "", true));
        String move = robotService.play(robot1, moves);
        Assertions.assertThat(robotCacheRepository.findAll()).hasSize(1);
        Assertions.assertThat(robotCacheRepository.findAll().get(0).getPgnMoveText()).isEqualTo(move);

        String move2 = robotService.play(robot1, moves);
        Assertions.assertThat(robotCacheRepository.findAll()).hasSize(1);
        Assertions.assertThat(move2).isEqualTo(move);
    }
}
