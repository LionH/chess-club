package org.chesscorp.club.controllers;

import org.chesscorp.club.exception.ChessException;
import org.chesscorp.club.model.ChessGame;
import org.chesscorp.club.model.Player;
import org.chesscorp.club.service.AuthenticationService;
import org.chesscorp.club.service.ChessGameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

/**
 * @author Yannick Kirschhoffer alcibiade@alcibiade.org
 */
@RestController
@RequestMapping("/api/chess/game")
public class ChessGameController {
    private Logger logger = LoggerFactory.getLogger(ChessGameController.class);

    @Autowired
    private ChessGameService chessGameService;

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Create a new game between two players.
     *
     * @param whitePlayerId identifier of the white player
     * @param blackPlayerId identifier of the black player
     * @return the created game model
     */
    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    public ChessGame createGame(
            @RequestParam String authenticationToken,
            @RequestParam String whitePlayerId,
            @RequestParam String blackPlayerId) {
        Player player = authenticationService.getPlayer(authenticationToken);
        if (!player.getId().equals(whitePlayerId) && !player.getId().equals(blackPlayerId)) {
            throw new ChessException("Can't create a game without playing in it.");
        }

        ChessGame created = chessGameService.createGame(whitePlayerId, blackPlayerId);
        logger.info("Game created {} vs. {} created: {}", whitePlayerId, blackPlayerId, created);

        return created;
    }

    @Transactional
    @RequestMapping(value = "/{gameId}", method = RequestMethod.GET)
    public ChessGame getGame(@PathVariable String gameId) {
        ChessGame game = chessGameService.getGame(gameId);
        logger.info("Game fetched: {}", game);

        return game;
    }
}
