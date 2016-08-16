package org.chesscorp.club.controllers;

import org.chesscorp.club.dto.ChessAnalysis;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.service.ChessPositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Yannick Kirschhoffer alcibiade@alcibiade.org
 */
@RestController
@RequestMapping("/api/chess/stats")
public class ChessStatsController {
    private Logger logger = LoggerFactory.getLogger(ChessStatsController.class);

    private ChessPositionService chessPositionService;

    @Autowired
    public ChessStatsController(ChessPositionService chessPositionService) {
        this.chessPositionService = chessPositionService;
    }

    /**
     * Search for related games.
     *
     * @param gameId of a game
     * @return a list of related games, ie. games which contains the same position as the current one.
     */
    @Transactional(readOnly = true)
    @RequestMapping(value = "/related/{gameId}", method = RequestMethod.GET)
    public List<ChessGame> relatedGames(
            @PathVariable Number gameId
    ) {
        List<ChessGame> games = chessPositionService.findRelatedGames(gameId);
        logger.debug("Found {} related games for game {}: {}", games.size(), gameId, games);
        return games;
    }

    /**
     * Get game analysis.
     *
     * @param gameId of a game
     * @return a game analysis model
     */
    @Transactional(readOnly = true)
    @RequestMapping(value = "/analysis/{gameId}", method = RequestMethod.GET)
    public ChessAnalysis gameAnalysis(
            @PathVariable Number gameId
    ) {
        ChessAnalysis analysis = chessPositionService.getGameAnalysis(gameId);
        return analysis;
    }
}
