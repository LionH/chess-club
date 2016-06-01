package org.chesscorp.club.service;

import org.alcibiade.chess.model.ChessGameStatus;
import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.dto.PlayerProfile;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.people.ClubPlayer;
import org.chesscorp.club.model.people.ExternalPlayer;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.model.people.RobotPlayer;
import org.chesscorp.club.persistence.ChessGameRepository;
import org.chesscorp.club.persistence.PlayerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class PlayerServiceTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ChessGameRepository chessGameRepository;

    @Autowired
    private PlayerService playerService;

    @Test
    @Transactional
    public void testSearchSuccess() {
        playerRepository.save(new ExternalPlayer("Freddie Mercury", "freddie"));
        playerRepository.save(new ExternalPlayer("Brian May", "brian"));
        playerRepository.save(new ExternalPlayer("John Deacon", "john"));
        playerRepository.save(new ExternalPlayer("Roger Taylor", "roger"));


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
                .isEqualToComparingFieldByField(new PlayerProfile(player1, new ArrayList<>(), new ArrayList<>()));
        Assertions.assertThat(playerService.getProfile(player2.getId()))
                .isEqualToComparingFieldByField(new PlayerProfile(player2, new ArrayList<>(), new ArrayList<>()));
        Assertions.assertThat(playerService.getProfile(player3.getId()))
                .isEqualToComparingFieldByField(new PlayerProfile(player3, new ArrayList<>(), new ArrayList<>()));

        chessGameRepository.save(new ChessGame(player1, player2, new ArrayList<>(), ChessGameStatus.BLACKWON, OffsetDateTime.now()));
        chessGameRepository.save(new ChessGame(player2, player1, new ArrayList<>(), ChessGameStatus.BLACKWON, OffsetDateTime.now()));

        Assertions.assertThat(playerService.getProfile(player1.getId()).getPvpStatistics()).hasSize(1);
        Assertions.assertThat(playerService.getProfile(player2.getId()).getPvpStatistics()).hasSize(1);
        Assertions.assertThat(playerService.getProfile(player3.getId()).getPvpStatistics()).hasSize(0);
    }

    @Test
    @Transactional
    public void testSearchAI() {
        playerRepository.save(new ExternalPlayer("External player", "ext"));
        playerRepository.save(new ClubPlayer("Player 1"));
        playerRepository.save(new ClubPlayer("Player 2"));
        playerRepository.save(new ClubPlayer("Player 3"));
        playerRepository.save(new RobotPlayer("GnuChess lvl 1", "gnuchess", "1"));
        playerRepository.save(new RobotPlayer("GnuChess lvl 2", "gnuchess", "2"));

        Assertions.assertThat(playerService.searchAI()).hasSize(2);
    }

    @Test
    @Transactional
    public void testSearchOpponents() {
        playerRepository.save(new ExternalPlayer("External player", "ext"));
        Player player1 = playerRepository.save(new ClubPlayer("Player 1"));
        playerRepository.save(new ClubPlayer("Player 2"));
        playerRepository.save(new ClubPlayer("Player 3"));
        playerRepository.save(new RobotPlayer("GnuChess lvl 1", "gnuchess", "1"));
        playerRepository.save(new RobotPlayer("GnuChess lvl 2", "gnuchess", "2"));

        Assertions.assertThat(playerService.searchOpponents(player1.getId())).hasSize(2)
                .extracting("id").doesNotContain(player1.getId());
    }
}
