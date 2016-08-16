package org.chesscorp.club.controllers;

import org.chesscorp.club.dto.ChessOpeningDescription;
import org.chesscorp.club.service.ChessOpeningsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Yannick Kirschhoffer alcibiade@alcibiade.org
 */
@RestController
@RequestMapping("/api/chess/openings")
public class ChessOpeningsController {

    private ChessOpeningsService chessOpeningsService;

    @Autowired
    public ChessOpeningsController(ChessOpeningsService chessOpeningsService) {
        this.chessOpeningsService = chessOpeningsService;
    }

    /**
     * Search for matching openings.
     *
     * @param moves the first game moves
     * @return a list of matching openings.
     */
    @Transactional
    @RequestMapping(value = "/openings", method = RequestMethod.GET)
    public List<ChessOpeningDescription> getOpenings(@RequestParam List<String> moves) {
        List<ChessOpeningDescription> openings = chessOpeningsService.getOpenings(moves);
        return openings;
    }
}
