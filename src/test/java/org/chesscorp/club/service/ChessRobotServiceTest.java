package org.chesscorp.club.service;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.model.people.RobotPlayer;
import org.chesscorp.club.persistence.PlayerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class ChessRobotServiceTest {

    private Logger logger = LoggerFactory.getLogger(ChessRobotServiceTest.class);

    @Autowired
    private ChessRobotService chessRobotService;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    @Transactional
    public void testRobotAgainstHimself() throws IOException {
        RobotPlayer robot1 = playerRepository.save(new RobotPlayer("Robot 1", "randomAI", ""));

        ArrayList<String> moves = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String robotMove = chessRobotService.play(robot1, moves);
            Assertions.assertThat(robotMove).isNotEmpty();
            moves.add(robotMove);
            logger.debug("Current game history is {}", moves);
        }
    }
}

