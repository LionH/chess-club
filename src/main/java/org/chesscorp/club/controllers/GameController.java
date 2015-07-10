package org.chesscorp.club.controllers;

import org.chesscorp.club.model.ChessGame;
import org.chesscorp.club.service.ChessGameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yannick Kirschhoffer alcibiade@alcibiade.org
 */
@RestController
public class GameController {
    private Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private ChessGameService chessGameService;

    @RequestMapping(value = "/api/game", method = RequestMethod.POST)
    public ChessGame createGame(
            @RequestParam String whitePlayerId,
            @RequestParam String blackPlayerId) {
        logger.info("Creating game {} vs. {}", whitePlayerId, blackPlayerId);

        ChessGame created = chessGameService.createGame(whitePlayerId, blackPlayerId);
        logger.info("Game created: {}", created);

        return created;
    }
}
