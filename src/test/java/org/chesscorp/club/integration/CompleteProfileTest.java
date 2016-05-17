package org.chesscorp.club.integration;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.dto.PlayerProfile;
import org.chesscorp.club.model.people.ClubPlayer;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.persistence.PlayerRepository;
import org.chesscorp.club.service.PlayerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Full profile test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class CompleteProfileTest {

    @Autowired
    private ChessGameFactory chessGameFactory;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    @Transactional
    public void testProfile() {
        Player p1 = playerRepository.save(new ClubPlayer("Player 1"));
        Player p2 = playerRepository.save(new ClubPlayer("Player 2"));

        chessGameFactory.createGameWonByWhite(p1, p2);
        chessGameFactory.createGameWonByWhite(p1, p2);
        chessGameFactory.createGameWonByBlack(p1, p2);

        PlayerProfile p1Profile = playerService.getProfile(p1.getId());
        Assertions.assertThat(p1Profile.getEloHistory()).hasSize(3).isSorted();
        Assertions.assertThat(p1Profile.getPvpStatistics()).hasSize(1);
    }
}
