package org.chesscorp.club.dto;

import org.chesscorp.club.model.game.EloRating;
import org.chesscorp.club.model.people.Player;

import java.util.List;

/**
 * Player profile data structure.
 */
public class PlayerProfile {
    private Player player;
    private List<EloRating> eloHistory;

    public PlayerProfile(Player player, List<EloRating> eloHistory) {
        this.player = player;
        this.eloHistory = eloHistory;
    }

    public Player getPlayer() {
        return player;
    }

    public List<EloRating> getEloHistory() {
        return eloHistory;
    }
}
