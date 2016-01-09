package org.chesscorp.club.service;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.game.ChessMove;
import org.chesscorp.club.model.people.ClubPlayer;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.model.people.RobotPlayer;
import org.chesscorp.club.persistence.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class BootstrapServiceTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private ChessGameRepository chessGameRepository;

    @Autowired
    private ChessMoveRepository chessMoveRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BootstrapService bootstrapService;

    @Test
    @Transactional
    public void testSampleData() {
        bootstrapService.populate();

        Assertions.assertThat(playerRepository.findAll()).hasSize(19)
                .hasAtLeastOneElementOfType(RobotPlayer.class)
                .hasAtLeastOneElementOfType(ClubPlayer.class);
        Assertions.assertThat(accountRepository.findAll()).hasSize(4);
        Assertions.assertThat(robotRepository.findAll()).hasSize(15);
        Assertions.assertThat(chessGameRepository.findAll()).hasSize(2);
    }

    @Test
    @Transactional
    public void testPgnFix() {
        Player p1 = playerRepository.save(new ClubPlayer("P1"));
        Player p2 = playerRepository.save(new ClubPlayer("P1"));
        ChessGame game = new ChessGame(p1, p2);

        ChessMove move1 = game.addMove(OffsetDateTime.now(), "Pe2e4");
        ChessMove move2 = game.addMove(OffsetDateTime.now(), "Pd7d5");
        chessGameRepository.save(game);
        chessMoveRepository.save(move1);
        chessMoveRepository.save(move2);

        bootstrapService.fixPgnNotationInGames();

        ChessGame fixedGame = chessGameRepository.getOne(game.getId());
        Assertions.assertThat(fixedGame.getMoves().get(0).getPgn()).isEqualTo("e4");
        Assertions.assertThat(fixedGame.getMoves().get(1).getPgn()).isEqualTo("d5");
    }
}
