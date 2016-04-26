package org.chesscorp.club.controllers;

import org.chesscorp.club.dto.PlayerProfile;
import org.chesscorp.club.model.people.ClubPlayer;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.model.people.RobotPlayer;
import org.chesscorp.club.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Yannick Kirschhoffer alcibiade@alcibiade.org
 */
@RestController
@RequestMapping("/api/player")
public class PlayerController {
    private Logger logger = LoggerFactory.getLogger(PlayerController.class);

    @Autowired
    private PlayerService playerService;

    @Transactional(readOnly = true)
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public List<Player> search(
            @RequestParam String query) {
        List<Player> players = playerService.search(query);
        logger.debug("Found {} players matching {}", players.size(), query);
        return players;
    }

    @Transactional(readOnly = true)
    @RequestMapping(value = "/search-ai", method = RequestMethod.GET)
    public List<RobotPlayer> searchAI() {
        List<RobotPlayer> players = playerService.searchAI();
        logger.debug("Found {} AI players", players.size());
        return players;
    }

    @Transactional(readOnly = true)
    @RequestMapping(value = "/search-opponents", method = RequestMethod.GET)
    public List<ClubPlayer> searchOpponents(@RequestParam Long playerId) {
        List<ClubPlayer> players = playerService.searchOpponents(playerId);
        logger.debug("Found {} opponent players", players.size());
        return players;
    }

    @Transactional(readOnly = true)
    @RequestMapping(value = "/profile/{playerId}", method = RequestMethod.GET)
    public PlayerProfile getProfile(
            @PathVariable Long playerId) {
        PlayerProfile profile = playerService.getProfile(playerId);
        logger.debug("Found profile for player {}: {}", playerId, profile.getPlayer().getDisplayName());
        return profile;
    }
}
