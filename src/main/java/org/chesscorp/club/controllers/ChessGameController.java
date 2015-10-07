package org.chesscorp.club.controllers;

import org.chesscorp.club.exception.ChessException;
import org.chesscorp.club.exception.InvalidChessMoveException;
import org.chesscorp.club.model.ChessGame;
import org.chesscorp.club.model.Player;
import org.chesscorp.club.service.AuthenticationService;
import org.chesscorp.club.service.ChessGameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

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
     * Search for games.
     *
     * @param playerId identifier of a player involved in the game.
     * @return a list of games
     */
    @Transactional
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public List<ChessGame> search(
            @RequestParam Number playerId) {
        List<ChessGame> games = chessGameService.searchGames(playerId);
        logger.debug("Found {} games for player {}", games.size(), playerId);

        return games;
    }

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
            @CookieValue(value = AuthenticationController.AUTHENTICATION_TOKEN) String authenticationToken,
            @RequestParam Number whitePlayerId,
            @RequestParam Number blackPlayerId) {
        Player player = authenticationService.getSession(authenticationToken).getAccount().getPlayer();
        if (!player.getId().equals(whitePlayerId) && !player.getId().equals(blackPlayerId)) {
            throw new ChessException("Can't create a game without playing in it.");
        }

        ChessGame created = chessGameService.createGame(whitePlayerId, blackPlayerId);
        logger.info("Game created {} vs. {} created: {}", whitePlayerId, blackPlayerId, created);

        return created;
    }

    @Transactional
    @RequestMapping(value = "/{gameId}", method = RequestMethod.GET)
    public ChessGame getGame(@PathVariable Number gameId) {
        ChessGame game = chessGameService.getGame(gameId);
        logger.info("Game fetched: {}", game);

        return game;
    }

    @Transactional
    @RequestMapping(value = "/{gameId}", method = RequestMethod.POST)
    public ChessGame postMove(
            @CookieValue(value = AuthenticationController.AUTHENTICATION_TOKEN) String authenticationToken,
            @PathVariable Number gameId,
            @RequestParam String move) {
        Player player = authenticationService.getSession(authenticationToken).getAccount().getPlayer();
        ChessGame game = chessGameService.getGame(gameId);

        Player nextPlayer = game.getNextPlayer();

        if (!player.equals(nextPlayer)) {
            throw new InvalidChessMoveException("It is " + nextPlayer.getDisplayName() + "'s turn");
        }

        game = chessGameService.move(game, move);
        logger.info("Move {} played in {}", move, game);

        return game;
    }
}
