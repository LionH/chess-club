package org.chesscorp.club.dto;

import org.chesscorp.club.model.Player;

/**
 * Player profile data structure.
 */
public class PlayerProfile {
    private Player player;

    public PlayerProfile(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
