package org.chesscorp.club.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.chesscorp.club.Application;
import org.chesscorp.club.jobs.GameUpdateListener;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.persistence.ChessPositionRepository;
import org.chesscorp.club.service.AuthenticationService;
import org.chesscorp.club.service.ChessAnalysisService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("ai-gnuchess")
public class ChessStatsControllerTest {
    private Logger logger = LoggerFactory.getLogger(ChessStatsControllerTest.class);

    @Autowired
    private ChessStatsController chessStatsController;
    @Autowired
    private ChessGameController chessGameController;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private GameUpdateListener gameUpdateListener;
    @Autowired
    private ChessAnalysisService chessAnalysisService;
    @Autowired
    private ChessPositionRepository positionRepository;

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testRelatedGame() throws Exception {
        authenticationService.signup("a@b.com", "pwd", "Alcibiade");
        String alcibiadeToken = authenticationService.signin("a@b.com", "pwd");
        Player alcibiade = authenticationService.getSession(alcibiadeToken).getAccount().getPlayer();

        authenticationService.signup("b@b.com", "pwd", "Bob");
        String bobToken = authenticationService.signin("b@b.com", "pwd");
        Player bob = authenticationService.getSession(bobToken).getAccount().getPlayer();

        ChessGame game1 = chessGameController.createGame(alcibiadeToken, alcibiade.getId(), bob.getId());
        chessGameController.postMove(alcibiadeToken, game1.getId(), "e4");
        chessGameController.postMove(bobToken, game1.getId(), "e5");
        game1 = chessGameController.getGame(game1.getId());

        ChessGame game2 = chessGameController.createGame(alcibiadeToken, alcibiade.getId(), bob.getId());
        chessGameController.postMove(alcibiadeToken, game2.getId(), "e4");
        chessGameController.postMove(bobToken, game2.getId(), "e5");
        game2 = chessGameController.getGame(game2.getId());

        gameUpdateListener.gameUpdated(game1.getId());
        gameUpdateListener.gameUpdated(game2.getId());

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(chessStatsController).build();
        ObjectMapper mapper = new ObjectMapper();
        StringWriter jsonWriter = new StringWriter();
        mapper.writeValue(jsonWriter, chessStatsController.relatedGames(game1.getId()));
        logger.debug("Result: {}", jsonWriter.toString());

        mockMvc.perform(
                get("/api/chess/stats/related/" + game1.getId())
//        ).andDo(
//                print()
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$", hasSize(1))
        ).andExpect(
                jsonPath("$[0].id", Matchers.equalTo(game2.getId().intValue()))
        );

        // Analysis is empty as positions are not processed yet
        mockMvc.perform(
                get("/api/chess/stats/analysis/" + game1.getId())
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$.movesAnalysis", hasSize(0))
        ).andExpect(
                jsonPath("$.gameId", Matchers.equalTo(game1.getId().intValue()))
        );

        positionRepository.findAll().forEach(position -> chessAnalysisService.analyzePosition(position.getId()));

        // Now analysis is filled

        mockMvc.perform(
                get("/api/chess/stats/analysis/" + game1.getId())
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$.movesAnalysis", hasSize(game1.getMoves().size()))
        ).andExpect(
                jsonPath("$.gameId", Matchers.equalTo(game1.getId().intValue()))
        );
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testNotAvailableYet() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(chessStatsController).build();

        mockMvc.perform(
                get("/api/chess/stats/related/999999")
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$", hasSize(0))
        );
    }
}
