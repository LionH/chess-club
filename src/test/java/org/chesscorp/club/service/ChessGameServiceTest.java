package org.chesscorp.club.service;

import org.alcibiade.chess.model.ChessGameStatus;
import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.exception.InvalidChessMoveException;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.game.ChessMove;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.model.people.RobotPlayer;
import org.chesscorp.club.persistence.ChessMoveRepository;
import org.chesscorp.club.persistence.EloRatingRepository;
import org.chesscorp.club.persistence.PlayerRepository;
import org.chesscorp.club.persistence.RobotRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class ChessGameServiceTest {
    private Logger logger = LoggerFactory.getLogger(ChessGameServiceTest.class);

    @Autowired
    private ChessGameService chessGameService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ChessMoveRepository chessMoveRepository;

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private EloRatingRepository eloRatingRepository;

    @Test
    @Transactional
    public void testGameOperations() throws InterruptedException {
        Player p1 = playerRepository.save(new Player("Player 1"));
        Player p2 = playerRepository.save(new Player("Player 2"));

        ChessGame game = chessGameService.createGame(p1.getId(), p2.getId());
        Thread.sleep(10);
        Assertions.assertThat(game.getWhitePlayer()).isEqualToComparingFieldByField(p1);
        Assertions.assertThat(game.getBlackPlayer()).isEqualToComparingFieldByField(p2);
        Assertions.assertThat(game.getId().longValue()).isGreaterThan(0L);
        Assertions.assertThat(game.getStartDate()).isBeforeOrEqualTo(OffsetDateTime.now());

        game = chessGameService.move(game, "e2e4");
        Assertions.assertThat(game.getMoves()).extracting(ChessMove::getPgn).containsExactly("e4");

        game = chessGameService.move(game, "e7e5");
        Assertions.assertThat(game.getMoves()).extracting(ChessMove::getPgn).containsExactly("e4", "e5");

        Assertions.assertThat(chessGameService.searchGames(12345L, null)).isEmpty();

        Assertions.assertThat(chessGameService.searchGames(p1.getId(), null)).hasSize(1).containsExactly(game);
        Assertions.assertThat(chessGameService.searchGames(p2.getId(), null)).hasSize(1).containsExactly(game);
    }

    @Test
    @Transactional
    public void testSaveAndReload() throws InterruptedException {
        Player p1 = playerRepository.save(new Player("Player 1"));
        Player p2 = playerRepository.save(new Player("Player 2"));

        ChessGame game = chessGameService.createGame(p1.getId(), p2.getId());
        game = chessGameService.move(game, "e2e4");
        game = chessGameService.move(game, "e7e5");

        ChessGame game2 = chessGameService.getGame(game.getId());
        Assertions.assertThat(game2.getMoves()).isNotEmpty();
        Assertions.assertThat(chessMoveRepository.findByGameId(game.getId())).isNotEmpty();
    }

    @Test
    @Transactional
    public void testRobotAsBlack() {
        Player p1 = playerRepository.save(new Player("Player 1"));
        RobotPlayer rob = robotRepository.save(new RobotPlayer("rob", "randomAI", ""));

        ChessGame game = chessGameService.createGame(p1.getId(), rob.getId());
        Assertions.assertThat(game.getMoves()).isEmpty();
        game = chessGameService.move(game, "e4");
        Assertions.assertThat(game.getMoves()).hasSize(2);
    }

    @Test
    @Transactional
    public void testRobotAsWhite() {
        Player p1 = playerRepository.save(new Player("Player 1"));
        RobotPlayer rob = robotRepository.save(new RobotPlayer("rob", "randomAI", "{level=3}"));

        ChessGame game = chessGameService.createGame(rob.getId(), p1.getId());
        Assertions.assertThat(game.getMoves()).isNotEmpty();
    }

    @Test(expected = RuntimeException.class)
    @Transactional
    public void testUnknownAI() {
        Player p1 = playerRepository.save(new Player("Player 1"));
        RobotPlayer rob = robotRepository.save(new RobotPlayer("rob", "bogusAI", "{level=3}"));

        ChessGame game = chessGameService.createGame(rob.getId(), p1.getId());
    }

    @Test(expected = IllegalStateException.class)
    @Transactional
    public void testRefuseSamePlayer() {
        Player p1 = playerRepository.save(new Player("Player 1"));
        chessGameService.createGame(p1.getId(), p1.getId());
    }

    @Test
    @Transactional
    public void testGameEndHooksWhiteWon() {
        Player p1 = playerRepository.save(new Player("Player 1"));
        Player p2 = playerRepository.save(new Player("Player 2"));

        // 1. e4 e5 2. Qh5 Nc6 3. Bc4 Nf6 4. Qxf7#
        ChessGame game = chessGameService.createGame(p1.getId(), p2.getId());
        game = chessGameService.move(game, "e4");
        game = chessGameService.move(game, "e5");
        game = chessGameService.move(game, "Qh5");
        game = chessGameService.move(game, "Nc6");
        game = chessGameService.move(game, "Bc4");
        game = chessGameService.move(game, "Nf6");
        game = chessGameService.move(game, "Qxf7#");

        eloRatingRepository.findAll()
                .stream()
                .forEach(r -> logger.debug("Rating: {}", r));

        Assertions.assertThat(game.getStatus()).isEqualTo(ChessGameStatus.WHITEWON);

        Assertions.assertThat(
                eloRatingRepository.findAll()
                        .stream()
                        .map(rank -> rank.getPlayer().getDisplayName())
                        .collect(Collectors.toList())
        ).containsExactly("Player 1", "Player 2");

        Assertions.assertThat(eloRatingRepository.findFirstByPlayerIdOrderByIdDesc(p1.getId()).getEloRating()).isEqualTo(1210);
        Assertions.assertThat(eloRatingRepository.findFirstByPlayerIdOrderByIdDesc(p2.getId()).getEloRating()).isEqualTo(1190);
    }

    @Test(expected = InvalidChessMoveException.class)
    @Transactional
    public void testBogusMove() {
        Player p1 = playerRepository.save(new Player("Player 1"));
        Player p2 = playerRepository.save(new Player("Player 2"));
        ChessGame game = chessGameService.createGame(p1.getId(), p2.getId());
        game = chessGameService.move(game, "e4");
        game = chessGameService.move(game, "exxx");
    }

    @Test(expected = InvalidChessMoveException.class)
    @Transactional
    public void testIllegalMove() {
        Player p1 = playerRepository.save(new Player("Player 1"));
        Player p2 = playerRepository.save(new Player("Player 2"));
        ChessGame game = chessGameService.createGame(p1.getId(), p2.getId());
        game = chessGameService.move(game, "e5");
    }
}
