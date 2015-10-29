package org.chesscorp.club.service;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.persistence.ChessGameRepository;
import org.chesscorp.club.persistence.PlayerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;

/**
 * Test the comment service operations.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@TransactionConfiguration(defaultRollback = true)
public class ChessCommentServiceTest {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ChessGameRepository chessGameRepository;
    @Autowired
    private ChessCommentService chessCommentService;

    @Test
    @Transactional
    public void testCommentPublication() {
        Player p1 = playerRepository.save(new Player("Player 1"));
        Player p2 = playerRepository.save(new Player("Player 2"));
        ChessGame game = chessGameRepository.save(new ChessGame(p1, p2));

        Assertions.assertThat(chessCommentService.getCommentsByGame(game.getId())).isEmpty();

        chessCommentService.postComment(p1, game.getId(), "Hello world !");
        Assertions.assertThat(chessCommentService.getCommentsByGame(game.getId())).hasSize(1);
        chessCommentService.postComment(p2, game.getId(), "Hello to you too !");
        Assertions.assertThat(chessCommentService.getCommentsByGame(game.getId())).hasSize(2);
    }
}
