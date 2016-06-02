package org.chesscorp.club.service;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.model.people.ClubPlayer;
import org.chesscorp.club.model.people.RobotPlayer;
import org.chesscorp.club.persistence.PlayerRepository;
import org.chesscorp.club.persistence.RobotRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("ai-crafty")
@Rollback
public class BootstrapServiceTestCrafty {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private BootstrapService bootstrapService;

    @Test
    @Transactional
    public void testSampleData() {
        bootstrapService.populate();

        Assertions.assertThat(playerRepository.findAll()).hasSize(26)
                .hasAtLeastOneElementOfType(RobotPlayer.class)
                .hasAtLeastOneElementOfType(ClubPlayer.class);
        Assertions.assertThat(robotRepository.findAll()).hasSize(22);
    }

}
