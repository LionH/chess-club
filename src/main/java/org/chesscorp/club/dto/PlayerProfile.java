package org.chesscorp.club.dto;

import org.chesscorp.club.model.EloRank;
import org.chesscorp.club.model.Player;

import java.util.List;

/**
 * Player profile data structure.
 */
public class PlayerProfile {
    private Player player;
    private List<EloRank> eloHistory;

    public PlayerProfile(Player player, List<EloRank> eloHistory) {
        this.player = player;
        this.eloHistory = eloHistory;
    }

    public Player getPlayer() {
        return player;
    }

    public List<EloRank> getEloHistory() {
        return eloHistory;
    }
}
