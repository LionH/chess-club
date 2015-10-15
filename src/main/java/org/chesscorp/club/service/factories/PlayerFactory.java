package org.chesscorp.club.service.factories;

import org.chesscorp.club.model.Player;

/**
 * Search for players and create them dynamically if required.
 */
public interface PlayerFactory {

    /**
     * Find a player based on its display name. It there is no matching player, a new instance is created on the fly.
     * Note that the player will already be persisted on returning and have a valid ID.
     *
     * @param displayName the display name of the searched player
     * @return an existing player informations or a newly created instance.
     */
    Player findOrCreatePlayer(String displayName);
}
