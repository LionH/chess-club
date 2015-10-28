package org.chesscorp.club.service;

import org.chesscorp.club.dto.PlayerProfile;
import org.chesscorp.club.model.people.Player;

import java.util.List;

/**
 * Player search and data access API
 */
public interface PlayerService {

    /**
     * Search for player(s) by fuzzy query.
     *
     * @param query a query based on user input
     * @return a list of player matches.
     */
    List<Player> search(String query);

    /**
     * Get data to fill the player profile page.
     *
     * @param playerId a player ID
     * @return the profile structure
     */
    PlayerProfile getProfile(Long playerId);
}
