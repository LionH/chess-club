package org.chesscorp.club.service;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.dto.ChessOpeningDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class ChessOpeningsServiceTest {
    private Logger logger = LoggerFactory.getLogger(ChessGameServiceTest.class);

    @Autowired
    private ChessOpeningsService chessOpeningsService;

    @Test
    @Transactional
    public void testEmptyOpenings() {
        List<ChessOpeningDescription> openings = chessOpeningsService.getOpenings(new ArrayList<>());
        Assertions.assertThat(openings).isEmpty();
    }

    @Test
    @Transactional
    public void testKingPawn() {
        ArrayList<String> moves = new ArrayList<>();
        moves.add("e4");

        List<ChessOpeningDescription> openings = chessOpeningsService.getOpenings(moves);
        Assertions.assertThat(openings).isNotEmpty();
        Assertions.assertThat(openings.get(0).getVariants()).hasSize(18);
    }

    @Test
    @Transactional
    public void testKingPawnStep1() {
        ArrayList<String> moves = new ArrayList<>();
        moves.add("e4");
        moves.add("e5");

        List<ChessOpeningDescription> openings = chessOpeningsService.getOpenings(moves);
        Assertions.assertThat(openings).isNotEmpty();
        Assertions.assertThat(openings.get(0).getVariants()).hasSize(12);
    }

    @Test
    @Transactional
    public void testPositionMatching() {
        ArrayList<String> moves = new ArrayList<>();
        moves.add("e4");
        moves.add("Nc6");
        moves.add("d4");
        moves.add("Nf6");

        List<ChessOpeningDescription> openings = chessOpeningsService.getOpenings(moves);
        Assertions.assertThat(openings).isEmpty();
    }
}
