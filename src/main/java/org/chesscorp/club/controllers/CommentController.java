package org.chesscorp.club.controllers;

import org.chesscorp.club.model.comment.ChessComment;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.service.AuthenticationService;
import org.chesscorp.club.service.ChessCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Yannick Kirschhoffer alcibiade@alcibiade.org
 */
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ChessCommentService chessCommentService;

    @Transactional
    @RequestMapping(value = "/game/{gameId}", method = RequestMethod.GET)
    public List<ChessComment> getByGame(@PathVariable Long gameId) {
        return chessCommentService.getCommentsByGame(gameId);
    }

    @Transactional
    @RequestMapping(value = "/game/{gameId}", method = RequestMethod.POST)
    public List<ChessComment> postComment(
            @CookieValue(value = AuthenticationController.AUTHENTICATION_TOKEN) String authenticationToken,
            @PathVariable Long gameId,
            @RequestParam String text) {
        Player player = authenticationService.getSession(authenticationToken).getAccount().getPlayer();
        chessCommentService.postComment(player, gameId, text);
        return chessCommentService.getCommentsByGame(gameId);
    }
}
