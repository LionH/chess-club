package org.chesscorp.club.dto;

import org.chesscorp.club.model.game.EloRating;
import org.chesscorp.club.model.people.Player;

import java.util.List;
import java.util.Map;

/**
 * Player profile data structure.
 */
public class PlayerProfile {
    private Player player;
    private List<EloRating> eloHistory;
    private Map<Player, PvpStatus> pvpStatistics;

    public PlayerProfile(Player player, List<EloRating> eloHistory, Map<Player, PvpStatus> pvpStatistics) {
        this.player = player;
        this.eloHistory = eloHistory;
        this.pvpStatistics = pvpStatistics;
    }

    public Player getPlayer() {
        return player;
    }

    public List<EloRating> getEloHistory() {
        return eloHistory;
    }

    public Map<Player, PvpStatus> getPvpStatistics() {
        return pvpStatistics;
    }
}
