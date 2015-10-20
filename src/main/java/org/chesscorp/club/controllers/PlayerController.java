package org.chesscorp.club.controllers;

import org.chesscorp.club.dto.PlayerProfile;
import org.chesscorp.club.model.Player;
import org.chesscorp.club.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api/player")
public class PlayerController {
    private Logger logger = LoggerFactory.getLogger(PlayerController.class);

    @Autowired
    private PlayerService playerService;

    @Transactional
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public List<Player> search(
            @RequestParam String query) {
        List<Player> players = playerService.search(query);
        logger.debug("Found {} players matching {}", players.size(), query);
        return players;
    }

    @Transactional
    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public PlayerProfile getProfile(
            @RequestParam Long playerId) {
        PlayerProfile profile = playerService.getProfile(playerId);
        logger.debug("Found profile for player {}: {}", playerId, profile.getPlayer().getDisplayName());
        return profile;
    }
}
