package org.chesscorp.club.controllers;

import org.chesscorp.club.Application;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.persistence.ChessPositionRepository;
import org.chesscorp.club.service.AuthenticationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@DirtiesContext
public class ChessStatsControllerTest {
    @Autowired
    private ChessStatsController chessStatsController;
    @Autowired
    private ChessGameController chessGameController;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private ChessPositionRepository positionRepository;

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testRelatedGame() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(chessStatsController).build();

        authenticationService.signup("a@b.c", "pwd", "Alcibiade");
        String alcibiadeToken = authenticationService.signin("a@b.c", "pwd");
        Player alcibiade = authenticationService.getSession(alcibiadeToken).getAccount().getPlayer();

        authenticationService.signup("b@b.c", "pwd", "Bob");
        String bobToken = authenticationService.signin("b@b.c", "pwd");
        Player bob = authenticationService.getSession(bobToken).getAccount().getPlayer();

        ChessGame game1 = chessGameController.createGame(alcibiadeToken, alcibiade.getId(), bob.getId());
        chessGameController.postMove(alcibiadeToken, game1.getId(), "e4");
        chessGameController.postMove(bobToken, game1.getId(), "e5");

        while (positionRepository.count() < 2) {
            Thread.sleep(100);
        }

        mockMvc.perform(
                get("/api/chess/stats/related/" + game1.getId())
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$", hasSize(0))
        );
    }
}
