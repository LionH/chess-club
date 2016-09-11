package org.chesscorp.club.service;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.people.ClubPlayer;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.model.stats.ChessClubPosition;
import org.chesscorp.club.persistence.ChessPositionRepository;
import org.chesscorp.club.persistence.PlayerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Analysis engine tests.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
@ActiveProfiles("ai-gnuchess")
public class ChessAnalysisServiceTest {

    private Logger logger = LoggerFactory.getLogger(ChessAnalysisServiceTest.class);

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ChessGameService chessGameService;
    @Autowired
    private ChessPositionService chessPositionService;
    @Autowired
    private ChessAnalysisService chessAnalysisService;
    @Autowired
    private ChessPositionRepository positionRepository;

    @Test
    @Transactional
    public void testSimpleAnalysis() {
        Player p1 = playerRepository.save(new ClubPlayer("Player 1"));
        Player p2 = playerRepository.save(new ClubPlayer("Player 2"));
        ChessGame game = chessGameService.createGame(p1.getId(), p2.getId());
        game = chessGameService.move(game, "e4");
        chessPositionService.updateGamePositions(game.getId());

        List<ChessClubPosition> positions = positionRepository.findAll();
        Assertions.assertThat(positions).hasSize(1);

        chessAnalysisService.analyzePosition(positions.get(0).getId());

        ChessClubPosition filledPosition = positionRepository.getOne(positions.get(0).getId());
        Assertions.assertThat(filledPosition.getScore()).isNotNull();
        Assertions.assertThat(filledPosition.getExpected()).isNotEmpty();
    }

}