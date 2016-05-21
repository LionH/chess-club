package org.chesscorp.club.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.chesscorp.club.Application;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.persistence.ChessMoveToPositionRepository;
import org.chesscorp.club.persistence.ChessPositionRepository;
import org.chesscorp.club.service.AuthenticationService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ChessStatsControllerTest {
    private Logger logger = LoggerFactory.getLogger(ChessStatsControllerTest.class);

    @Autowired
    private ChessStatsController chessStatsController;
    @Autowired
    private ChessGameController chessGameController;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private ChessPositionRepository positionRepository;
    @Autowired
    private ChessMoveToPositionRepository moveToPositionRepository;

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testRelatedGame() throws Exception {
        authenticationService.signup("a@b.c", "pwd", "Alcibiade");
        String alcibiadeToken = authenticationService.signin("a@b.c", "pwd");
        Player alcibiade = authenticationService.getSession(alcibiadeToken).getAccount().getPlayer();

        authenticationService.signup("b@b.c", "pwd", "Bob");
        String bobToken = authenticationService.signin("b@b.c", "pwd");
        Player bob = authenticationService.getSession(bobToken).getAccount().getPlayer();

        ChessGame game1 = chessGameController.createGame(alcibiadeToken, alcibiade.getId(), bob.getId());
        chessGameController.postMove(alcibiadeToken, game1.getId(), "e4");
        chessGameController.postMove(bobToken, game1.getId(), "e5");

        ChessGame game2 = chessGameController.createGame(alcibiadeToken, alcibiade.getId(), bob.getId());
        chessGameController.postMove(alcibiadeToken, game2.getId(), "e4");
        chessGameController.postMove(bobToken, game2.getId(), "e5");

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(chessStatsController).build();

        while (positionRepository.count() < 2 && moveToPositionRepository.count() < 4) {
            Thread.sleep(100);
        }

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
