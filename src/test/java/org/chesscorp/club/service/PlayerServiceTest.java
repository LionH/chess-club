package org.chesscorp.club.service;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.dto.PlayerProfile;
import org.chesscorp.club.model.people.ClubPlayer;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.persistence.PlayerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class PlayerServiceTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerService playerService;

    @Test
    @Transactional
    public void testSearchSuccess() {
        playerRepository.save(new Player("Freddie Mercury"));
        playerRepository.save(new Player("Brian May"));
        playerRepository.save(new Player("John Deacon"));
        playerRepository.save(new Player("Roger Taylor"));


        Assertions.assertThat(playerService.search("")).hasSize(4);
        Assertions.assertThat(playerService.search("Billy")).isEmpty();
        Assertions.assertThat(playerService.search("bri")).hasSize(1);
        Assertions.assertThat(playerService.search("ER")).hasSize(2);
    }

    @Test
    @Transactional
    public void testProfile() {
        Player player1 = playerRepository.save(new ClubPlayer("Player 1"));
        Player player2 = playerRepository.save(new ClubPlayer("Player 2"));
        Player player3 = playerRepository.save(new ClubPlayer("Player 3"));

        Assertions.assertThat(playerService.getProfile(player1.getId()))
                .isEqualToComparingFieldByField(new PlayerProfile(player1, new ArrayList<>()));
        Assertions.assertThat(playerService.getProfile(player2.getId()))
                .isEqualToComparingFieldByField(new PlayerProfile(player2, new ArrayList<>()));
        Assertions.assertThat(playerService.getProfile(player3.getId()))
                .isEqualToComparingFieldByField(new PlayerProfile(player3, new ArrayList<>()));
    }
}
