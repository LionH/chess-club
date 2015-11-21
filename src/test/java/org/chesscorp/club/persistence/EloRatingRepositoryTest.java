package org.chesscorp.club.persistence;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.game.EloRating;
import org.chesscorp.club.model.people.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class EloRatingRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ChessGameRepository chessGameRepository;

    @Autowired
    private EloRatingRepository eloRatingRepository;

    @Test
    @Transactional
    public void testRegistration() {
        Player p1 = playerRepository.save(new Player("Player 1"));
        Player p2 = playerRepository.save(new Player("Player 2"));

        ChessGame game1 = chessGameRepository.save(new ChessGame(p1, p2));
        ChessGame game2 = chessGameRepository.save(new ChessGame(p1, p2));

        EloRating rating1 = eloRatingRepository.save(new EloRating(p1, game1, 1201));
        EloRating rating2 = eloRatingRepository.save(new EloRating(p2, game1, 1199));

        Assertions.assertThat(eloRatingRepository.findAll()).hasSize(2);
        Assertions.assertThat(eloRatingRepository.findFirstByPlayerIdOrderByIdDesc(p1.getId()).getEloRating()).isEqualTo(1201);
        Assertions.assertThat(eloRatingRepository.findFirstByPlayerIdOrderByIdDesc(p2.getId()).getEloRating()).isEqualTo(1199);

        EloRating rating3 = eloRatingRepository.save(new EloRating(p1, game2, 1202));
        EloRating rating4 = eloRatingRepository.save(new EloRating(p2, game2, 1198));

        Assertions.assertThat(eloRatingRepository.findAll()).hasSize(4);
        Assertions.assertThat(eloRatingRepository.findFirstByPlayerIdOrderByIdDesc(p1.getId()).getEloRating()).isEqualTo(1202);
        Assertions.assertThat(eloRatingRepository.findFirstByPlayerIdOrderByIdDesc(p2.getId()).getEloRating()).isEqualTo(1198);
    }

}
