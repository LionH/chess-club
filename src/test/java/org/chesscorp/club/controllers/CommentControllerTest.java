package org.chesscorp.club.controllers;

import org.chesscorp.club.Application;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.persistence.ChessGameRepository;
import org.chesscorp.club.persistence.PlayerRepository;
import org.chesscorp.club.service.AuthenticationService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class CommentControllerTest {

    @Autowired
    private CommentController commentController;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ChessGameRepository chessGameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    @Transactional
    public void testCommentPosting() throws Exception {

        authenticationService.signup("email@domain.com", "pwd", "Player 1");
        String authenticationToken = authenticationService.signin("email@domain.com", "pwd");

        Player p1 = playerRepository.findByDisplayName("Player 1").get(0);
        Player p2 = playerRepository.save(new Player("Player 2"));
        ChessGame game = chessGameRepository.save(new ChessGame(p1, p2));


        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();

        // Read an empty set of messages

        mockMvc.perform(
                get("/api/comment/game/" + game.getId())
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$", hasSize(0))
        );

        // Prevent post for unauthenticated endpoints

        mockMvc.perform(
                post("/api/comment/game/" + game.getId())
                        .param("text", "Hello world !")
                        .cookie(new Cookie(AuthenticationController.AUTHENTICATION_TOKEN, "BogusToken"))
        ).andExpect(
                status().is4xxClientError()
        );

        // Post a new message

        mockMvc.perform(
                post("/api/comment/game/" + game.getId())
                        .param("text", "Hello world !")
                        .cookie(new Cookie(AuthenticationController.AUTHENTICATION_TOKEN, authenticationToken))
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$", hasSize(1))
        );

        // Read the comment

        mockMvc.perform(
                get("/api/comment/game/" + game.getId())
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$", hasSize(1))
        );
    }

    @Test
    @Transactional
    public void testHtmlEscapeAndMarkdown() throws Exception {

        authenticationService.signup("email@domain.com", "pwd", "Player 1");
        String authenticationToken = authenticationService.signin("email@domain.com", "pwd");

        Player p1 = playerRepository.findByDisplayName("Player 1").get(0);
        Player p2 = playerRepository.save(new Player("Player 2"));
        ChessGame game = chessGameRepository.save(new ChessGame(p1, p2));


        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();

        // Post a new comment with html syntax

        mockMvc.perform(
                post("/api/comment/game/" + game.getId())
                        .param("text", "Hello world<br /> &nbsp; !")
                        .cookie(new Cookie(AuthenticationController.AUTHENTICATION_TOKEN, authenticationToken))
        ).andExpect(
                status().is2xxSuccessful()
        );

        // Read the comment

        mockMvc.perform(
                get("/api/comment/game/" + game.getId())
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$[0].text", Matchers.is("Hello world<br /> &nbsp; !"))
        ).andExpect(
                jsonPath("$[0].html", Matchers.is("<p>Hello world&lt;br /&gt; &amp;nbsp; !</p>\n"))
        );
    }
}