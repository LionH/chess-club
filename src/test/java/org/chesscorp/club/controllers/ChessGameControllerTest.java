package org.chesscorp.club.controllers;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.exception.ChessClubException;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.game.ChessMove;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.service.AuthenticationService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.time.OffsetDateTime;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class ChessGameControllerTest {
    private Logger logger = LoggerFactory.getLogger(ChessGameControllerTest.class);

    @Autowired
    private ChessGameController chessGameController;

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    public void testEmptyMvcController() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(chessGameController).build();

        mockMvc.perform(
                post("/api/chess/game/search").param("playerId", "777")
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$", hasSize(0))
        );
    }

    @Test
    @Transactional
    public void testBasicGame() throws Exception {
        authenticationService.signup("a@b.com", "pwd", "Alcibiade");
        String alcibiadeToken = authenticationService.signin("a@b.com", "pwd");
        Player alcibiade = authenticationService.getSession(alcibiadeToken).getAccount().getPlayer();


        authenticationService.signup("b@b.com", "pwd", "Bob");
        String bobToken = authenticationService.signin("b@b.com", "pwd");
        Player bob = authenticationService.getSession(bobToken).getAccount().getPlayer();

        /*
         * Game creation.
         */

        ChessGame game1 = chessGameController.createGame(alcibiadeToken, alcibiade.getId(), bob.getId());
        Assertions.assertThat(game1.getWhitePlayer()).isEqualToComparingFieldByField(alcibiade);
        Assertions.assertThat(game1.getBlackPlayer()).isEqualToComparingFieldByField(bob);
        Assertions.assertThat(game1.getId().longValue()).isGreaterThan(0L);
        Assertions.assertThat(game1.getStartDate()).isBeforeOrEqualTo(OffsetDateTime.now());

        /*
         * Game fetch
         */

        ChessGame game2 = chessGameController.getGame(game1.getId());
        Assertions.assertThat(game2).isEqualToComparingFieldByField(game1);

        /*
         * Game moves
         */

        ChessGame game3 = chessGameController.postMove(alcibiadeToken, game1.getId(), "e4");
        Assertions.assertThat(game3.getMoves()).extracting(ChessMove::getPgn).containsExactly("e4");

        ChessGame game4 = chessGameController.postMove(bobToken, game1.getId(), "e5");
        Assertions.assertThat(game4.getMoves()).extracting(ChessMove::getPgn).containsExactly("e4", "e5");

        List<ChessGame> games = chessGameController.search(alcibiade.getId(), true);
        Assertions.assertThat(games).containsExactly(game1);

        Assertions.assertThat(chessGameController.search(alcibiade.getId(), false)).isEmpty();
        Assertions.assertThat(chessGameController.search(alcibiade.getId(), null)).isEqualTo(games);

        /*
         * Search games from the controller itself.
         */
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(chessGameController).build();

        mockMvc.perform(
                post("/api/chess/game/search").param("playerId", alcibiade.getId().toString())
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$", hasSize(1))
        );

        /*
         * Load games by id from the controller itself.
         */

        mockMvc.perform(
                get("/api/chess/game/" + game1.getId())
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$.status", Matchers.is("OPEN"))
        ).andExpect(
                jsonPath("$.moves", hasSize(2))
        );

        /*
         * Post a move.
         */

        mockMvc.perform(
                post("/api/chess/game/" + game1.getId())
                        .cookie(new Cookie(AuthenticationController.AUTHENTICATION_TOKEN, bobToken))
                        .param("move", "d4")
        ).andExpect(
                status().is5xxServerError()
        );

        mockMvc.perform(
                post("/api/chess/game/" + game1.getId())
                        .param("move", "d4")
        ).andExpect(
                status().is4xxClientError()
        );

        mockMvc.perform(
                post("/api/chess/game/" + game1.getId())
                        .cookie(new Cookie(AuthenticationController.AUTHENTICATION_TOKEN, alcibiadeToken))
                        .param("move", "d4")
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$.status", Matchers.is("OPEN"))
        ).andExpect(
                jsonPath("$.moves", hasSize(3))
        );
    }

    @Test(expected = ChessClubException.class)
    @Transactional
    public void testRefuseThirdPartyCreation() throws InterruptedException {
        authenticationService.signup("a@b.com", "pwd", "Alcibiade");
        String alcibiadeToken = authenticationService.signin("a@b.com", "pwd");
        Player alcibiade = authenticationService.getSession(alcibiadeToken).getAccount().getPlayer();

        authenticationService.signup("b@b.com", "pwd", "Bob");
        String bobToken = authenticationService.signin("b@b.com", "pwd");
        Player bob = authenticationService.getSession(bobToken).getAccount().getPlayer();

        authenticationService.signup("c@b.com", "pwd", "Charlie");
        String charlieToken = authenticationService.signin("c@b.com", "pwd");

        ChessGame game1 = chessGameController.createGame(charlieToken, alcibiade.getId(), bob.getId());
        Thread.sleep(10);
        Assertions.assertThat(game1.getWhitePlayer()).isEqualToComparingFieldByField(alcibiade);
        Assertions.assertThat(game1.getBlackPlayer()).isEqualToComparingFieldByField(bob);
        Assertions.assertThat(game1.getId().longValue()).isGreaterThan(0L);
        Assertions.assertThat(game1.getStartDate()).isBeforeOrEqualTo(OffsetDateTime.now());

        ChessGame game2 = chessGameController.getGame(game1.getId());
        Assertions.assertThat(game2).isEqualToComparingFieldByField(game1);
    }

    @Test(expected = ChessClubException.class)
    @Transactional
    public void testRefuseMove() {
        authenticationService.signup("a@b.com", "pwd", "Alcibiade");
        String alcibiadeToken = authenticationService.signin("a@b.com", "pwd");
        Player alcibiade = authenticationService.getSession(alcibiadeToken).getAccount().getPlayer();

        authenticationService.signup("b@b.com", "pwd", "Bob");
        String bobToken = authenticationService.signin("b@b.com", "pwd");
        Player bob = authenticationService.getSession(bobToken).getAccount().getPlayer();

        ChessGame game1 = chessGameController.createGame(alcibiadeToken, alcibiade.getId(), bob.getId());
        chessGameController.postMove(alcibiadeToken, game1.getId(), "e4");
        // Refuse 2nd move from same player
        chessGameController.postMove(alcibiadeToken, game1.getId(), "e5");
    }

}
