package org.chesscorp.club.persistence;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.model.ChessGame;
import org.chesscorp.club.model.EloRank;
import org.chesscorp.club.model.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@TransactionConfiguration(defaultRollback = true)
public class EloRankRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ChessGameRepository chessGameRepository;

    @Autowired
    private EloRankRepository eloRankRepository;

    @Test
    @Transactional
    public void testRegistration() {
        Player p1 = playerRepository.save(new Player("Player 1"));
        Player p2 = playerRepository.save(new Player("Player 2"));

        ChessGame game1 = chessGameRepository.save(new ChessGame(p1, p2));
        ChessGame game2 = chessGameRepository.save(new ChessGame(p1, p2));

        EloRank rank1 = eloRankRepository.save(new EloRank(p1, game1, 1201));
        EloRank rank2 = eloRankRepository.save(new EloRank(p2, game1, 1199));

        EloRank rank3 = eloRankRepository.save(new EloRank(p1, game2, 1202));
        EloRank rank4 = eloRankRepository.save(new EloRank(p2, game2, 1198));

        Assertions.assertThat(eloRankRepository.findAll()).hasSize(4);
    }

}
